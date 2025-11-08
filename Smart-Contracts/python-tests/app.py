# app.py
from flask import Flask, render_template, request, jsonify
from web3 import Web3
from solcx import compile_standard, install_solc, set_solc_version
from pathlib import Path
import json
import os

# ---------------- CONFIG ----------------
GANACHE_URL = "http://127.0.0.1:7545"
# Ruta al nuevo contrato unificado
CONTRACT_PATH = Path(__file__).resolve().parent.parent / "contracts" / "contratoMixto.sol"
CONTRACT_NAME = "ContratoTFG"
ABI_FILE_NAME = "ContratoTFG_abi.json"

# Si ya tienes la dirección de un contrato desplegado:
USE_EXISTING_CONTRACT = False
CONTRACT_ADDRESS = "" 

# Solidity version
SOLC_VERSION = "0.8.19"
try:
    install_solc(SOLC_VERSION)
    set_solc_version(SOLC_VERSION)
except Exception as e:
    print(f"Error instalando/configurando solc {SOLC_VERSION}: {e}")
    print("Asegúrate de tener permisos o hazlo manualmente.")

# ---------------- Inicialización Web3 & Flask ----------------
app = Flask(__name__, template_folder=str(Path(__file__).resolve().parent / "templates"))
w3 = Web3(Web3.HTTPProvider(GANACHE_URL))
if not w3.is_connected():
    raise SystemExit(f"No se pudo conectar a Ganache en {GANACHE_URL}.")

ACCOUNTS = w3.eth.accounts

# ---------------- Compilar / Desplegar / Cargar contrato ----------------
abi = None
contract = None

def compile_and_deploy():
    global abi, contract
    if not CONTRACT_PATH.exists():
        raise FileNotFoundError(f"No se encuentra el contrato en {CONTRACT_PATH}.")
    
    with CONTRACT_PATH.open("r", encoding="utf-8") as f:
        source = f.read()

    print("Compilando contrato unificado (puede tardar)...")
    
    # Ruta base para resolver imports de OpenZeppelin
    # Asumimos que 'openZeppelin' está en 'repo/openZeppelin'
    base_import_path = str(CONTRACT_PATH.parents[1])

    compiled = compile_standard(
        {
            "language": "Solidity",
            "sources": {CONTRACT_PATH.name: {"content": source}},
            "settings": {
                "outputSelection": {"*": {"*": ["abi", "evm.bytecode"]}},
                # Añadimos remappings para OpenZeppelin
                "remappings": [f"openZeppelin/={base_import_path}/openZeppelin/"]
            }
        },
        solc_version=SOLC_VERSION,
        allow_paths=[base_import_path] # Permitir imports desde la carpeta del repo
    )

    if "errors" in compiled:
        print("Errores de compilación:")
        for error in compiled["errors"]:
            if error["severity"] == "error":
                print(error["formattedMessage"])
        raise SystemExit("Fallo en la compilación. Revisa los imports de OpenZeppelin.")

    if CONTRACT_NAME not in compiled["contracts"][CONTRACT_PATH.name]:
        raise KeyError(f"No se encontró el contrato '{CONTRACT_NAME}'.")

    contract_data = compiled["contracts"][CONTRACT_PATH.name][CONTRACT_NAME]
    abi_local = contract_data["abi"]
    bytecode = contract_data["evm"]["bytecode"]["object"]

    with open(ABI_FILE_NAME, "w", encoding="utf-8") as f:
        json.dump(abi_local, f, indent=2, ensure_ascii=False)

    print(f"Desplegando contrato {CONTRACT_NAME}...")
    Contract_ = w3.eth.contract(abi=abi_local, bytecode=bytecode)
    
    # Constructor del ContratoTFG:
    # constructor(string memory _erc20Name, string memory _erc20Symbol, uint256 _initialERC20Supply, string memory _nftName, string memory _nftSymbol)
    tx_hash = Contract_.constructor(
        "MiToken (TFG)", 
        "MTFG", 
        w3.to_wei(1000000, "ether"), # Supply inicial de ERC-20
        "Mi NFT (TFG)", 
        "NFTFG"
    ).transact({'from': ACCOUNTS[0]})
    
    tx_receipt = w3.eth.wait_for_transaction_receipt(tx_hash)
    print("Despliegue finalizado en:", tx_receipt.contractAddress)
    abi = abi_local
    contract = w3.eth.contract(address=tx_receipt.contractAddress, abi=abi)

