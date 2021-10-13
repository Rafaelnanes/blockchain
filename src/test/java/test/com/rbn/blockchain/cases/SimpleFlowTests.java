package test.com.rbn.blockchain.cases;

import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.wallet.Wallet;
import com.rbn.blockchain.service.DefaultBlockchainService;
import com.rbn.blockchain.service.DefaultNodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.com.rbn.blockchain.util.ConfigTests;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = SimpleFlowTests.InternalConfig.class)
@ExtendWith(SpringExtension.class)
public class SimpleFlowTests {

  @Autowired
  private DefaultBlockchainService blockchainService;

  @Test
  void simple() {
    // retrieve satoshi nakamoto's wallet
    Blockchain blockchain = blockchainService.getBlockchain();
    String publicKey =
        "3056301006072a8648ce3d020106052b8104000a034200046321eefa7bbc34d4b12177ac0720bded7da9a042219b93a8a290d30abdfcaa00f9257d131aae1c11dd28ca369f5e8e3b9ab527570ee8f1637e9a61d3b4638ba9";
    String privateKey =
        "303e020100301006072a8648ce3d020106052b8104000a042730250201010420539a3f3d9a794bc7e810b2097bcbe936ff7a95190fc68f3af8e49b59b05e49f3";
    BigDecimal balance = blockchain.getBalance(publicKey);
    var satoshiNakamotoWallet = new Wallet(privateKey, publicKey, blockchain);
    assertNotNull(satoshiNakamotoWallet);
    assertEquals(new BigDecimal(1000), balance);

    // sending money to another wallet
    var recipientWallet = new Wallet();
    var transaction = satoshiNakamotoWallet.createTransaction(recipientWallet.getPublicKey(), new BigDecimal(150));

    // mining
    blockchainService.mine(List.of(transaction));

    // assertions
    satoshiNakamotoWallet.updateBalance(blockchain);
    recipientWallet.updateBalance(blockchain);
    Assertions.assertEquals(new BigDecimal(150), recipientWallet.getBalance());
    Assertions.assertEquals(new BigDecimal(850), satoshiNakamotoWallet.getBalance());
  }

  @Configuration
  @Import(DefaultBlockchainService.class)
  static class InternalConfig extends ConfigTests {

    @Bean
    public DefaultNodeService getDefaultNodeService() {
      return Mockito.mock(DefaultNodeService.class);
    }
  }

}
