// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract MiToken {

    string public name = "MiToken";
    string public symbol = "MTK";
    uint8 public decimals = 18;	

    uint256 private _totalSupply;
    mapping(address => uint256) private _balances;

    //registro de autorizaciones
    mapping(address => mapping(address => uint256)) private _allowances;
    //Es un diccionario (clave, valor) cuya clave es owner y el valor es otro diccionario
    // que tiene el spender (quien puede gastar el dinero del owner) y que cantidad 

    constructor(uint256 initialSupply){
        _totalSupply = initialSupply;
        _balances[msg.sender] = initialSupply; //el deployer recibe todos los tokens
        emit Transfer(address(0), msg.sender, initialSupply);
    }

    event Transfer(address indexed from, address indexed to, uint256 value);
    event Approval(address indexed owner, address indexed spender, uint256 value);

    function totalSupply () public view returns (uint256){
        return _totalSupply;
    }

    function balanceOf (address account) public view returns (uint256){
        return _balances[account];
    }

    function transfer(address recipent, uint256 amount) public returns (bool) {
        require(_balances[msg.sender] >= amount, "Saldo insuficiente"); 
        //si el balance es mayor o igual al monto, se ejecuta el transfer);

        _balances[msg.sender] -= amount;
        _balances[recipent] += amount;

        //se actualiza el balance del sender y del recipent
        emit Transfer(msg.sender, recipent, amount);
        return true;

    }

    //Función para dar permiso a alguien para tocar tus tokens y que cantidad
    function approve(address spender, uint256 amount) public returns (bool){
        _allowances[msg.sender][spender] = amount;
        emit Approval(msg.sender, spender, amount);
        return true;
    } 

    //Función para ver el monto que se autorizó a un spender para gastar de un owner
    //Si no están "enlazados" devuelve 0
    function allowance(address owner, address spender) public view returns (uint256){
        return _allowances[owner][spender];
    }

    function transferFrom(address owner, address recipent, uint256 amount) public returns (bool){
        require(_balances[owner] >= amount, "Saldo insuficiente");
        require(_allowances[owner][msg.sender] >= amount, "No tienes permiso para gastar tanto");

        _balances[owner] -= amount;
        _balances[recipent] += amount;
        _allowances[owner][msg.sender] -= amount;

        emit Transfer(owner, recipent, amount);
        return true;
    }




}