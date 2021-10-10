package com.rbn.blockchain.model.wallet;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class TransactionPool {

  private final Set<Transaction> transactions;

  public TransactionPool() {
    this.transactions = new HashSet<>();
  }

}
