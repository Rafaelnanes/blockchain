package com.rbn.blockchain.web;

import com.rbn.blockchain.model.Blockchain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

  private Blockchain blockchain;

  @PostConstruct
  public void init() {
    this.blockchain = new Blockchain();
  }

  @GetMapping
  public Blockchain get() {
    return blockchain;
  }

  @PostMapping("/replace")
  public Blockchain post(@RequestBody Blockchain blockchain) {
    return blockchain.replaceChain(blockchain);
  }

}
