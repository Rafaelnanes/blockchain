package com.rbn.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbn.blockchain.exception.InvalidBlockException;
import com.rbn.blockchain.model.wallet.Block;
import com.rbn.blockchain.model.wallet.Transaction;
import com.rbn.blockchain.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Blockchain {

  private final List<Block> chain;

  private final TransactionPool transactionPool;

  public Blockchain() {
    this.chain = new ArrayList<>();
    this.transactionPool = new TransactionPool();
    this.chain.add(Block.getGenesisBlock());
  }

  public void addBlock(Block block) throws InvalidBlockException {
    Block lastBlock = this.chain.get(this.chain.size() - 1);
    String generatedHash = Utils.generateHash(lastBlock.getHash(),
        block.getParsedData(),
        String.valueOf(block.getNonce()));
    boolean lastHashValid = block.getLastHash().equals(lastBlock.getHash());
    boolean validProofOfWork = generatedHash.equals(block.getHash());
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

  public TransactionPool getTransactionPool() {
    return this.transactionPool;
  }

  @JsonIgnore
  public boolean isChainValid() {
    boolean isValid = true;
    for (int i = 1; i < chain.size(); i++) {
      Block lastBlock = chain.get(i - 1);
      Block currentBlock = chain.get(i);
      String generatedHash = Utils.generateHash(lastBlock.getHash(),
          currentBlock.getParsedData(),
          String.valueOf(currentBlock.getNonce()));
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

  public BigDecimal getBalance(String publicKey) {
    BigDecimal amount = BigDecimal.ZERO;
    for (Block block : this.chain) {
      for (Transaction transaction : block.getData()) {
        if (!transaction.getOutputMap().containsKey(publicKey)) {
          continue;
        }
        if (transaction.getInputMap().getAddress().equals(publicKey)) {
          amount = transaction.getOutputMap().get(publicKey);
        } else {
          amount = amount.add(transaction.getOutputMap().get(publicKey));
        }
      }
    }
    return amount;
  }

}
