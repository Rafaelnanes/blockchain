package test.com.rbn.blockchain;

import com.rbn.blockchain.exception.AmountExceedsBalanceException;
import com.rbn.blockchain.model.wallet.Transaction;
import com.rbn.blockchain.model.wallet.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionTests {

  @Test
  void valid_transaction() {
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));
    var transaction = senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(10));
    Map<String, BigDecimal> outputMap = transaction.getOutputMap();
    assertEquals(new BigDecimal(40), outputMap.get(senderWallet.getPublicKey()));
    assertEquals(new BigDecimal(10), outputMap.get(recipientWallet.getPublicKey()));
    assertNotNull(transaction.getId());
    assertTrue(Transaction.isValid(transaction));
  }

  @Test
  @DisplayName("Different transactions using the same value, but with different signs and same balance")
  void different_transactions_and_different_signs() {
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));
    var transaction1 = senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(10));
    var transaction2 = senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(10));
    Map<String, BigDecimal> outputMap = transaction1.getOutputMap();
    Map<String, BigDecimal> outputMap2 = transaction2.getOutputMap();
    assertEquals(new BigDecimal(40), outputMap.get(senderWallet.getPublicKey()));
    assertEquals(new BigDecimal(40), outputMap2.get(senderWallet.getPublicKey()));
    Assertions.assertNotEquals(transaction1.getInputMap().getSignature(), transaction2.getInputMap().getSignature());
  }

  @Test
  void cannot_change_transaction_data() {
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));
    var transaction = senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(10));
    Map<String, BigDecimal> outputMap = transaction.getOutputMap();
    Assertions.assertThrows(UnsupportedOperationException.class,
        () -> outputMap.put(senderWallet.getPublicKey(), new BigDecimal(1)));
  }

  @Test
  void amount_exceeds_balance() {
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));
    Assertions.assertThrows(AmountExceedsBalanceException.class,
        () -> senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal("50.1")));
  }

}
