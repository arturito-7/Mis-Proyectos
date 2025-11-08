// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

contract Billetera{

    //función para recibir Ether directamente
    receive() external payable { } 

    //otra forma (por si alguien llama a una función que no existe)
    fallback() external payable { }

    //ver saldo total del contrato
    function getBalance() public view returns (uint256){
        return address(this).balance; //this es el contrato actual
    }

    //enviar Ether a otra dirección
    function enviar(address payable _to, uint256 _amount) public{
        require(address(this).balance >= _amount, "No hay saldo suficiente");
        _to.transfer(_amount);
    }

}