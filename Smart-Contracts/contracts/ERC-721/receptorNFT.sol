// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

interface IERC721Receiver {
    function onERC721Received(address operator, address from, uint256 tokenId, bytes calldata data)
        external
        returns (bytes4);
}

contract ReceptorNFT is IERC721Receiver {
    event NFTRecibido(address operador, address desde, uint256 tokenId, bytes data);

    function onERC721Received(address operator, address from, uint256 tokenId, bytes calldata data)
        external
        override
        returns (bytes4)
    {
        emit NFTRecibido(operator, from, tokenId, data);
        return this.onERC721Received.selector;
    }
}
