// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "@openzeppelin/contracts-upgradeable/token/ERC20/ERC20Upgradeable.sol";
import "@openzeppelin/contracts-upgradeable/access/OwnableUpgradeable.sol";
import "@openzeppelin/contracts-upgradeable/proxy/utils/UUPSUpgradeable.sol";

contract MiTokenUpgradeableV2 is ERC20Upgradeable, OwnableUpgradeable, UUPSUpgradeable {

    function initializeV2() public reinitializer(2) {
        // inicialización extra de la V2 si hace falta
    }

    function mintExtra(address account, uint256 amount) public onlyOwner {
        _mint(account, amount);
    }

    function _authorizeUpgrade(address newImplementation) internal override onlyOwner {}
}
