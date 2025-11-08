// SPDX-License-Identifier: MIT
pragma solidity ^0.8.13;

interface IBilletera {
    function getBalance() external view returns (uint256);
    function enviar (address payable _to, uint256 _amount) external;
}