# Cargar contrato o desplegarlo
if USE_EXISTING_CONTRACT and CONTRACT_ADDRESS:
    try:
        with open(ABI_FILE_NAME, "r", encoding="utf-8") as f: abi = json.load(f)
        contract = w3.eth.contract(address=CONTRACT_ADDRESS, abi=abi)
        print("Usando contrato existente en", CONTRACT_ADDRESS)
    except Exception as e:
        print(f"No se pudo cargar ABI: {e}. Se intentará compilar y desplegar.")
        compile_and_deploy()
else:
    try:
        compile_and_deploy()
    except Exception as e:
        print(f"Compilación o despliegue falló: {e}")
        raise SystemExit("No se pudo compilar el contrato.")

# ---------------- Helpers (para ERC-20) ----------------
def to_wei_amount(amount_str):
    try:
        amount_str = str(amount_str).replace(',', '.')
        value = float(amount_str)
        return w3.to_wei(value, 'ether')
    except Exception:
        raise ValueError("Formato de cantidad inválido.")

def fmt_from_wei(x):
    return float(w3.from_wei(x, 'ether'))

# ---------------- Ruta UI ----------------
@app.route("/")
def index():
    return render_template("index.html", accounts=ACCOUNTS)

# ======================================================
# --- ENDPOINTS DE LECTURA (API) ---
# ======================================================

# --- ERC-20 ---
@app.route("/api/erc20/name", methods=["GET"])
def api_erc20_name():
    try:
        n = contract.functions.name().call()
        return jsonify(success=True, result=n)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/erc20/symbol", methods=["GET"])
def api_erc20_symbol():
    try:
        s = contract.functions.symbol().call()
        return jsonify(success=True, result=s)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/erc20/decimals", methods=["GET"])
def api_erc20_decimals():
    try:
        d = contract.functions.decimals().call()
        return jsonify(success=True, result=d)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/erc20/totalSupply", methods=["GET"])
def api_erc20_total_supply():
    try:
        ts = contract.functions.totalSupply().call()
        return jsonify(success=True, result=str(fmt_from_wei(ts)))
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/erc20/balanceOf", methods=["POST"])
def api_erc20_balance_of():
    try:
        addr = request.json.get("address")
        if not addr: return jsonify(success=False, error="Falta 'address'")
        b = contract.functions.balanceOf(addr).call()
        return jsonify(success=True, result=str(fmt_from_wei(b)))
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/erc20/allowance", methods=["POST"])
def api_erc20_allowance():
    try:
        owner = request.json.get("owner")
        spender = request.json.get("spender")
        if not owner or not spender: return jsonify(success=False, error="Falta 'owner' o 'spender'")
        a = contract.functions.allowance(owner, spender).call()
        return jsonify(success=True, result=str(fmt_from_wei(a)))
    except Exception as e: return jsonify(success=False, error=str(e))

# --- ERC-721 (NFT) ---
@app.route("/api/nft/name", methods=["GET"])
def api_nft_name():
    try:
        n = contract.functions.nftName().call() # Función renombrada
        return jsonify(success=True, result=n)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/nft/symbol", methods=["GET"])
def api_nft_symbol():
    try:
        s = contract.functions.nftSymbol().call() # Función renombrada
        return jsonify(success=True, result=s)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/nft/totalSupply", methods=["GET"])
