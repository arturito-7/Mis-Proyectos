// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

interface IERC20 {
    function transferFrom(address from, address to, uint256 amount) external returns (bool);
    function balanceOf(address account) external view returns (uint256);
    function transfer(address to, uint256 amount) external returns (bool);
}

contract PagoConToken {
    IERC20 public token;  // referencia al contrato ERC20
    address public owner;
    uint256 public totalRecaudado;

    event PagoRealizado(address indexed pagador, uint256 cantidad);

    constructor(address _tokenAddress) {
        token = IERC20(_tokenAddress);  // guardamos la dirección del token
        owner = msg.sender;
    }

    function pagar(uint256 cantidad) public {
        // Transferimos tokens del usuario a este contrato
        require(token.transferFrom(msg.sender, address(this), cantidad), "Fallo en el pago");
        totalRecaudado += cantidad;

        emit PagoRealizado(msg.sender, cantidad);
    }

    // El contrato reenvía tokens a otra cuenta
    function reenviarPago(address destinatario, uint256 cantidad) external {
        require(token.transfer(destinatario, cantidad),"Reenvio fallido");
    }

    function saldoDelContrato() public view returns (uint256) {
        return token.balanceOf(address(this));
    }
}
