// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

interface IERC20 {
    function transferFrom(address from, address to, uint256 amount) external returns (bool);
    function balanceOf(address account) external view returns (uint256);
    function transfer(address to, uint256 amount) external returns (bool);
}

contract PagoConToken {
    IERC20 public token;          
    address public owner;         
    uint256 public totalRecaudado;

    event PagoRealizado(address indexed pagador, uint256 cantidad);

    constructor(address _tokenAddress) {
        require(_tokenAddress != address(0), "Direccion de token invalida");
        token = IERC20(_tokenAddress);
        owner = msg.sender;
    }

    modifier onlyOwner() {
        require(msg.sender == owner, "Solo el owner puede ejecutar esto");
        _;
    }

    function pagar(uint256 cantidad) public {
        require(cantidad > 0, "Cantidad debe ser mayor que cero");
        require(
            token.transferFrom(msg.sender, address(this), cantidad),
            "Fallo en el pago"
        );

        totalRecaudado += cantidad;
        emit PagoRealizado(msg.sender, cantidad);
    }

    function reenviarPago(address destinatario, uint256 cantidad) external onlyOwner {
        require(destinatario != address(0), "Direccion destino invalida");
        require(cantidad > 0, "Cantidad debe ser mayor que cero");
        require(
            token.transfer(destinatario, cantidad),
            "Reenvio fallido"
        );
        totalRecaudado -= cantidad;
    }

    function saldoDelContrato() public view returns (uint256) {
        return token.balanceOf(address(this));
    }

    function transferirPropiedad(address nuevoOwner) external onlyOwner {
        require(nuevoOwner != address(0), "Direccion invalida");
        owner = nuevoOwner;
    }
}