def api_nft_total_supply():
    try:
        ts = contract.functions.totalSupplyNFT().call() # Función renombrada
        return jsonify(success=True, result=str(ts))
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/nft/balanceOf", methods=["POST"])
def api_nft_balance_of():
    try:
        addr = request.json.get("address")
        if not addr: return jsonify(success=False, error="Falta 'address'")
        b = contract.functions.balanceOfNFT(addr).call() # Función renombrada
        return jsonify(success=True, result=str(b))
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/nft/ownerOf", methods=["POST"])
def api_nft_owner_of():
    try:
        token_id = request.json.get("tokenId")
        if token_id is None: return jsonify(success=False, error="Falta 'tokenId'")
        owner = contract.functions.ownerOf(int(token_id)).call() # Sin colisión
        return jsonify(success=True, result=owner)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/nft/tokenURI", methods=["POST"])
def api_nft_token_uri():
    try:
        token_id = request.json.get("tokenId")
        if token_id is None: return jsonify(success=False, error="Falta 'tokenId'")
        uri = contract.functions.tokenURI(int(token_id)).call() # Sin colisión
        return jsonify(success=True, result=uri)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/nft/getApproved", methods=["POST"])
def api_nft_get_approved():
    try:
        token_id = request.json.get("tokenId")
        if token_id is None: return jsonify(success=False, error="Falta 'tokenId'")
        approved = contract.functions.getApprovedNFT(int(token_id)).call() # Función renombrada
        return jsonify(success=True, result=approved)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/nft/isApprovedForAll", methods=["POST"])
def api_nft_is_approved_for_all():
    try:
        owner = request.json.get("owner")
        operator = request.json.get("operator")
        if not owner or not operator: return jsonify(success=False, error="Falta 'owner' o 'operator'")
        is_approved = contract.functions.isApprovedForAllNFT(owner, operator).call() # Función renombrada
        return jsonify(success=True, result=is_approved)
    except Exception as e: return jsonify(success=False, error=str(e))

# --- Admin / Roles ---
@app.route("/api/admin/owner", methods=["GET"])
def api_admin_owner():
    try:
        o = contract.functions.owner().call()
        return jsonify(success=True, result=o)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/admin/paused", methods=["GET"])
def api_admin_paused():
    try:
        p = contract.functions.paused().call()
        return jsonify(success=True, result=p)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/admin/isMinter", methods=["POST"])
def api_admin_is_minter():
    try:
        addr = request.json.get("address")
        if not addr: return jsonify(success=False, error="Falta 'address'")
        is_m = contract.functions.minters(addr).call()
        return jsonify(success=True, result=is_m)
    except Exception as e: return jsonify(success=False, error=str(e))

@app.route("/api/admin/isBurner", methods=["POST"])
def api_admin_is_burner():
    try:
        addr = request.json.get("address")
        if not addr: return jsonify(success=False, error="Falta 'address'")
        is_b = contract.functions.burners(addr).call()
        return jsonify(success=True, result=is_b)
    except Exception as e: return jsonify(success=False, error=str(e))

# ======================================================
# --- ENDPOINTS DE TRANSACCIÓN (TX) ---
# ======================================================

def handle_tx(function_call, sender):
    try:
        tx = function_call.transact({'from': sender})
        rcpt = w3.eth.wait_for_transaction_receipt(tx)
        return jsonify(success=True, txHash=rcpt.transactionHash.hex())
    except Exception as e:
        return jsonify(success=False, error=str(e))

# --- ERC-20 ---
@app.route("/tx/erc20/transfer", methods=["POST"])
def tx_erc20_transfer():
    data = request.json or {}
    sender = data.get("from")
    to = data.get("to")
    amount = data.get("amount")
    if not all([sender, to, amount]): return jsonify(success=False, error="Faltan 'from', 'to' o 'amount'")
    amount_wei = to_wei_amount(amount)
    call = contract.functions.transfer(to, amount_wei) # Estándar OZ
    return handle_tx(call, sender)

@app.route("/tx/erc20/approve", methods=["POST"])
def tx_erc20_approve():
    data = request.json or {}
    sender = data.get("from")
    spender = data.get("spender")
    amount = data.get("amount")
    if not all([sender, spender, amount]): return jsonify(success=False, error="Faltan 'from', 'spender' o 'amount'")
    amount_wei = to_wei_amount(amount)
    call = contract.functions.approve(spender, amount_wei) # Estándar OZ
    return handle_tx(call, sender)

