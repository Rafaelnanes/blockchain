package com.rbn.blockchain.model.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbn.blockchain.exception.AmountExceedsBalanceException;
import com.rbn.blockchain.exception.InvalidSignatureException;
import com.rbn.blockchain.exception.InvalidWalletException;
import lombok.Getter;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Transaction {

  private final Wallet senderWallet;
  private final String recipientPublicKey;
  private final String id;
  private final Map<String, BigDecimal> output;
  private TransactionInput input;

  Transaction(Wallet senderWallet, String recipientPublicKey, BigDecimal amount) {
    this.senderWallet = senderWallet;
    this.recipientPublicKey = recipientPublicKey;
    this.id = UUID.randomUUID().toString();

    this.output = new HashMap<>();
    this.output.put(recipientPublicKey, amount);
    this.output.put(senderWallet.getPublicKey(), senderWallet.getBalance());

    this.input = new TransactionInput(this, amount);
  }

  public static boolean isValid(Transaction transaction) {
    BigDecimal totalAmount = transaction.getOutputMap()
                                        .entrySet()
                                        .stream()
                                        .filter(x -> !x.getKey().equals(transaction.getInputMap().address))
                                        .map(Map.Entry::getValue)
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

  public void update(Transaction transaction) {
    if (!transaction.getInputMap().getAddress().equals(senderWallet.getPublicKey())) {
      throw new InvalidWalletException();
    }

    boolean signatureValid =
        Wallet.verify(senderWallet.getPublicKey(), getOutputMapAsString(), getInputMap().signature);
    if (!signatureValid) {
      throw new InvalidSignatureException();
    }

    BigDecimal balance = this.output.get(senderWallet.getPublicKey());
    BigDecimal amount = transaction.getInputMap().getAmount();

    if (balance.compareTo(amount) < 0) {
      throw new AmountExceedsBalanceException(amount);
    }

    this.output.put(senderWallet.getPublicKey(), balance.subtract(amount));

    for (Map.Entry<String, BigDecimal> entryOutputMap : transaction.getOutputMap().entrySet()) {
      if (entryOutputMap.getKey().equals(senderWallet.getPublicKey())) {
        continue;
      }
      this.output.computeIfPresent(entryOutputMap.getKey(), (key, value) -> value.add(entryOutputMap.getValue()));
      this.output.computeIfAbsent(entryOutputMap.getKey(), key -> entryOutputMap.getValue());
    }

    this.input = new TransactionInput(this, transaction.getInputMap().amount.add(this.input.amount));
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

  @Getter
  public static class TransactionInput {
    private final LocalDateTime timestamp;
    private final BigDecimal amount;
    private final String address;
    private final String signature;

    @SneakyThrows
    public TransactionInput(Transaction transaction, BigDecimal amount) {
      this.timestamp = LocalDateTime.now();
      Wallet senderWallet = transaction.senderWallet;
      this.amount = amount;
      this.address = senderWallet.getPublicKey();
      this.signature = senderWallet.sign(transaction.getOutputMapAsString());
    }
  }
}
