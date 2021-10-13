package com.rbn.blockchain.service;

import com.rbn.blockchain.model.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DefaultWalletService {

  private final DefaultBlockchainService blockchainService;

  public Wallet create() {
    return new Wallet();
  }

  public Wallet get(String privateKey, String publicKey) {
    return new Wallet(privateKey, publicKey, blockchainService.getBlockchain());
  }

  public BigDecimal getBalance(String publicKey) {
    return blockchainService.getBlockchain().getBalance(publicKey);
  }

}
