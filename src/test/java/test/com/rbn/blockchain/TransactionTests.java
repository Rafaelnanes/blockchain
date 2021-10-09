package test.com.rbn.blockchain;

import com.rbn.blockchain.model.Transaction;
import com.rbn.blockchain.model.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

public class TransactionTests {

  @Test
  void simple() {
    var senderWallet = new Wallet(new BigDecimal(50));
    var recipientWallet = new Wallet(new BigDecimal(20));
    var transaction = new Transaction(senderWallet, recipientWallet.getPublicKey(), new BigDecimal(10));
    Map<String, Object> outputMap = transaction.getOutputMap();
    Assertions.assertEquals(new BigDecimal(40), outputMap.get(senderWallet.getPublicKey()));
    Assertions.assertEquals(new BigDecimal(10), outputMap.get(recipientWallet.getPublicKey()));
  }

}
