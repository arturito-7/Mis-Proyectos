// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "openZeppelin/contracts/token/ERC20/ERC20.sol";
import "openZeppelin/contracts/access/Ownable.sol";

contract MiTokenSeguro is ERC20, Ownable {

    constructor(uint256 initialSupply) ERC20("MiTokenSeguro", "MTK") {
        _mint(msg.sender, initialSupply); //deploy + mint inicial
    }

    function mint(address account, uint256 amount) public onlyOwner {
        _mint(account, amount);
    }

    function burn(address account, uint256 amount) public onlyOwner {
        _burn(account, amount);
    }
}
