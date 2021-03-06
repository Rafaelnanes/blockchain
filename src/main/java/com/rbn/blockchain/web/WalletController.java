package com.rbn.blockchain.web;

import com.rbn.blockchain.model.wallet.Wallet;
import com.rbn.blockchain.service.DefaultWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  @Autowired
  private DefaultWalletService defaultWalletService;

  @GetMapping("/balance")
  public BigDecimal get(@RequestParam("publicKey") String publicKey) {
    return defaultWalletService.getBalance(publicKey);
  }

  @PostMapping
  public Wallet create() {
    return defaultWalletService.create();
  }

}
