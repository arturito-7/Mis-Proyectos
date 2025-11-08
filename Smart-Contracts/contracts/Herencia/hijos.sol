// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

//Contrato base 
contract Padre{
    string public nombre = "Contrato Padre";

    function decirHola() public pure returns (string memory) {
        return "Hola desde el Padre";
    }

}

//Contrato Hijo
contract Hijo is Padre {

    function decirAdios() public pure returns (string memory){
        return "Adios desde el Hijo";
    }

}