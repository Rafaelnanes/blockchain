package test.com.rbn.blockchain;

import com.rbn.blockchain.exception.InvalidTransactionException;
import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.dto.CreateTransactionRequest;
import com.rbn.blockchain.model.dto.WalletRequest;
import com.rbn.blockchain.model.wallet.Transaction;
import com.rbn.blockchain.model.wallet.Wallet;
import com.rbn.blockchain.service.DefaultBlockchainService;
import com.rbn.blockchain.service.DefaultNodeService;
import com.rbn.blockchain.service.DefaultTransactionService;
import com.rbn.blockchain.service.DefaultWalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.com.rbn.blockchain.util.TestUtils;

import java.math.BigDecimal;
import java.util.Optional;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class TransactionPoolTests {

  private DefaultTransactionService transactionService;

  private DefaultBlockchainService blockchainService;

  @BeforeEach
  void beforeEach() {
    DefaultNodeService mock = Mockito.mock(DefaultNodeService.class);
    Mockito.when(mock.getNodeWallet()).thenReturn(new Wallet());
    blockchainService = new DefaultBlockchainService(mock);
    var walletService = new DefaultWalletService(blockchainService);
    transactionService = new DefaultTransactionService(walletService, blockchainService);
  }

  @Test
  void add_one_transaction() {
    // given
    Blockchain blockchain = blockchainService.getBlockchain();
    var senderWallet = TestUtils.satoshiNakamotoWallet(blockchain);
    var recipientWallet = new Wallet();
    CreateTransactionRequest createTransactionRequest = createInput(senderWallet, recipientWallet);

    // when
    var transaction = transactionService.create(createTransactionRequest);
    var transactionPool = blockchain.getTransactionPool();
    senderWallet.updateBalance(blockchain);

    // then
    Assertions.assertEquals(1, transactionPool.getTransactions().size());
    Optional<Transaction> existingTransaction = transactionPool.getExistingTransaction(transaction);
    Assertions.assertTrue(existingTransaction.isPresent());
    Assertions.assertEquals(transaction.getId(), existingTransaction.get().getId());
    Assertions.assertEquals(new BigDecimal(990), senderWallet.getBalance());
  }

  @Test
  void add_two_transactions_same_wallet() {
    // given
    Blockchain blockchain = blockchainService.getBlockchain();
    var senderWallet = TestUtils.satoshiNakamotoWallet(blockchain);
    var recipientWallet = new Wallet();
    CreateTransactionRequest createTransactionRequest = createInput(senderWallet, recipientWallet);

    // when
    var transactionOne = transactionService.create(createTransactionRequest);
    var transaction = transactionService.create(createTransactionRequest);
    var transactionPool = blockchain.getTransactionPool();
    senderWallet.updateBalance(blockchain);

    // then
    Assertions.assertEquals(1, transactionPool.getTransactions().size());
    Optional<Transaction> existingTransaction = transactionPool.getExistingTransaction(transaction);
    Assertions.assertTrue(existingTransaction.isPresent());
    Assertions.assertEquals(transactionOne.getId(), existingTransaction.get().getId());
  }

  @Test
  @DisplayName("Using wrong publicKey in sender wallet")
  void invalid_transaction() {
    // given
    Blockchain blockchain = blockchainService.getBlockchain();
    var senderWallet = TestUtils.satoshiNakamotoWallet(blockchain);
    var secondWallet = new Wallet();
    CreateTransactionRequest createTransactionRequest = createInput(senderWallet, secondWallet);

    // when
    transactionService.create(createTransactionRequest);

    // a wallet with different KeyPair
    var mixWallet = new Wallet(senderWallet.getPrivateKey(), secondWallet.getPublicKey(), blockchain);
    CreateTransactionRequest createTransactionRequest2 = createInput(mixWallet, secondWallet);

    // when
    Assertions.assertThrows(InvalidTransactionException.class,
        () -> transactionService.create(createTransactionRequest2));
  }

  @Test
  void update_transaction() {
    // given
    Blockchain blockchain = blockchainService.getBlockchain();
    var senderWallet = TestUtils.satoshiNakamotoWallet(blockchain);
    var recipientWallet = new Wallet();
    var recipient2Wallet = new Wallet();
    // when
    transactionService.create(createInput(senderWallet, recipientWallet));
    transactionService.create(createInput(senderWallet, recipient2Wallet));
    transactionService.create(createInput(senderWallet, recipient2Wallet));

    // then
    var transactionPool = blockchain.getTransactionPool();
    Assertions.assertEquals(1, transactionPool.getTransactions().size());

    // assert inputMap
    var finalTransaction = blockchain.getTransactionPool().getTransactions().get(0);
    Assertions.assertEquals(new BigDecimal(30), finalTransaction.getInputMap().getAmount());
    String senderWalletPublicKey = senderWallet.getPublicKey();
    Assertions.assertEquals(senderWalletPublicKey, finalTransaction.getInputMap().getAddress());
    Assertions.assertTrue(
        Wallet.verify(senderWalletPublicKey,
            finalTransaction.getOutputMapAsString(),
            finalTransaction.getInputMap().getSignature()));

    // assert outputMap
    Assertions.assertEquals(new BigDecimal(970), finalTransaction.getOutputMap().get(senderWalletPublicKey));
    Assertions.assertEquals(new BigDecimal(10), finalTransaction.getOutputMap().get(recipientWallet.getPublicKey()));
    Assertions.assertEquals(new BigDecimal(20), finalTransaction.getOutputMap().get(recipient2Wallet.getPublicKey()));

  }


  private CreateTransactionRequest createInput(Wallet senderWallet,
                                               Wallet secondWallet) {
    CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
    WalletRequest senderWalletRequest = new WalletRequest(senderWallet.getPublicKey(), senderWallet.getPrivateKey());
    createTransactionRequest.setSenderWallet(senderWalletRequest);
    createTransactionRequest.setReceiverAddress(secondWallet.getPublicKey());
    createTransactionRequest.setAmount(new BigDecimal(10));
    return createTransactionRequest;
  }

}
