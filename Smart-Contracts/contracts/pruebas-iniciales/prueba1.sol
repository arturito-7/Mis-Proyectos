pragma solidity ^0.5.16;

contract P{

    event Prueba(string mensaje);
    
    function f() public{ 
        emit Prueba("Esto es una prueba"); 
    } 
}