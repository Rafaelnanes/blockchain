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

  @Test
  void retrieve_wallet_from_privateKey_encoded() throws Exception {
    var wallet1 = new Wallet(new BigDecimal("20"));
    String data = "anyData";
    String signedData = wallet1.sign(data);
    Wallet sameWallet1 = new Wallet(wallet1.getPrivateKey(), wallet1.getPublicKey());
    Assertions.assertEquals(wallet1.getPublicKey(), sameWallet1.getPublicKey());
    Assertions.assertEquals(wallet1.getPrivateKey(), sameWallet1.getPrivateKey());
    boolean verifyWallet1 = Wallet.verify(wallet1.getPublicKey(), data, signedData);
    Assertions.assertTrue(verifyWallet1);
    boolean verifySameWallet1 = Wallet.verify(sameWallet1.getPublicKey(), data, signedData);
    Assertions.assertTrue(verifySameWallet1);
  }

}
