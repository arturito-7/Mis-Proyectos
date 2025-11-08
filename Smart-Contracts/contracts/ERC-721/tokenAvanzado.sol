// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract tokenAvanzado {
    string public name;
    string public symbol;

    mapping(uint256 => address) private _owners;
    mapping(address => uint256) private _balances;
    mapping(uint256 => address) private _tokenApprovals;
    mapping(address => mapping(address => bool)) private _operatorApprovals;

    event Transfer(address indexed from, address indexed to, uint256 indexed tokenId);
    event Approval(address indexed owner, address indexed approved, uint256 indexed tokenId);
    event ApprovalForAll(address indexed owner, address indexed operator, bool approved);

    constructor(string memory _name, string memory _symbol) {
        name = _name;
        symbol = _symbol;
    }

    function balanceOf(address owner) public view returns (uint256) {
        require(owner != address(0), "Direccion no valida");
        return _balances[owner];
    }

    function ownerOf(uint256 tokenId) public view returns (address) {
        address owner = _owners[tokenId];
        require(owner != address(0), "Token no existe");
        return owner;
    }

    function _mint(address to, uint256 tokenId) internal {
        require(to != address(0), "No se puede mintear a la direccion cero");
        require(_owners[tokenId] == address(0), "Token ya existe");

        _owners[tokenId] = to;
        _balances[to] += 1;

        emit Transfer(address(0), to, tokenId);
    }

    function transferFrom(address from, address to, uint256 tokenId) public {
        address owner = ownerOf(tokenId);
        require(
            msg.sender == owner || msg.sender == getApproved(tokenId) || isApprovedForAll(owner, msg.sender),
            "No autorizado para transferir"
        );
        require(owner == from, "From no es el dueno");
        require(to != address(0), "Direccion destino invalida");
        require(from != to, "No puedes transferirte a ti mismo");

        _approve(address(0), tokenId);

        _balances[from] -= 1;
        _balances[to] += 1;
        _owners[tokenId] = to;

        emit Transfer(from, to, tokenId);
    }

    function approve(address to, uint256 tokenId) public {
        address owner = ownerOf(tokenId);
        require(to != owner, "No puedes aprobarte a ti mismo");
        require(msg.sender == owner || isApprovedForAll(owner, msg.sender), "No autorizado");

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

    function isApprovedForAll(address owner, address operator) public view returns (bool) {
        return _operatorApprovals[owner][operator];
    }

    // --- Mint público para pruebas ---
    function mintPublic(uint256 tokenId) public {
        _mint(msg.sender, tokenId);
    }

    function transfer(address to, uint256 tokenId) public {
        transferFrom(msg.sender, to, tokenId);
    }


}