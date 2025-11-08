// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

import "./IERC20p1.sol";

contract MiToken is IERC20 {

    string public name = "MiToken";
    string public symbol = "MTK";
    uint8 public decimals = 18;	

    uint256 private _totalSupply;
    mapping(address => uint256) private _balances;

    constructor(uint256 initialSupply){
        _totalSupply = initialSupply;
        _balances[msg.sender] = initialSupply; //el deployer recibe todos los tokens
        emit Transfer(address(0), msg.sender, initialSupply);
    }

    function totalSupply () public view override returns (uint256){
        return _totalSupply;
    }

    function balanceOf (address account) public view override returns (uint256){
        return _balances[account];
    }

    function transfer(address recipent, uint256 amount) public override returns (bool) {
        require(_balances[msg.sender] >= amount, "Saldo insuficiente"); 
        //si el balance es mayor o igual al monto, se ejecuta el transfer);

        _balances[msg.sender] -= amount;
        _balances[recipent] += amount;

        //se actualiza el balance del sender y del recipent
        emit Transfer(msg.sender, recipent, amount);
        return true;

    }

}