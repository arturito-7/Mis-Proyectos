// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

//Prueba de código no payable

contract prueba3 {

    //unsigned int de 256 dígitos, variable de estado, se guarda en la blockchain
    uint256 numero;
    function set(uint256 _num) public {
        numero = _num;
    }

    function get() public view returns (uint256) {
        return numero;
    }

}