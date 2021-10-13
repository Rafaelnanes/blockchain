package com.rbn.blockchain.web;

import com.rbn.blockchain.model.wallet.Wallet;
import com.rbn.blockchain.service.DefaultWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  @Autowired
  private DefaultWalletService defaultWalletService;

  @PostMapping
  public Wallet create() {
    return defaultWalletService.create();
  }

}
