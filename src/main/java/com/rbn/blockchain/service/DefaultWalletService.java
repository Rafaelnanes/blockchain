package com.rbn.blockchain.service;

import com.rbn.blockchain.model.wallet.Wallet;
import org.springframework.stereotype.Service;

@Service
public class DefaultWalletService {

  public Wallet create() {
    return new Wallet();
  }

}
