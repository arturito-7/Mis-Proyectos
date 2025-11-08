// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

// --- IMPORTS BASE (ERC20 Y ROLES) ---
import "openZeppelin/contracts/token/ERC20/ERC20.sol";
import "openZeppelin/contracts/access/Ownable.sol";
import "openZeppelin/contracts/security/Pausable.sol";

// --- INTERFACE (Para safeTransfer de NFT) ---
interface IERC721Receiver {
    function onERC721Received(
        address operator,
        address from,
        uint256 tokenId,
        bytes calldata data
    ) external returns (bytes4);
}

/**
 * @title ContratoTFG
 * @dev Contrato híbrido que maneja un token ERC-20 y un token ERC-721
 * bajo un mismo conjunto de roles (Owner, Minter, Burner, Pausable).
 */
contract ContratoTFG is ERC20, Ownable, Pausable {

    // =========================================================================
    // --- ESTADO Y ROLES COMUNES ---
    // =========================================================================
    
    // El 'owner' y 'onlyOwner' vienen de Ownable.
    // El 'paused' y 'whenNotPaused' vienen de Pausable.

    mapping(address => bool) public minters;
    mapping(address => bool) public burners;

    event MinterAdded(address indexed account);
    event MinterRemoved(address indexed account);
    event BurnerAdded(address indexed account);
    event BurnerRemoved(address indexed account);

    modifier onlyMinter() {
        require(minters[msg.sender], "No tienes permisos de minter");
        _;
    }

    modifier onlyBurner() {
        require(burners[msg.sender], "No tienes permisos de burner");
        _;
    }

    // =========================================================================
    // --- LÓGICA ERC-721 (NFT) ---
    // --- Renombrada para evitar colisiones con ERC-20 ---
    // =========================================================================

    // --- Variables de estado NFT ---
    string public nftName;
    string public nftSymbol;

    // Mapping de Token ID a su dueño
    mapping(uint256 => address) private _nftOwners;

    // Mapping de dueño a su cantidad de NFTs
    mapping(address => uint256) private _nftBalances;

    // Mapping de Token ID a dirección aprobada
    mapping(uint256 => address) private _nftTokenApprovals; // <-- Aprobación para NFT

    // Mapping de dueño a operador (aprobado para todo)
    mapping(address => mapping(address => bool)) private _nftOperatorApprovals; // <-- Aprobación para todo NFT

    // URIs únicos
    mapping(string => bool) private _usedURIs;
    mapping(uint256 => string) private _nftTokenURIs;
    string private _nftBaseURI = "";

    // Enumeración (NFTs)
    uint256[] private _allNFTTokens;
    mapping(uint256 => uint256) private _allNFTTokensIndex;
    mapping(address => uint256[]) private _ownedNFTTokens;
    mapping(uint256 => uint256) private _ownedNFTTokensIndex;

    // --- Eventos NFT (Nombre estándar para compatibilidad de explorers) ---
    // ¡¡¡CORRECCIÓN!!!: Eliminamos las definiciones duplicadas de 'Transfer' y 'Approval'.
    // Ya están heredadas de ERC20.sol
    // event Transfer(address indexed from, address indexed to, uint256 indexed tokenId); <-- ELIMINADO
    // event Approval(address indexed owner, address indexed approved, uint256 indexed tokenId); <-- ELIMINADO
    event ApprovalForAll(address indexed owner, address indexed operator, bool approved);

    // =========================================================================
    // --- CONSTRUCTOR ---
    // =========================================================================

    constructor(
        string memory _erc20Name,
        string memory _erc20Symbol,
        uint256 _initialERC20Supply,
        string memory _nftName,
        string memory _nftSymbol
    ) 
        ERC20(_erc20Name, _erc20Symbol)
        Ownable() // ¡¡¡CORRECCIÓN!!!: Constructor vacío para OZ 4.x
    {
        // Mint inicial de ERC-20 para el deployer
        if (_initialERC20Supply > 0) {
            _mint(msg.sender, _initialERC20Supply);
        }
        
        // Asignar metadata del NFT
        nftName = _nftName;
        nftSymbol = _nftSymbol;
    }

    // =========================================================================
    // --- FUNCIONES DE ROLES Y PAUSA (ADMIN) ---
    // =========================================================================
    
    // 'transferOwnership(newOwner)' ya está incluido en Ownable.

    function pause() public onlyOwner {
        _pause(); // Función interna de Pausable
    }

    function unpause() public onlyOwner {
        _unpause(); // Función interna de Pausable
    }

    function addMinter(address account) public onlyOwner {
        minters[account] = true;
        emit MinterAdded(account);
    }

    function removeMinter(address account) public onlyOwner {
        minters[account] = false;
        emit MinterRemoved(account);
    }

    function addBurner(address account) public onlyOwner {
        burners[account] = true;
        emit BurnerAdded(account);
    }

    function removeBurner(address account) public onlyOwner {
        burners[account] = false;
        emit BurnerRemoved(account);
    }

    // =========================================================================
    // --- FUNCIONES ERC-20 (FUNGIBLE) ---
    // =========================================================================
    
    // (Hereda 'approve', 'transfer', 'allowance', etc. de ERC20.sol)
    // (Estas funciones usan el mapping '_allowances' de ERC20)

    /**
     * @dev Hook de Pausable: se ejecuta antes de CUALQUIER transferencia ERC-20.
     */
    function _beforeTokenTransfer(address from, address to, uint256 amount)
        internal
        whenNotPaused // Modificador de Pausable
        override
    {
        super._beforeTokenTransfer(from, to, amount);
    }

    /**
     * @dev Crea 'amount' tokens para 'account'.
     * Requiere rol 'onlyMinter'.
     */
    function mintERC20(address account, uint256 amount) public onlyMinter whenNotPaused {
        _mint(account, amount);
    }

    /**
     * @dev Destruye 'amount' tokens de 'account'.
     * Requiere rol 'onlyBurner'.
     */
    function burnERC20(address account, uint256 amount) public onlyBurner whenNotPaused {
        _burn(account, amount);
    }


    // =========================================================================
    // --- FUNCIONES ERC-721 (NFT - RENOMBRADAS) ---
    // =========================================================================

    // --- Lectura (View) ---

    function balanceOfNFT(address owner_) public view returns (uint256) {
        require(owner_ != address(0), "Direccion no valida");
        return _nftBalances[owner_];
    }

    function ownerOf(uint256 tokenId) public view returns (address) {
        address owner_ = _nftOwners[tokenId];
        require(owner_ != address(0), "Token no existe");
        return owner_;
    }

    function getApprovedNFT(uint256 tokenId) public view returns (address) {
        require(_nftOwners[tokenId] != address(0), "Token no existe");
        return _nftTokenApprovals[tokenId];
    }

    function isApprovedForAllNFT(address owner_, address operator) public view returns (bool) {
        return _nftOperatorApprovals[owner_][operator];
    }

    function tokenURI(uint256 tokenId) public view returns (string memory) {
        require(_nftOwners[tokenId] != address(0), "Token no existe");
        return string(abi.encodePacked(_nftBaseURI, _nftTokenURIs[tokenId]));
    }

    function totalSupplyNFT() public view returns (uint256) {
        return _allNFTTokens.length;
    }

    function tokenByIndexNFT(uint256 index) public view returns (uint256) {
        require(index < _allNFTTokens.length, "Index out of bounds");
        return _allNFTTokens[index];
    }

    function tokenOfOwnerByIndexNFT(address owner_, uint256 index) public view returns (uint256) {
        require(index < _ownedNFTTokens[owner_].length, "Index out of bounds");
        return _ownedNFTTokens[owner_][index];
    }

    // --- Escritura (Transacción) ---

    function setBaseURI_NFT(string memory newBaseURI) public onlyOwner {
        _nftBaseURI = newBaseURI;
    }

    /**
     * @dev Crea un nuevo NFT.
     * Requiere rol 'onlyMinter'.
     */
    function mintNFT(address to, uint256 tokenId, string memory uri) public onlyMinter whenNotPaused {
        require(!_usedURIs[uri], "El URI ya esta en uso");
        _mintNFT_internal(to, tokenId);
        _setTokenURI_NFT(tokenId, uri);
        _usedURIs[uri] = true;

        _addTokenToAllTokensEnumeration(tokenId);
        _addTokenToOwnerEnumeration(to, tokenId);
    }

    /**
     * @dev Destruye un NFT.
     * Requiere rol 'onlyBurner'.
     */
    function burnNFT(uint256 tokenId) public onlyBurner whenNotPaused {
        address tokenOwner = ownerOf(tokenId);
        _approveNFT_internal(address(0), tokenId);
        _nftBalances[tokenOwner] -= 1;

        _removeTokenFromOwnerEnumeration(tokenOwner, tokenId);
        _removeTokenFromAllTokensEnumeration(tokenId);

        delete _usedURIs[_nftTokenURIs[tokenId]];
        delete _nftTokenURIs[tokenId];
        delete _nftOwners[tokenId];

        emit Transfer(tokenOwner, address(0), tokenId);
    }

    /**
     * @dev Aprueba un 'tokenId' de NFT para 'to'.
     * Esto es independiente del 'approve' de ERC20.
     */
    function approveNFT(address to, uint256 tokenId) public whenNotPaused {
        address owner_ = ownerOf(tokenId);
        require(to != owner_, "No puedes aprobarte a ti mismo");
        require(msg.sender == owner_ || isApprovedForAllNFT(owner_, msg.sender), "No autorizado");
        _approveNFT_internal(to, tokenId);
    }

    /**
     * @dev Aprueba a un 'operator' para todos los NFTs.
     * Esto es independiente del 'approve' de ERC20.
     */
    function setApprovalForAllNFT(address operator, bool approved) public whenNotPaused {
        require(operator != msg.sender, "No puedes aprobarte a ti mismo");
        _nftOperatorApprovals[msg.sender][operator] = approved;
        emit ApprovalForAll(msg.sender, operator, approved);
    }

    function transferFromNFT(address from, address to, uint256 tokenId) public whenNotPaused {
        address owner_ = ownerOf(tokenId);
        require(
            msg.sender == owner_ || msg.sender == getApprovedNFT(tokenId) || isApprovedForAllNFT(owner_, msg.sender),
            "No autorizado"
        );
        require(owner_ == from, "From no es el dueno");
        require(to != address(0), "Direccion destino invalida");
        require(from != to, "No puedes transferirte a ti mismo");

        _approveNFT_internal(address(0), tokenId);
        _nftBalances[from] -= 1;
        _nftBalances[to] += 1;
        _nftOwners[tokenId] = to;

        _removeTokenFromOwnerEnumeration(from, tokenId);
        _addTokenToOwnerEnumeration(to, tokenId);

        emit Transfer(from, to, tokenId);
    }

    function safeTransferFromNFT(address from, address to, uint256 tokenId) public whenNotPaused {
        safeTransferFromNFT(from, to, tokenId, "");
    }

    function safeTransferFromNFT(address from, address to, uint256 tokenId, bytes memory data) public whenNotPaused {
        transferFromNFT(from, to, tokenId);
        require(_checkOnERC721Received(from, to, tokenId, data), "Receptor no implementa ERC721Receiver");
    }

    // =========================================================================
    // --- FUNCIONES INTERNAS (NFT) ---
    // =========================================================================

    function _mintNFT_internal(address to, uint256 tokenId) internal {
        require(to != address(0), "No se puede mintear a la direccion cero");
        require(_nftOwners[tokenId] == address(0), "Token ya existe");

        _nftOwners[tokenId] = to;
        _nftBalances[to] += 1;
        emit Transfer(address(0), to, tokenId);
    }

    function _approveNFT_internal(address to, uint256 tokenId) internal {
        _nftTokenApprovals[tokenId] = to;
        emit Approval(ownerOf(tokenId), to, tokenId);
    }

     function _setTokenURI_NFT(uint256 tokenId, string memory uri) internal {
        require(_nftOwners[tokenId] != address(0), "Token no existe");
        _nftTokenURIs[tokenId] = uri;
    }

    function _checkOnERC721Received(address from, address to, uint256 tokenId, bytes memory data) private returns (bool) {
        if (to.code.length > 0) {
            try IERC721Receiver(to).onERC721Received(msg.sender, from, tokenId, data)
                returns (bytes4 retval)
            {
                return retval == IERC721Receiver.onERC721Received.selector;
            } catch {
                return false;
            }
        }
        return true;
    }

    // --- Enumeración interna (NFT) ---
    function _addTokenToOwnerEnumeration(address to, uint256 tokenId) private {
        _ownedNFTTokensIndex[tokenId] = _ownedNFTTokens[to].length;
        _ownedNFTTokens[to].push(tokenId);
    }

    function _removeTokenFromOwnerEnumeration(address from, uint256 tokenId) private {
        uint256 lastTokenIndex = _ownedNFTTokens[from].length - 1;
        uint256 tokenIndex = _ownedNFTTokensIndex[tokenId];

        if(tokenIndex != lastTokenIndex) {
            uint256 lastTokenId = _ownedNFTTokens[from][lastTokenIndex];
            _ownedNFTTokens[from][tokenIndex] = lastTokenId;
            _ownedNFTTokensIndex[lastTokenId] = tokenIndex;
        }

        _ownedNFTTokens[from].pop();
        delete _ownedNFTTokensIndex[tokenId];
    }

    function _addTokenToAllTokensEnumeration(uint256 tokenId) private {
        _allNFTTokensIndex[tokenId] = _allNFTTokens.length;
        _allNFTTokens.push(tokenId);
    }

    function _removeTokenFromAllTokensEnumeration(uint256 tokenId) private {
        uint256 lastTokenIndex = _allNFTTokens.length - 1;
        uint256 tokenIndex = _allNFTTokensIndex[tokenId];

        if(tokenIndex != lastTokenIndex) {
            uint256 lastTokenId = _allNFTTokens[lastTokenIndex];
            _allNFTTokens[tokenIndex] = lastTokenId;
            _allNFTTokensIndex[lastTokenId] = tokenIndex;
        }

        _allNFTTokens.pop();
        delete _allNFTTokensIndex[tokenId];
    }
}