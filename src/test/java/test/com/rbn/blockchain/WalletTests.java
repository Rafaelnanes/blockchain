package test.com.rbn.blockchain;

import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.wallet.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WalletTests {

  @Test
  void valid_wallet_verify() {
    var wallet = new Wallet();
    String data = "anyData";
    String signatureEncoded = wallet.sign(data);
    boolean verify = Wallet.verify(wallet.getPublicKey(),
        data,
        signatureEncoded);
    Assertions.assertTrue(verify);
  }

  @Test
  void invalid_wallet_using_wrong_keyPair() {
    String publicKey =
        "3056301006072a8648ce3d020106052b8104000a034200046321eefa7bbc34d4b12177ac0720bded7da9a042219b93a8a290d30abdfcaa00f9257d131aae1c11dd28ca369f5e8e3b9ab527570ee8f1637e9a61d3b4638ba9";
    String anotherPrivateKey = new Wallet().getPrivateKey();
    Wallet wallet = new Wallet(anotherPrivateKey, publicKey, new Blockchain());
    String sign = wallet.sign("aa");
    boolean verify = Wallet.verify(publicKey, "aa", sign);
    Assertions.assertFalse(verify);
  }

  @Test
  @DisplayName("False signature cuz of changed data")
  void changed_data() {
    var wallet = new Wallet();
    String data = "anyData";
    String signatureEncoded = wallet.sign(data);
    boolean verify = Wallet.verify(wallet.getPublicKey(),
        "data2",
        signatureEncoded);
    Assertions.assertFalse(verify);
  }

  @Test
  @DisplayName("False signature cuz of different wallet")
  void invalid_wallet_verify() {
    var wallet = new Wallet();
    var wallet2 = new Wallet();
    String data = "anyData";
    String signatureEncoded = wallet.sign(data);
    boolean verify = Wallet.verify(wallet2.getPublicKey(),
        data,
        signatureEncoded);
    Assertions.assertFalse(verify);
  }

  @Test
  void retrieve_wallet_from_privateKey_encoded() throws Exception {
    var wallet1 = new Wallet();
    String data = "anyData";
    String signedData = wallet1.sign(data);
    Wallet sameWallet1 = new Wallet(wallet1.getPrivateKey(), wallet1.getPublicKey(), new Blockchain());
    Assertions.assertEquals(wallet1.getPublicKey(), sameWallet1.getPublicKey());
    Assertions.assertEquals(wallet1.getPrivateKey(), sameWallet1.getPrivateKey());
    boolean verifyWallet1 = Wallet.verify(wallet1.getPublicKey(), data, signedData);
    Assertions.assertTrue(verifyWallet1);
    boolean verifySameWallet1 = Wallet.verify(sameWallet1.getPublicKey(), data, signedData);
    Assertions.assertTrue(verifySameWallet1);
  }

}
