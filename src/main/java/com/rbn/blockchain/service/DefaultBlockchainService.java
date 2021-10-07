package com.rbn.blockchain.service;

import com.rbn.blockchain.exception.NotFoundException;
import com.rbn.blockchain.model.Block;
import com.rbn.blockchain.model.Blockchain;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class DefaultBlockchainService {

  private final Blockchain blockchain = new Blockchain();

  public Block getBlock(String hash) throws NotFoundException {
    return blockchain.getChain()
                     .stream()
                     .filter(x -> x.getHash().equals(hash))
                     .findAny()
                     .orElseThrow(NotFoundException::new);
  }

  public Blockchain replace(Blockchain blockchain) {
    return this.blockchain.replaceChain(blockchain);
  }

  public Block mine(String data) {
    return blockchain.addBlock(data);
  }
}
