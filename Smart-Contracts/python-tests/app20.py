# app20.py
from flask import Flask, render_template, request, jsonify
from web3 import Web3
from solcx import compile_standard, install_solc, set_solc_version
from pathlib import Path
import json
import os

# ---------------- CONFIG ----------------
GANACHE_URL = "http://127.0.0.1:7545"
CONTRACT_PATH = Path(__file__).resolve().parent.parent / "contracts" / "ERC-20" / "MiTokenSeguro.sol"
# Si ya tienes la dirección de un contrato desplegado, puedes ponerla aquí para evitar compilar/desplegar:
USE_EXISTING_CONTRACT = False
CONTRACT_ADDRESS = ""  # p.ej. "0x1234...". Si USE_EXISTING_CONTRACT=True, se usará esta dirección.

# Solidity version
SOLC_VERSION = "0.8.19"
install_solc(SOLC_VERSION)
set_solc_version(SOLC_VERSION)

# ---------------- Inicialización Web3 & Flask ----------------
app = Flask(__name__, template_folder=str(Path(__file__).resolve().parent / "templates"))
w3 = Web3(Web3.HTTPProvider(GANACHE_URL))
if not w3.is_connected():
    raise SystemExit(f"No se pudo conectar a Ganache en {GANACHE_URL}. ¿Está corriendo?")

# Obtener cuentas locales (Ganache suele exponerlas desbloqueadas)
ACCOUNTS = w3.eth.accounts

# ---------------- Compilar / Desplegar / Cargar contrato ----------------
abi = None
contract = None

def compile_and_deploy():
    global abi, contract
    if not CONTRACT_PATH.exists():
        raise FileNotFoundError(f"No se encuentra el contrato en {CONTRACT_PATH}. Ajusta CONTRACT_PATH.")
    with CONTRACT_PATH.open("r", encoding="utf-8") as f:
        source = f.read()

    print("Compilando contrato (puede tardar unos segundos)...")
    compiled = compile_standard(
        {
            "language": "Solidity",
            "sources": {"MiTokenSeguro.sol": {"content": source}},
            "settings": {"outputSelection": {"*": {"*": ["abi", "evm.bytecode"]}}}
        },
        solc_version=SOLC_VERSION,
        base_path=str(CONTRACT_PATH.parents[2])  # intenta resolver imports respecto a la raíz del proyecto
    )

    contract_data = compiled["contracts"]["MiTokenSeguro.sol"]["MiTokenSeguro"]
    abi_local = contract_data["abi"]
    bytecode = contract_data["evm"]["bytecode"]["object"]

    # Guardar ABI localmente (opcional)
    with open("MiTokenSeguro_abi.json", "w", encoding="utf-8") as f:
        json.dump(abi_local, f, indent=2, ensure_ascii=False)

    # Desplegar (desde la primera cuenta)
    print("Desplegando contrato MiTokenSeguro...")
    MiToken = w3.eth.contract(abi=abi_local, bytecode=bytecode)
    tx_hash = MiToken.constructor(w3.to_wei(1000, "ether")).transact({'from': ACCOUNTS[0]})
    tx_receipt = w3.eth.wait_for_transaction_receipt(tx_hash)
    print("Despliegue finalizado en:", tx_receipt.contractAddress)
    abi = abi_local
    contract = w3.eth.contract(address=tx_receipt.contractAddress, abi=abi)

# Si el usuario quiere usar una dirección existente -> la carga
if USE_EXISTING_CONTRACT and CONTRACT_ADDRESS:
    try:
        with open("MiTokenSeguro_abi.json", "r", encoding="utf-8") as f:
            abi = json.load(f)
        contract = w3.eth.contract(address=CONTRACT_ADDRESS, abi=abi)
        print("Usando contrato existente en", CONTRACT_ADDRESS)
    except Exception as e:
        print("No se pudo cargar ABI desde MiTokenSeguro_abi.json:", e)
        print("Se intentará compilar y desplegar.")
        compile_and_deploy()
else:
    # Intentar compilar y desplegar; si falla (p.ej. imports), indicarlo para que uses una dirección ya desplegada
    try:
        compile_and_deploy()
    except Exception as e:
        print("Compilación o despliegue falló:", e)
        if CONTRACT_ADDRESS:
            print("Intentando usar la dirección existente CONTRACT_ADDRESS...")
            try:
                with open("MiTokenSeguro_abi.json", "r", encoding="utf-8") as f:
                    abi = json.load(f)
                contract = w3.eth.contract(address=CONTRACT_ADDRESS, abi=abi)
                print("Usando contrato existente en", CONTRACT_ADDRESS)
            except Exception as e2:
                raise SystemExit("No se pudo compilar ni usar la dirección existente. Corrige imports o pega ABI manualmente.") from e2
        else:
            raise SystemExit("No se pudo compilar el contrato. Si tienes el contrato ya desplegado, marca USE_EXISTING_CONTRACT=True y pon CONTRACT_ADDRESS.")

# ---------------- Helpers ----------------
def to_wei_amount(amount_str):
    # permitimos pasar número entero o decimal en unidades 'humanas' (ej: 1.5 -> 1.5 tokens con 18 decimales)
    try:
        # aceptar comas o puntos
        amount_str = amount_str.replace(',', '.')
        value = float(amount_str)
        return w3.to_wei(value, 'ether')
    except Exception:
        raise ValueError("Formato de cantidad inválido. Usa ej. 10 o 1.5")

def fmt_from_wei(x):
    return float(w3.from_wei(x, 'ether'))

