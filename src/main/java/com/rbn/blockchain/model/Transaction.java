package com.rbn.blockchain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Transaction {

  private final Wallet senderWallet;
  private final String recipientPublicKey;
  private final BigDecimal amount;
  private final String id;
  private final Map<String, Object> outputMap;
  private final Map<String, Object> inputMap;

  public Transaction(Wallet senderWallet, String recipientPublicKey, BigDecimal amount) {
    this.senderWallet = senderWallet;
    this.recipientPublicKey = recipientPublicKey;
    this.amount = amount;
    this.id = UUID.randomUUID().toString();
    this.outputMap = new HashMap<>();
    this.inputMap = new HashMap<>();
  }

  public Map<String, Object> getOutputMap() {
    this.outputMap.put(recipientPublicKey, amount);
    this.outputMap.put(senderWallet.getPublicKey(), senderWallet.getBalance().subtract(amount));
    return this.outputMap;
  }

  public TransactionInput getInputMap() {
    return new TransactionInput(this);
  }

  @Getter
  private static class TransactionInput {
    private final LocalDateTime timestamp;
    private final BigDecimal amount;
    private final String address;
    private final String signature;

    @SneakyThrows
    public TransactionInput(Transaction transaction) {
      this.timestamp = LocalDateTime.now();
      Wallet senderWallet = transaction.senderWallet;
      this.amount = senderWallet.getBalance();
      this.address = senderWallet.getPublicKey();
      this.signature = senderWallet.sign(new ObjectMapper().writeValueAsString(transaction.outputMap.toString()));
    }
  }

}
