package com.rbn.blockchain.model.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

  private final Wallet senderWallet;
  private final String recipientPublicKey;
  private final String id;
  private final Map<String, BigDecimal> output;
  private final TransactionInput input;

  Transaction(Wallet senderWallet, String recipientPublicKey, BigDecimal amount) {
    this.senderWallet = senderWallet;
    this.recipientPublicKey = recipientPublicKey;
    this.id = UUID.randomUUID().toString();

    this.output = new HashMap<>();
    this.output.put(recipientPublicKey, amount);
    this.output.put(senderWallet.getPublicKey(), senderWallet.getBalance().subtract(amount));

    this.input = new TransactionInput(this);
  }

  public static boolean isValid(Transaction transaction) {
    BigDecimal totalAmount = transaction.getOutputMap()
                                        .values()
                                        .stream()
                                        .reduce(BigDecimal::add)
                                        .get();
    boolean isDataValid = Wallet.verify(transaction.senderWallet.getPublicKey(),
        transaction.getOutputMapAsString(),
        transaction.getInputMap().signature);
    return isDataValid && totalAmount.compareTo(transaction.getInputMap().amount) == 0;
  }

  public Map<String, BigDecimal> getOutputMap() {
    return Collections.unmodifiableMap(this.output);
  }

  @SneakyThrows
  public String getOutputMapAsString() {
    return new ObjectMapper().writeValueAsString(this.output.toString());
  }

  public TransactionInput getInputMap() {
    return input;
  }

  public String getId() {
    return this.id;
  }

  public String getRecipient() {
    return this.recipientPublicKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction that = (Transaction) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(senderWallet, recipientPublicKey, id, output, input);
  }


  @Getter
  public static class TransactionInput {
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
      this.signature = senderWallet.sign(transaction.getOutputMapAsString());
    }
  }
}
