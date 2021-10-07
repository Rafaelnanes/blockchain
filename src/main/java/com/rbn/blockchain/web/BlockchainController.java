package com.rbn.blockchain.web;

import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.service.DefaultBlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

  @Autowired
  private DefaultBlockchainService blockchainService;

  @GetMapping
  public Blockchain get() {
    return blockchainService.getBlockchain();
  }

  @PutMapping
  public Blockchain post(@RequestBody Blockchain blockchain) {
    return blockchainService.replace(blockchain);
  }

}
