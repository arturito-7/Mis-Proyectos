// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

interface IERC20 {
    function transferFrom(address from, address to, uint256 amount) external returns (bool);
    function transfer(address to, uint256 amount) external returns (bool);
}

contract TiendaNFT {
    // ---------------------------
    // Variables del contrato
    // ---------------------------
    IERC20 public token;        // Token ERC20 que se usa como moneda
    address public owner;       
    uint256 public precioNFT;   // Precio en tokens por cada NFT
    uint256 public totalVendidos; // Contador de NFTs vendidos

    // --- NFT simple dentro del contrato ---
    struct NFT {
        address owner;
        string uri;
    }

    mapping(uint256 => NFT) public nfts;  // tokenId => NFT
    mapping(address => uint256[]) public nftsDeUsuario; // para listar NFTs de cada usuario

    // ---------------------------
    // Eventos
    // ---------------------------
    event NFTComprado(address indexed comprador, uint256 tokenId, string uri, uint256 tokensGastados, uint256 tokensDevueltos);

    // ---------------------------
    // Constructor
    // ---------------------------
    constructor(address _tokenAddress, uint256 _precioNFT) {
        require(_tokenAddress != address(0), "Direccion token invalida");
        token = IERC20(_tokenAddress);
        owner = msg.sender;
        precioNFT = _precioNFT;
    }

    modifier onlyOwner() {
        require(msg.sender == owner, "Solo owner");
        _;
    }

    // ---------------------------
    // Comprar NFT
    // ---------------------------
    function comprarNFT(string calldata uri, uint256 cantidadPagada) external {
        require(cantidadPagada >= precioNFT, "No se envian suficientes tokens");

        // Transferimos los tokens del usuario al contrato
        require(token.transferFrom(msg.sender, address(this), cantidadPagada), "Pago fallido");

        // Calculamos si hay sobrante y lo devolvemos
        uint256 tokensDevueltos = 0;
        if(cantidadPagada > precioNFT) {
            tokensDevueltos = cantidadPagada - precioNFT;
            require(token.transfer(msg.sender, tokensDevueltos), "Devolucion fallida");
        }

        // Calculamos tokenId automáticamente
        totalVendidos += 1;
        uint256 tokenId = totalVendidos;

        // Mint simple
        nfts[tokenId] = NFT(msg.sender, uri);
        nftsDeUsuario[msg.sender].push(tokenId);

        emit NFTComprado(msg.sender, tokenId, uri, cantidadPagada, tokensDevueltos);
    }

    // ---------------------------
    // Consultas
    // ---------------------------
    function nftDeUsuario(address usuario) external view returns (uint256[] memory) {
        return nftsDeUsuario[usuario];
    }

    function verNFT(uint256 tokenId) external view returns (address propietario, string memory uri) {
        NFT memory nft_ = nfts[tokenId];
        return (nft_.owner, nft_.uri);
    }

    function totalNFTsVendidos() external view returns (uint256) {
        return totalVendidos;
    }

    // ---------------------------
    // Owner puede retirar tokens
    // ---------------------------
    function retirarTokens(address destino, uint256 cantidad) external onlyOwner {
        require(token.transfer(destino, cantidad), "Retiro fallido");
    }

    function setPrecioNFT(uint256 nuevoPrecio) external onlyOwner {
        precioNFT = nuevoPrecio;
    }
}
