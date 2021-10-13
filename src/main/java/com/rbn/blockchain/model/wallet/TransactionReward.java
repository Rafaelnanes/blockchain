package com.rbn.blockchain.model.wallet;

import java.math.BigDecimal;

public class TransactionReward extends Transaction {

  public TransactionReward(String recipientPublicKey, BigDecimal amount) {
    super(recipientPublicKey, amount);
  }

}
