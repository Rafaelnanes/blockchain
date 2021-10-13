package com.rbn.blockchain.model;

import com.rbn.blockchain.exception.InvalidTransactionException;
import com.rbn.blockchain.model.wallet.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TransactionPool {

  private final List<Transaction> transactions = new ArrayList<>();

  public Optional<Transaction> getExistingTransaction(Transaction transaction) {
    return transactions.stream()
                       .filter(x -> x.getInputMap().getAddress().equals(transaction.getInputMap().getAddress()))
                       .findAny();
  }

  public void setTransaction(Transaction transaction) {
    if (!Transaction.isValid(transaction)) {
      log.info("Invalid transaction: {}", transaction);
      throw new InvalidTransactionException();
    }
    Optional<Transaction> existingTransaction = getExistingTransaction(transaction);
    if (existingTransaction.isPresent()) {
      existingTransaction.get().update(transaction);
    } else {
      transactions.add(transaction);
    }
  }

  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }

  public void clear() {
    this.transactions.clear();
  }
}
