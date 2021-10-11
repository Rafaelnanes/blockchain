package test.com.rbn.blockchain;

import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.TransactionPool;
import com.rbn.blockchain.model.wallet.Transaction;
import com.rbn.blockchain.model.wallet.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionPoolTests {

  @Test
  void add_one_transaction() {
    // given
    Blockchain blockchain = new Blockchain();
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));

    // when
    var transaction = senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(10));
    TransactionPool transactionPool = blockchain.getTransactionPool();
    transactionPool.setTransaction(transaction);

    // then
    Assertions.assertEquals(1, transactionPool.getTransactions().size());
    Optional<Transaction> existingTransaction = transactionPool.getExistingTransaction(transaction);
    Assertions.assertTrue(existingTransaction.isPresent());
    Assertions.assertEquals(transaction, existingTransaction.get());
    Assertions.assertEquals(new BigDecimal(40), senderWallet.getBalance());
  }

  @Test
  void add_two_transactions_same_wallet() {
    // given
    Blockchain blockchain = new Blockchain();
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));
    var transaction = senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(10));

    // when
    TransactionPool transactionPool = blockchain.getTransactionPool();
    transactionPool.setTransaction(transaction);

    // then
    Assertions.assertEquals(1, transactionPool.getTransactions().size());
    Optional<Transaction> existingTransaction = transactionPool.getExistingTransaction(transaction);
    Assertions.assertTrue(existingTransaction.isPresent());
    Assertions.assertEquals(transaction.getId(), existingTransaction.get().getId());
  }

  @Test
  void update_transaction() {
    // given
    Blockchain blockchain = new Blockchain();
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));
    var recipient2Wallet = new Wallet(new BigDecimal(30));
    var transaction = senderWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(10));
    var transaction2 = senderWallet.createTransaction(recipient2Wallet.getPublicKey(), new BigDecimal(10));
    var transaction3 = senderWallet.createTransaction(recipient2Wallet.getPublicKey(), new BigDecimal(5));
    // when
    TransactionPool transactionPool = blockchain.getTransactionPool();
    transactionPool.setTransaction(transaction);
    transactionPool.setTransaction(transaction2);
    transactionPool.setTransaction(transaction3);

    // then
    Assertions.assertEquals(1, transactionPool.getTransactions().size());
    Optional<Transaction> existingTransaction = transactionPool.getExistingTransaction(transaction);
    Assertions.assertTrue(existingTransaction.isPresent());
    Transaction finalTransaction = existingTransaction.get();

    // assert inputMap
    Assertions.assertEquals(new BigDecimal(25), finalTransaction.getInputMap().getAmount());
    String senderWalletPublicKey = senderWallet.getPublicKey();
    Assertions.assertEquals(senderWalletPublicKey, finalTransaction.getInputMap().getAddress());
    Assertions.assertTrue(
        Wallet.verify(senderWalletPublicKey,
            finalTransaction.getOutputMapAsString(),
            finalTransaction.getInputMap().getSignature()));

    // assert outputMap
    Assertions.assertEquals(new BigDecimal(25), finalTransaction.getOutputMap().get(senderWalletPublicKey));
    Assertions.assertEquals(new BigDecimal(10), finalTransaction.getOutputMap().get(recipientWallet.getPublicKey()));
    Assertions.assertEquals(new BigDecimal(15), finalTransaction.getOutputMap().get(recipient2Wallet.getPublicKey()));

  }

}
