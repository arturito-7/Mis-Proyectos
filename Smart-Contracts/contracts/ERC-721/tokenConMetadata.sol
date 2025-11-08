// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract tokenAvanzado {
    string public name;
    string public symbol;

    address public owner; // dueño del contrato

    mapping(uint256 => address) private _owners;
    mapping(address => uint256) private _balances;
    mapping(uint256 => address) private _tokenApprovals;
    mapping(address => mapping(address => bool)) private _operatorApprovals;

    // --- NUEVO: metadatos ---
    mapping(uint256 => string) private _tokenURIs;
    string private _baseURI = "";

    // --- Eventos ---
    event Transfer(address indexed from, address indexed to, uint256 indexed tokenId);
    event Approval(address indexed owner, address indexed approved, uint256 indexed tokenId);
    event ApprovalForAll(address indexed owner, address indexed operator, bool approved);
    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);

    modifier onlyOwner() {
        require(msg.sender == owner, "Solo el dueno del contrato puede hacer esto");
        _;
    }

    constructor(string memory _name, string memory _symbol) {
        name = _name;
        symbol = _symbol;
        owner = msg.sender; // el que despliega el contrato es el dueño
    }

    // --- Propiedad del contrato ---
    function transferOwnership(address newOwner) public onlyOwner {
        require(newOwner != address(0), "Direccion invalida");
        emit OwnershipTransferred(owner, newOwner);
        owner = newOwner;
    }

    // --- Funciones basicas ERC-721 ---
    function balanceOf(address owner_) public view returns (uint256) {
        require(owner_ != address(0), "Direccion no valida");
        return _balances[owner_];
    }

    function ownerOf(uint256 tokenId) public view returns (address) {
        address owner_ = _owners[tokenId];
        require(owner_ != address(0), "Token no existe");
        return owner_;
    }

    // --- Mint solo para el owner del contrato ---
    function mint(address to, uint256 tokenId, string memory uri) public onlyOwner {
        _mint(to, tokenId);
        _setTokenURI(tokenId, uri);
    }

    function _mint(address to, uint256 tokenId) internal {
        require(to != address(0), "No se puede mintear a la direccion cero");
        require(_owners[tokenId] == address(0), "Token ya existe");

        _owners[tokenId] = to;
        _balances[to] += 1;
        emit Transfer(address(0), to, tokenId);
    }

    // --- Burn solo para el owner del contrato ---
    function burn(uint256 tokenId) public onlyOwner {
        address tokenOwner = ownerOf(tokenId);
        _approve(address(0), tokenId);
        _balances[tokenOwner] -= 1;
        delete _owners[tokenId];
        delete _tokenURIs[tokenId];
        emit Transfer(tokenOwner, address(0), tokenId);
    }

    // --- Transferencias ---
    function transferFrom(address from, address to, uint256 tokenId) public {
        address owner_ = ownerOf(tokenId);
        require(
            msg.sender == owner_ || msg.sender == getApproved(tokenId) || isApprovedForAll(owner_, msg.sender),
            "No autorizado"
        );
        require(owner_ == from, "From no es el dueno");
        require(to != address(0), "Direccion destino invalida");
        require(from != to, "No puedes transferirte a ti mismo");

        _approve(address(0), tokenId);
        _balances[from] -= 1;
        _balances[to] += 1;
        _owners[tokenId] = to;

        emit Transfer(from, to, tokenId);
    }

    function transfer(address to, uint256 tokenId) public {
        transferFrom(msg.sender, to, tokenId);
    }

    // --- Approvals ---
    function approve(address to, uint256 tokenId) public {
        address owner_ = ownerOf(tokenId);
        require(to != owner_, "No puedes aprobarte a ti mismo");
        require(msg.sender == owner_ || isApprovedForAll(owner_, msg.sender), "No autorizado");
        _approve(to, tokenId);
    }

    function getApproved(uint256 tokenId) public view returns (address) {
        require(_owners[tokenId] != address(0), "Token no existe");
        return _tokenApprovals[tokenId];
    }

    function _approve(address to, uint256 tokenId) internal {
        _tokenApprovals[tokenId] = to;
        emit Approval(ownerOf(tokenId), to, tokenId);
    }

    function setApprovalForAll(address operator, bool approved) public {
        require(operator != msg.sender, "No puedes aprobarte a ti mismo");
        _operatorApprovals[msg.sender][operator] = approved;
        emit ApprovalForAll(msg.sender, operator, approved);
    }

    function isApprovedForAll(address owner_, address operator) public view returns (bool) {
        return _operatorApprovals[owner_][operator];
    }

    // --- METADATOS ---
    function _setTokenURI(uint256 tokenId, string memory uri) internal {
        require(_owners[tokenId] != address(0), "Token no existe");
        _tokenURIs[tokenId] = uri;
    }

    function tokenURI(uint256 tokenId) public view returns (string memory) {
        require(_owners[tokenId] != address(0), "Token no existe");
        return string(abi.encodePacked(_baseURI, _tokenURIs[tokenId]));
    }

    function _setBaseURI(string memory newBaseURI) public onlyOwner {
        _baseURI = newBaseURI;
    }
}
