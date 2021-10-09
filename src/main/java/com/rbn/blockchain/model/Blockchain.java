package com.rbn.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbn.blockchain.exception.InvalidBlockException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blockchain {

  private final List<Block> chain;

  public Blockchain() {
    chain = new ArrayList<>();
    chain.add(Block.getGenesisBlock());
  }

  public void addBlock(Block block) throws InvalidBlockException {
    Block lastBlock = this.chain.get(this.chain.size() - 1);
    String generatedHash = Block.generateHash(lastBlock.getHash(),
        block.getData(),
        block.getNonce(),
        block.getDifficulty(),
        block.getEffortTime(),
        block.getTimestamp());
    boolean lastHashValid = block.getLastHash().equals(lastBlock.getHash());
    boolean validProofOfWork =
        generatedHash.equals(block.getHash());
    if (lastHashValid && validProofOfWork) {
      this.chain.add(block);
    } else {
      throw new InvalidBlockException();
    }
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
      String generatedHash = Block.generateHash(lastBlock.getHash(),
          currentBlock.getData(),
          currentBlock.getNonce(),
          currentBlock.getDifficulty(),
          currentBlock.getEffortTime(),
          currentBlock.getTimestamp());
      boolean wrongProofOfWork = !generatedHash.equals(currentBlock.getHash());
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
