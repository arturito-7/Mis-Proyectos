# app721.py
from flask import Flask, render_template, request, jsonify
from web3 import Web3
from solcx import compile_standard, install_solc, set_solc_version
from pathlib import Path
import json
import os

# ---------------- CONFIG ----------------
GANACHE_URL = "http://127.0.0.1:7545"
# Actualiza la ruta a tu nuevo contrato
CONTRACT_PATH = Path(__file__).resolve().parent.parent / "contracts" / "ERC-721" / "contratoConRoles.sol"
CONTRACT_NAME = "contratoTop"
ABI_FILE_NAME = "contratoConRoles_abi.json"

# Si ya tienes la dirección de un contrato desplegado, puedes ponerla aquí:
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
            "sources": {CONTRACT_PATH.name: {"content": source}},
            "settings": {"outputSelection": {"*": {"*": ["abi", "evm.bytecode"]}}}
        },
        solc_version=SOLC_VERSION,
        base_path=str(CONTRACT_PATH.parents[2]) 
    )

    # Asegúrate de que el nombre del contrato "contratoTop" es correcto
    if CONTRACT_NAME not in compiled["contracts"][CONTRACT_PATH.name]:
        raise KeyError(f"No se encontró el contrato '{CONTRACT_NAME}' en los artefactos de compilación.")

    contract_data = compiled["contracts"][CONTRACT_PATH.name][CONTRACT_NAME]
    abi_local = contract_data["abi"]
    bytecode = contract_data["evm"]["bytecode"]["object"]

    # Guardar ABI localmente
    with open(ABI_FILE_NAME, "w", encoding="utf-8") as f:
        json.dump(abi_local, f, indent=2, ensure_ascii=False)

    # Desplegar (desde la primera cuenta)
    print(f"Desplegando contrato {CONTRACT_NAME}...")
    Contract_ = w3.eth.contract(abi=abi_local, bytecode=bytecode)
    
    # ¡Importante! El constructor de 'contratoConRoles.sol' pide _name y _symbol
    tx_hash = Contract_.constructor("Mi NFT con Roles", "MNR").transact({'from': ACCOUNTS[0]})
    
    tx_receipt = w3.eth.wait_for_transaction_receipt(tx_hash)
    print("Despliegue finalizado en:", tx_receipt.contractAddress)
    abi = abi_local
    contract = w3.eth.contract(address=tx_receipt.contractAddress, abi=abi)

# Cargar contrato o desplegarlo
if USE_EXISTING_CONTRACT and CONTRACT_ADDRESS:
    try:
        with open(ABI_FILE_NAME, "r", encoding="utf-8") as f:
            abi = json.load(f)
        contract = w3.eth.contract(address=CONTRACT_ADDRESS, abi=abi)
        print("Usando contrato existente en", CONTRACT_ADDRESS)
    except Exception as e:
        print(f"No se pudo cargar ABI desde {ABI_FILE_NAME}:", e)
        print("Se intentará compilar y desplegar.")
        compile_and_deploy()
else:
    try:
        compile_and_deploy()
    except Exception as e:
        print("Compilación o despliegue falló:", e)
        if CONTRACT_ADDRESS:
            print("Intentando usar la dirección existente CONTRACT_ADDRESS...")
            try:
                with open(ABI_FILE_NAME, "r", encoding="utf-8") as f:
                    abi = json.load(f)
                contract = w3.eth.contract(address=CONTRACT_ADDRESS, abi=abi)
                print("Usando contrato existente en", CONTRACT_ADDRESS)
            except Exception as e2:
                raise SystemExit("No se pudo compilar ni usar la dirección existente.") from e2
        else:
            raise SystemExit("No se pudo compilar el contrato. Si tienes el contrato ya desplegado, marca USE_EXISTING_CONTRACT=True y pon CONTRACT_ADDRESS.")

# ---------------- Rutas UI ----------------
@app.route("/")
def index():
    # Renderizamos el NUEVO template
    return render_template("index721.html", accounts=ACCOUNTS)

