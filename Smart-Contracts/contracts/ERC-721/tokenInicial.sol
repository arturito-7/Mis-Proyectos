// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract tokenInicial{

    mapping(uint256 => address) private _owners;
    mapping(address => uint256) private _balances;

    function balanceOf(address owner) public view returns (uint256){
        require(owner != address(0), "Direccion no valida");
        return _balances[owner];
    }

    function ownerOf(uint256 tokenId) public view returns (address){
        require(_owners[tokenId] != address(0), "Token no existe");
        return _owners[tokenId];
    }

}