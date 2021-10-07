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
    Block lastBlock = this.chain.get(this.chain.size() - 1);
    String lastHash = lastBlock.getHash();
    int difficulty = lastBlock.getDifficulty();
    if (lastBlock.getEffort() >= Block.MINE_RATE) {
      difficulty--;
    } else {
      difficulty++;
    }
    Block blockMined = new Block(lastHash, data, difficulty).mine();
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
      boolean wrongProofOfWork = Block.generateHash(currentBlock).equals(currentBlock.getHash());
      boolean lastHashDoNotMatch = !currentBlock.getLastHash().equals(lastBlock.getHash());
      if (lastHashDoNotMatch || wrongProofOfWork) {
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