# ---------------- Endpoints de lectura (adaptados a ERC-721) ----------------
@app.route("/api/name", methods=["GET"])
def api_name():
    try:
        n = contract.functions.name().call()
        return jsonify(success=True, result=n)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/symbol", methods=["GET"])
def api_symbol():
    try:
        s = contract.functions.symbol().call()
        return jsonify(success=True, result=s)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/totalSupply", methods=["GET"])
def api_total_supply():
    try:
        # En ERC-721, totalSupply es un recuento, no necesita 'from_wei'
        ts = contract.functions.totalSupply().call()
        return jsonify(success=True, result=str(ts))
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/balanceOf", methods=["POST"])
def api_balance_of():
    try:
        addr = request.json.get("address")
        if not addr: return jsonify(success=False, error="Falta 'address' en JSON")
        # En ERC-721, balanceOf es un recuento
        b = contract.functions.balanceOf(addr).call()
        return jsonify(success=True, result=str(b))
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/ownerOf", methods=["POST"])
def api_owner_of():
    try:
        token_id = request.json.get("tokenId")
        if token_id is None: return jsonify(success=False, error="Falta 'tokenId' en JSON")
        owner = contract.functions.ownerOf(int(token_id)).call()
        return jsonify(success=True, result=owner)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/tokenURI", methods=["POST"])
def api_token_uri():
    try:
        token_id = request.json.get("tokenId")
        if token_id is None: return jsonify(success=False, error="Falta 'tokenId' en JSON")
        uri = contract.functions.tokenURI(int(token_id)).call()
        return jsonify(success=True, result=uri)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/getApproved", methods=["POST"])
def api_get_approved():
    try:
        token_id = request.json.get("tokenId")
        if token_id is None: return jsonify(success=False, error="Falta 'tokenId' en JSON")
        approved = contract.functions.getApproved(int(token_id)).call()
        return jsonify(success=True, result=approved)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/isApprovedForAll", methods=["POST"])
def api_is_approved_for_all():
    try:
        owner = request.json.get("owner")
        operator = request.json.get("operator")
        if not owner or not operator: return jsonify(success=False, error="Falta 'owner' o 'operator'")
        is_approved = contract.functions.isApprovedForAll(owner, operator).call()
        return jsonify(success=True, result=is_approved)
    except Exception as e: return jsonify(success=False, error=str(e))

# --- Lectura de Roles y Estado ---
@app.route("/api/owner", methods=["GET"])
def api_owner():
    try:
        o = contract.functions.owner().call()
        return jsonify(success=True, result=o)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/paused", methods=["GET"])
def api_paused():
    try:
        p = contract.functions.paused().call()
        return jsonify(success=True, result=p)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/isMinter", methods=["POST"])
def api_is_minter():
    try:
        addr = request.json.get("address")
        if not addr: return jsonify(success=False, error="Falta 'address'")
        is_m = contract.functions.minters(addr).call()
        return jsonify(success=True, result=is_m)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/isBurner", methods=["POST"])
def api_is_burner():
    try:
        addr = request.json.get("address")
        if not addr: return jsonify(success=False, error="Falta 'address'")
        is_b = contract.functions.burners(addr).call()
        return jsonify(success=True, result=is_b)
    except Exception as e: return jsonify(success=False, error=str(e))

# ---------------- Endpoints de transacción (adaptados a ERC-721) ----------------
@app.route("/tx/transferFrom", methods=["POST"])
def tx_transfer_from():
    data = request.json or {}
    try:
        sender = data.get("from") # El msg.sender
        owner = data.get("owner")  # El 'from' de la función
        to = data.get("to")
        token_id = data.get("tokenId")
        if not all([sender, owner, to, token_id is not None]):
            return jsonify(success=False, error="Falta 'from' (sender), 'owner', 'to' o 'tokenId'")
        
        tx = contract.functions.transferFrom(owner, to, int(token_id)).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/approve", methods=["POST"])
