pragma solidity ^0.5.16;

contract P {
    event Prueba(string mensaje);

    // El constructor emite un evento cuando se despliega el contrato
    constructor() public {
        emit Prueba("✅ Contrato desplegado correctamente");
    }

    // Esta función también emite un evento cuando la llamas
    function f() public {
        emit Prueba("🎯 Esto es una prueba ejecutada desde f()");
    }
}
