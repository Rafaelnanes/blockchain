package test.com.rbn.blockchain;

import com.rbn.blockchain.model.wallet.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class WalletTests {

  @Test
  void valid_wallet_verify() {
    var wallet = new Wallet(new BigDecimal("20"));
    String data = "anyData";
    String signatureEncoded = wallet.sign(data);
    boolean verify = Wallet.verify(wallet.getPublicKey(),
        data,
        signatureEncoded);
    Assertions.assertTrue(verify);
  }

  @Test
  void changed_data() {
    var wallet = new Wallet(new BigDecimal("20"));
    String data = "anyData";
    String signatureEncoded = wallet.sign(data);
    boolean verify = Wallet.verify(wallet.getPublicKey(),
        "data2",
        signatureEncoded);
    Assertions.assertFalse(verify);
  }

  @Test
  void invalid_wallet_verify() {
    var wallet = new Wallet(new BigDecimal("20"));
    var wallet2 = new Wallet(new BigDecimal("20"));
    String data = "anyData";
    String signatureEncoded = wallet.sign(data);
    boolean verify = Wallet.verify(wallet2.getPublicKey(),
        data,
        signatureEncoded);
    Assertions.assertFalse(verify);
  }

}
