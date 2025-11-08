// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract BilleteraSegura {
    
    address payable public owner; //dueño del contrato

    constructor() {
        owner = payable(msg.sender); //quien desplega el contrato es el dueño
    }

    //Modifier: restringe el acceso a ciertas funciones
    modifier onlyOwner(){
        require(msg.sender == owner, "No eres el propietario");
        _; //Aquí falta el código de la función real
    }

    //Recibir ether
    receive() external payable { }

    //otra forma (por si alguien llama a una función que no existe)
    fallback() external payable { }

    //Consultar saldo
    function getBalance() public view returns (uint256){
        return address(this).balance;
    }

    //Enviar ether del contrato a una dirección externa
    function enviar(address payable _to, uint256 _amount) public onlyOwner{
        require(address(this).balance >= _amount, "Saldo insuficiente");
        _to.transfer(_amount);
    }

}