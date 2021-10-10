package com.rbn.blockchain.service;

import com.rbn.blockchain.model.wallet.Wallet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultWalletService {

  public Wallet create(BigDecimal initialAmount) {
    if (initialAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be bigger than zero");
    }
    return new Wallet(initialAmount);
  }

}
