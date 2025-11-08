// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

import "./IBilletera.sol";

contract BilleteraBasica is IBilletera {
    
    receive() external payable {}

    function getBalance() public view override returns (uint256) {
        return address(this).balance;
    }

    function enviar(address payable destinatario, uint256 cantidad) public override {
        require(address(this).balance >= cantidad, "Saldo insuficiente");
        destinatario.transfer(cantidad);
    }

}