# ---------------- Rutas UI ----------------
@app.route("/")
def index():
    # entregamos la lista de cuentas para que el frontend las muestre
    return render_template("index20.html", accounts=ACCOUNTS)

# ---------------- Endpoints de lectura ----------------
@app.route("/api/name", methods=["GET"])
def api_name():
    try:
        n = contract.functions.name().call()
        return jsonify(success=True, result=n)
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/api/symbol", methods=["GET"])
def api_symbol():
    try:
        s = contract.functions.symbol().call()
        return jsonify(success=True, result=s)
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/api/decimals", methods=["GET"])
def api_decimals():
    try:
        d = contract.functions.decimals().call()
        return jsonify(success=True, result=d)
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/api/totalSupply", methods=["GET"])
def api_total_supply():
    try:
        ts = contract.functions.totalSupply().call()
        return jsonify(success=True, result=str(fmt_from_wei(ts)))
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/api/balanceOf", methods=["POST"])
def api_balance_of():
    try:
        addr = request.json.get("address")
        if not addr:
            return jsonify(success=False, error="Falta 'address' en JSON")
        b = contract.functions.balanceOf(addr).call()
        return jsonify(success=True, result=str(fmt_from_wei(b)))
    except Exception as e:
        return jsonify(success=False, error=str(e))

# ---------------- Endpoints de transacción (todos con try/except) ----------------
@app.route("/tx/transfer", methods=["POST"])
def tx_transfer():
    data = request.json or {}
    try:
        sender = data.get("from")
        to = data.get("to")
        amount = data.get("amount")
        if not all([sender, to, amount]):
            return jsonify(success=False, error="Falta alguno de los campos: from, to, amount")
        amount_wei = to_wei_amount(str(amount))
        tx = contract.functions.transfer(to, amount_wei).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/tx/mint", methods=["POST"])
def tx_mint():
    data = request.json or {}
    try:
        sender = data.get("from")
        to = data.get("to")
        amount = data.get("amount")
        if not all([sender, to, amount]):
            return jsonify(success=False, error="Falta alguno de los campos: from, to, amount")
        amount_wei = to_wei_amount(str(amount))
        tx = contract.functions.mint(to, amount_wei).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/tx/burn", methods=["POST"])
def tx_burn():
    data = request.json or {}
    try:
        sender = data.get("from")
        account = data.get("account")
        amount = data.get("amount")
        if not all([sender, account, amount]):
            return jsonify(success=False, error="Falta alguno de los campos: from, account, amount")
        amount_wei = to_wei_amount(str(amount))
        tx = contract.functions.burn(account, amount_wei).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/tx/transferOwnership", methods=["POST"])
def tx_transfer_ownership():
    data = request.json or {}
    try:
        sender = data.get("from")
        new_owner = data.get("newOwner")
        if not all([sender, new_owner]):
            return jsonify(success=False, error="Falta alguno de los campos: from, newOwner")
        tx = contract.functions.transferOwnership(new_owner).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/tx/renounceOwnership", methods=["POST"])
def tx_renounce_ownership():
    data = request.json or {}
    try:
        sender = data.get("from")
        if not sender:
            return jsonify(success=False, error="Falta campo: from")
        tx = contract.functions.renounceOwnership().transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

# Otros endpoints útiles (approve, allowance, transferFrom)
@app.route("/tx/approve", methods=["POST"])
def tx_approve():
    data = request.json or {}
    try:
        sender = data.get("from")
        spender = data.get("spender")
        amount = data.get("amount")
        if not all([sender, spender, amount]):
            return jsonify(success=False, error="Falta alguno de los campos: from, spender, amount")
        amount_wei = to_wei_amount(str(amount))
        tx = contract.functions.approve(spender, amount_wei).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))
    
@app.route("/tx/increaseAllowance", methods=["POST"])
def tx_increase_allowance():
    data = request.json or {}
    try:
        sender = data.get("from")
        spender = data.get("spender")
        amount = data.get("amount")
        if not all([sender, spender, amount]):
            return jsonify(success=False, error="Falta alguno de los campos: from, spender, amount")
        amount_wei = to_wei_amount(str(amount))
        tx = contract.functions.increaseAllowance(spender, amount_wei).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))


@app.route("/tx/decreaseAllowance", methods=["POST"])
def tx_decrease_allowance():
    data = request.json or {}
    try:
        sender = data.get("from")
        spender = data.get("spender")
        amount = data.get("amount")
        if not all([sender, spender, amount]):
            return jsonify(success=False, error="Falta alguno de los campos: from, spender, amount")
        amount_wei = to_wei_amount(str(amount))
        tx = contract.functions.decreaseAllowance(spender, amount_wei).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

    
@app.route("/api/allowance", methods=["POST"])
def api_allowance():
    try:
        owner = request.json.get("owner")
        spender = request.json.get("spender")
        if not owner or not spender:
            return jsonify(success=False, error="Falta 'owner' o 'spender' en JSON")
        a = contract.functions.allowance(owner, spender).call()
        return jsonify(success=True, result=str(fmt_from_wei(a)))
    except Exception as e:
        return jsonify(success=False, error=str(e))

@app.route("/tx/transferFrom", methods=["POST"])
def tx_transfer_from():
    data = request.json or {}
    try:
        sender = data.get("from")
        owner = data.get("owner")
        to = data.get("to")
        amount = data.get("amount")
        if not all([sender, owner, to, amount]):
            return jsonify(success=False, error="Falta alguno de los campos: from, owner, to, amount")
        amount_wei = to_wei_amount(str(amount))
        tx = contract.functions.transferFrom(owner, to, amount_wei).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

# ---------------- Run ----------------
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