@app.route("/tx/erc20/transferFrom", methods=["POST"])
def tx_erc20_transfer_from():
    data = request.json or {}
    sender = data.get("from") # msg.sender
    owner = data.get("owner") # 'from' en la función
    to = data.get("to")
    amount = data.get("amount")
    if not all([sender, owner, to, amount]): return jsonify(success=False, error="Faltan 'from', 'owner', 'to' o 'amount'")
    amount_wei = to_wei_amount(amount)
    call = contract.functions.transferFrom(owner, to, amount_wei) # Estándar OZ
    return handle_tx(call, sender)

@app.route("/tx/erc20/increaseAllowance", methods=["POST"])
def tx_erc20_increase_allowance():
    data = request.json or {}
    sender = data.get("from")
    spender = data.get("spender")
    amount = data.get("amount")
    if not all([sender, spender, amount]): return jsonify(success=False, error="Faltan 'from', 'spender' o 'amount'")
    amount_wei = to_wei_amount(amount)
    call = contract.functions.increaseAllowance(spender, amount_wei) # Estándar OZ
    return handle_tx(call, sender)

@app.route("/tx/erc20/decreaseAllowance", methods=["POST"])
def tx_erc20_decrease_allowance():
    data = request.json or {}
    sender = data.get("from")
    spender = data.get("spender")
    amount = data.get("amount")
    if not all([sender, spender, amount]): return jsonify(success=False, error="Faltan 'from', 'spender' o 'amount'")
    amount_wei = to_wei_amount(amount)
    call = contract.functions.decreaseAllowance(spender, amount_wei) # Estándar OZ
    return handle_tx(call, sender)

@app.route("/tx/erc20/mint", methods=["POST"])
def tx_erc20_mint():
    data = request.json or {}
    sender = data.get("from")
    to = data.get("to")
    amount = data.get("amount")
    if not all([sender, to, amount]): return jsonify(success=False, error="Faltan 'from', 'to' o 'amount'")
    amount_wei = to_wei_amount(amount)
    call = contract.functions.mintERC20(to, amount_wei) # Función personalizada
    return handle_tx(call, sender)

@app.route("/tx/erc20/burn", methods=["POST"])
def tx_erc20_burn():
    data = request.json or {}
    sender = data.get("from")
    account = data.get("account")
    amount = data.get("amount")
    if not all([sender, account, amount]): return jsonify(success=False, error="Faltan 'from', 'account' o 'amount'")
    amount_wei = to_wei_amount(amount)
    call = contract.functions.burnERC20(account, amount_wei) # Función personalizada
    return handle_tx(call, sender)

# --- ERC-721 (NFT) ---
@app.route("/tx/nft/mint", methods=["POST"])
def tx_nft_mint():
    data = request.json or {}
    sender = data.get("from")
    to = data.get("to")
    token_id = data.get("tokenId")
    uri = data.get("uri")
    if not all([sender, to, token_id is not None, uri is not None]):
        return jsonify(success=False, error="Faltan 'from', 'to', 'tokenId' o 'uri'")
    call = contract.functions.mintNFT(to, int(token_id), uri) # Función renombrada
    return handle_tx(call, sender)

@app.route("/tx/nft/burn", methods=["POST"])
def tx_nft_burn():
    data = request.json or {}
    sender = data.get("from")
    token_id = data.get("tokenId")
    if not all([sender, token_id is not None]):
        return jsonify(success=False, error="Faltan 'from' o 'tokenId'")
    call = contract.functions.burnNFT(int(token_id)) # Función renombrada
    return handle_tx(call, sender)

@app.route("/tx/nft/approve", methods=["POST"])
def tx_nft_approve():
    data = request.json or {}
    sender = data.get("from")
    to = data.get("to")
    token_id = data.get("tokenId")
    if not all([sender, to, token_id is not None]):
        return jsonify(success=False, error="Faltan 'from', 'to' o 'tokenId'")
    call = contract.functions.approveNFT(to, int(token_id)) # Función renombrada
    return handle_tx(call, sender)

