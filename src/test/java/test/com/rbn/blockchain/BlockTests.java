package test.com.rbn.blockchain;

import com.rbn.blockchain.model.wallet.Block;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockTests {

  @Test
  @DisplayName("has a timestamp, lastHash, hash, and data property")
  void simple_creation() {
    Block genesisBlock = Block.getGenesisBlock();
    var block = Block.mine(genesisBlock, new ArrayList<>());
    assertNotNull(block.getTimestamp());
    assertNotNull(block.getHash());
    assertEquals(genesisBlock.getHash(), block.getLastHash());
    assertTrue(block.getNonce() > 0);
    assertEquals(new ArrayList<>(), block.getData());
    assertTrue(block.getDifficulty() > 0);
  }

  @Test
  @DisplayName("getGenesisBlock()")
  void get_genesis_block() {
    var genesisBlock = Block.getGenesisBlock();
    assertNotNull(genesisBlock.getTimestamp());
    assertEquals("d2b402d8ef34562e8c1391dd5cf0a0da1e902642a23965440953bbe4762b474e", genesisBlock.getHash());
    assertEquals("lastGenesisHash", genesisBlock.getLastHash());
    assertEquals(-1, genesisBlock.getNonce());
    assertEquals(1, genesisBlock.getDifficulty());
    var data = genesisBlock.getData();
    assertNotNull(data);
    var genesisTransaction = data.get(0);
    BigDecimal amount = genesisTransaction.getOutputMap().get(
        "3056301006072a8648ce3d020106052b8104000a034200046321eefa7bbc34d4b12177ac0720bded7da9a042219b93a8a290d30abdfcaa00f9257d131aae1c11dd28ca369f5e8e3b9ab527570ee8f1637e9a61d3b4638ba9");
    assertEquals(new BigDecimal(1000), amount);
  }

}
