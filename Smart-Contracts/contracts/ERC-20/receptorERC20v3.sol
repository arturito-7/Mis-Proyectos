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
    event EtherRecibido(address indexed remitente, uint256 cantidad);
    event EtherRetirado(address indexed destino, uint256 cantidad);

    constructor(address _tokenAddress) {
        require(_tokenAddress != address(0), "Direccion de token invalida");
        token = IERC20(_tokenAddress);
        owner = msg.sender;
    }

    modifier onlyOwner() {
        require(msg.sender == owner, "Solo el owner puede ejecutar esto");
        _;
    }

    // --------------------------
    // Funciones para tokens ERC20
    // --------------------------
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

    // --------------------------
    // Funciones para manejar Ether
    // --------------------------
    // Permite que el contrato reciba Ether directamente
    receive() external payable {
        emit EtherRecibido(msg.sender, msg.value);
    }

    fallback() external payable {
        emit EtherRecibido(msg.sender, msg.value);
    }

    // Consulta saldo de Ether
    function saldoEther() public view returns (uint256) {
        return address(this).balance;
    }

    // Retirar Ether a otra cuenta (solo owner)
    function retirarEther(address payable destino, uint256 cantidad) external onlyOwner {
        require(destino != address(0), "Direccion destino invalida");
        require(cantidad > 0, "Cantidad debe ser mayor que cero");
        require(cantidad <= address(this).balance, "No hay suficiente Ether");

        destino.transfer(cantidad);
        emit EtherRetirado(destino, cantidad);
    }

    // --------------------------
    // Función para transferir propiedad
    // --------------------------
    function transferirPropiedad(address nuevoOwner) external onlyOwner {
        require(nuevoOwner != address(0), "Direccion invalida");
        owner = nuevoOwner;
    }
}