def tx_approve():
    data = request.json or {}
    try:
        sender = data.get("from")
        to = data.get("to")
        token_id = data.get("tokenId")
        if not all([sender, to, token_id is not None]):
            return jsonify(success=False, error="Falta 'from', 'to' o 'tokenId'")
        
        tx = contract.functions.approve(to, int(token_id)).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/setApprovalForAll", methods=["POST"])
def tx_set_approval_for_all():
    data = request.json or {}
    try:
        sender = data.get("from")
        operator = data.get("operator")
        approved = data.get("approved") # Debe ser un booleano (true/false)
        if not all([sender, operator, approved is not None]):
            return jsonify(success=False, error="Falta 'from', 'operator' o 'approved'")
        
        tx = contract.functions.setApprovalForAll(operator, bool(approved)).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

# --- Transacciones de Roles ---
@app.route("/tx/mint", methods=["POST"])
def tx_mint():
    data = request.json or {}
    try:
        sender = data.get("from")
        to = data.get("to")
        token_id = data.get("tokenId")
        uri = data.get("uri")
        if not all([sender, to, token_id is not None, uri is not None]):
            return jsonify(success=False, error="Falta 'from', 'to', 'tokenId' o 'uri'")
        
        tx = contract.functions.mint(to, int(token_id), uri).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/burn", methods=["POST"])
def tx_burn():
    data = request.json or {}
    try:
        sender = data.get("from")
        token_id = data.get("tokenId")
        if not all([sender, token_id is not None]):
            return jsonify(success=False, error="Falta 'from' o 'tokenId'")
        
        tx = contract.functions.burn(int(token_id)).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

# --- Transacciones de Owner ---
@app.route("/tx/transferOwnership", methods=["POST"])
def tx_transfer_ownership():
    data = request.json or {}
    try:
        sender = data.get("from")
        new_owner = data.get("newOwner")
        if not all([sender, new_owner]):
            return jsonify(success=False, error="Falta 'from' o 'newOwner'")
        
        tx = contract.functions.transferOwnership(new_owner).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/pause", methods=["POST"])
def tx_pause():
    data = request.json or {}
    try:
        sender = data.get("from")
        if not sender: return jsonify(success=False, error="Falta 'from'")
        tx = contract.functions.pause().transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/unpause", methods=["POST"])
def tx_unpause():
    data = request.json or {}
    try:
        sender = data.get("from")
        if not sender: return jsonify(success=False, error="Falta 'from'")
        tx = contract.functions.unpause().transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/addMinter", methods=["POST"])
def tx_add_minter():
    data = request.json or {}
    try:
        sender = data.get("from")
        account = data.get("account")
        if not all([sender, account]): return jsonify(success=False, error="Falta 'from' o 'account'")
        tx = contract.functions.addMinter(account).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/removeMinter", methods=["POST"])
def tx_remove_minter():
    data = request.json or {}
    try:
        sender = data.get("from")
        account = data.get("account")
        if not all([sender, account]): return jsonify(success=False, error="Falta 'from' o 'account'")
        tx = contract.functions.removeMinter(account).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/addBurner", methods=["POST"])
def tx_add_burner():
    data = request.json or {}
    try:
        sender = data.get("from")
        account = data.get("account")
        if not all([sender, account]): return jsonify(success=False, error="Falta 'from' o 'account'")
        tx = contract.functions.addBurner(account).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/removeBurner", methods=["POST"])
def tx_remove_burner():
    data = request.json or {}
    try:
        sender = data.get("from")
        account = data.get("account")
        if not all([sender, account]): return jsonify(success=False, error="Falta 'from' o 'account'")
        tx = contract.functions.removeBurner(account).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/tx/setBaseURI", methods=["POST"])
def tx_set_base_uri():
    data = request.json or {}
    try:
        sender = data.get("from")
        uri = data.get("uri")
        if not all([sender, uri is not None]): # uri puede ser ""
            return jsonify(success=False, error="Falta 'from' o 'uri'")
        
        tx = contract.functions._setBaseURI(uri).transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e: return jsonify(success=False, error=str(e))

# ---------------- Run ----------------
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)