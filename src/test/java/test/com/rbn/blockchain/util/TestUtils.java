package test.com.rbn.blockchain.util;

import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.wallet.Wallet;

import java.math.BigDecimal;

public class TestUtils {

  public static Wallet satoshiNakamotoWallet(Blockchain blockchain) {
    String publicKey =
        "3056301006072a8648ce3d020106052b8104000a034200046321eefa7bbc34d4b12177ac0720bded7da9a042219b93a8a290d30abdfcaa00f9257d131aae1c11dd28ca369f5e8e3b9ab527570ee8f1637e9a61d3b4638ba9";
    String privateKey =
        "303e020100301006072a8648ce3d020106052b8104000a042730250201010420539a3f3d9a794bc7e810b2097bcbe936ff7a95190fc68f3af8e49b59b05e49f3";
    BigDecimal balance = blockchain.getBalance(publicKey);
    return new Wallet(privateKey, publicKey, blockchain);
  }

}
