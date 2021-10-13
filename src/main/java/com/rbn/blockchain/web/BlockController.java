package com.rbn.blockchain.web;

import com.rbn.blockchain.exception.NotFoundException;
import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.wallet.Block;
import com.rbn.blockchain.service.DefaultBlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blockchain")
public class BlockController {

  @Autowired
  private DefaultBlockchainService blockchainService;

  @GetMapping
  public Blockchain getAll() {
    return blockchainService.getBlockchain();
  }

  @GetMapping("/{hash}")
  public Block get(@PathVariable("hash") String hash) throws NotFoundException {
    return blockchainService.getBlock(hash);
  }

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PostMapping
  public void add(@RequestBody Block block) {
    blockchainService.add(block);
  }

  @PostMapping("/mine")
  public Block mine() {
    return blockchainService.mine();
  }

  @PutMapping("/consensus")
  public Blockchain post(@RequestBody Blockchain blockchain) {
    return blockchainService.consensus(blockchain);
  }

}
