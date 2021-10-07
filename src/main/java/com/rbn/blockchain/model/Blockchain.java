package com.rbn.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blockchain {

  private final List<Block> chain;

  public Blockchain() {
    chain = new ArrayList<>();
    chain.add(Block.getGenesisBlock());
  }

  public Block addBlock(String data) {
    String lastHash = this.chain.get(this.chain.size() - 1).getHash();
    Block blockMined = new Block(lastHash, data).mine();
    this.chain.add(blockMined);
    return blockMined;
  }

  @JsonIgnore
  public Block getLastBlock() {
    return chain.get(chain.size() - 1);
  }

  public List<Block> getChain() {
    return Collections.unmodifiableList(chain);
  }

  @JsonIgnore
  public boolean isChainValid() {
    boolean isValid = true;
    for (int i = 1; i < chain.size(); i++) {
      Block lastBlock = chain.get(i - 1);
      Block currentBlock = chain.get(i);
      if (!currentBlock.getLastHash().equals(lastBlock.getHash())) {
        isValid = false;
        break;
      }
    }
    return isValid;
  }

  public Blockchain replaceChain(Blockchain blockchain) {
    List<Block> localChain = blockchain.getChain();
    boolean isBigger = localChain.size() > this.chain.size();
    if (isBigger && blockchain.isChainValid()) {
      this.chain.clear();
      this.chain.addAll(localChain);
    }
    return this;
  }

}
