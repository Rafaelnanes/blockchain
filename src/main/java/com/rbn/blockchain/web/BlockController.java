package com.rbn.blockchain.web;

import com.rbn.blockchain.exception.NotFoundException;
import com.rbn.blockchain.model.Block;
import com.rbn.blockchain.service.DefaultBlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blocks")
public class BlockController {

  @Autowired
  private DefaultBlockchainService blockchainService;

  @GetMapping("/{hash}")
  public Block get(@PathVariable("hash") String hash) throws NotFoundException {
    return blockchainService.getBlock(hash);
  }

  @PostMapping("/mine")
  public Block mine(@RequestBody String data) {
    return blockchainService.mine(data);
  }

}
