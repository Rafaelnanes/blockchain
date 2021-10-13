package test.com.rbn.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbn.blockchain.model.Block;
import com.rbn.blockchain.util.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockTests {

  @Test
  @DisplayName("has a timestamp, lastHash, hash, and data property")
  void simple_creation() {
    var block = Block.mine("lastHash", "data", 2);
    assertTrue(block.getTimestamp() > 0);
    assertNotNull(block.getHash());
    assertEquals("lastHash", block.getLastHash());
    assertEquals(2, block.getNonce());
    assertEquals("data", block.getData());
    assertTrue(block.getDifficulty() > 0);
  }

  @Test
  @DisplayName("getGenesisBlock()")
  void get_genesis_block() throws JsonProcessingException {
    var genesisBlock = Block.getGenesisBlock();
    assertTrue(genesisBlock.getTimestamp() > 0);
    assertEquals("d2b402d8ef34562e8c1391dd5cf0a0da1e902642a23965440953bbe4762b474e", genesisBlock.getHash());
    assertEquals("lastGenesisHash", genesisBlock.getLastHash());
    assertEquals(-1, genesisBlock.getNonce());
    assertEquals(2, genesisBlock.getDifficulty());
    String data = genesisBlock.getData();
    assertNotNull(data);
    List<?> transactions = Utils.getObjectMapper().readValue(data, List.class);
    assertEquals(1, transactions.size());
    Object genesistTransaction = transactions.get(0);
    Object outputMap = ((Map) genesistTransaction).get("outputMap");
    Integer amount = ((Map<String, Integer>) outputMap).get(
        "3056301006072a8648ce3d020106052b8104000a034200046321eefa7bbc34d4b12177ac0720bded7da9a042219b93a8a290d30abdfcaa00f9257d131aae1c11dd28ca369f5e8e3b9ab527570ee8f1637e9a61d3b4638ba9");
    assertEquals(1000, amount);
  }

  @Test
  @DisplayName("mineBlock()")
  void mine_block() {
    var genesisBlock = Block.getGenesisBlock();
    var block = Block.mine(genesisBlock.getHash(), "data", genesisBlock.getDifficulty());
    String generatedHash = Utils.generateHash(genesisBlock.getHash(),
        "data",
        String.valueOf(block.getNonce()));
    assertEquals(generatedHash, block.getHash());
    assertTrue(block.getEffortTime() > 0);
  }

}