@app.route("/tx/nft/transferFrom", methods=["POST"])
def tx_nft_transfer_from():
    data = request.json or {}
    sender = data.get("from") # msg.sender
    owner = data.get("owner") # 'from' en la función
    to = data.get("to")
    token_id = data.get("tokenId")
    if not all([sender, owner, to, token_id is not None]):
        return jsonify(success=False, error="Faltan 'from', 'owner', 'to' o 'tokenId'")
    call = contract.functions.transferFromNFT(owner, to, int(token_id)) # Función renombrada
    return handle_tx(call, sender)

@app.route("/tx/nft/setApprovalForAll", methods=["POST"])
def tx_nft_set_approval_for_all():
    data = request.json or {}
    sender = data.get("from")
    operator = data.get("operator")
    approved = data.get("approved")
    if not all([sender, operator, approved is not None]):
        return jsonify(success=False, error="Faltan 'from', 'operator' o 'approved'")
    call = contract.functions.setApprovalForAllNFT(operator, bool(approved)) # Función renombrada
    return handle_tx(call, sender)

# --- Admin / Roles ---
@app.route("/tx/admin/transferOwnership", methods=["POST"])
def tx_admin_transfer_ownership():
    data = request.json or {}
    sender = data.get("from")
    new_owner = data.get("newOwner")
    if not all([sender, new_owner]): return jsonify(success=False, error="Faltan 'from' o 'newOwner'")
    call = contract.functions.transferOwnership(new_owner)
    return handle_tx(call, sender)

@app.route("/tx/admin/pause", methods=["POST"])
def tx_admin_pause():
    sender = (request.json or {}).get("from")
    if not sender: return jsonify(success=False, error="Falta 'from'")
    call = contract.functions.pause()
    return handle_tx(call, sender)

@app.route("/tx/admin/unpause", methods=["POST"])
def tx_admin_unpause():
    sender = (request.json or {}).get("from")
    if not sender: return jsonify(success=False, error="Falta 'from'")
    call = contract.functions.unpause()
    return handle_tx(call, sender)

@app.route("/tx/admin/addMinter", methods=["POST"])
def tx_admin_add_minter():
    data = request.json or {}
    sender, account = data.get("from"), data.get("account")
    if not all([sender, account]): return jsonify(success=False, error="Faltan 'from' o 'account'")
    call = contract.functions.addMinter(account)
    return handle_tx(call, sender)

@app.route("/tx/admin/removeMinter", methods=["POST"])
def tx_admin_remove_minter():
    data = request.json or {}
    sender, account = data.get("from"), data.get("account")
    if not all([sender, account]): return jsonify(success=False, error="Faltan 'from' o 'account'")
    call = contract.functions.removeMinter(account)
    return handle_tx(call, sender)

@app.route("/tx/admin/addBurner", methods=["POST"])
def tx_admin_add_burner():
    data = request.json or {}
    sender, account = data.get("from"), data.get("account")
    if not all([sender, account]): return jsonify(success=False, error="Faltan 'from' o 'account'")
    call = contract.functions.addBurner(account)
    return handle_tx(call, sender)

@app.route("/tx/admin/removeBurner", methods=["POST"])
def tx_admin_remove_burner():
    data = request.json or {}
    sender, account = data.get("from"), data.get("account")
    if not all([sender, account]): return jsonify(success=False, error="Faltan 'from' o 'account'")
    call = contract.functions.removeBurner(account)
    return handle_tx(call, sender)

@app.route("/tx/admin/setBaseURI", methods=["POST"])
def tx_admin_set_base_uri():
    data = request.json or {}
    sender, uri = data.get("from"), data.get("uri")
    if not all([sender, uri is not None]): return jsonify(success=False, error="Faltan 'from' o 'uri'")
    call = contract.functions.setBaseURI_NFT(uri) # Función renombrada
    return handle_tx(call, sender)

# ---------------- Run ----------------
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
