// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "@openzeppelin/contracts-upgradeable/token/ERC20/ERC20Upgradeable.sol";
import "@openzeppelin/contracts-upgradeable/access/OwnableUpgradeable.sol";
import "@openzeppelin/contracts-upgradeable/proxy/utils/UUPSUpgradeable.sol";

contract MiTokenUpgradeable is ERC20Upgradeable, OwnableUpgradeable, UUPSUpgradeable{

    // constructor se reemplaza por initialize
    function initialize(uint256 initialSupply) public initializer {
        __ERC20_init("MiTokenUpgradeable", "MTKU");
        __Ownable_init();
        _mint(msg.sender, initialSupply);
    }

    function mint(address account, uint256 amount) public onlyOwner {
        _mint(account, amount);
    }

    function burn(address account, uint256 amount) public onlyOwner {
        _burn(account, amount);
    }

    // función requerida por UUPS para validar actualizaciones
    function _authorizeUpgrade(address newImplementation) internal override onlyOwner {}

}