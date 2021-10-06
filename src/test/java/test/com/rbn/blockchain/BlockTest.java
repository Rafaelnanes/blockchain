package test.com.rbn.blockchain;

import com.rbn.blockchain.Block;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BlockTest {

  @Test
  @DisplayName("has a timestamp, lastHash, hash, and data property")
  void simpleCreation() {
    var block = new Block("lastHash", "data");
    assertNotNull(block.getTimestamp());
    assertNotNull(block.getHash());
    assertEquals("lastHash", block.getLastHash());
    assertEquals(0, block.getNonce());
    assertEquals("data", block.getData());
    assertEquals(1, block.getDifficulty());
  }

  @Test
  @DisplayName("getGenesisBlock()")
  void getGenesisBlock() {
    var genesisBlock = Block.getGenesisBlock();
    assertNotNull(genesisBlock.getTimestamp());
    assertNotNull(genesisBlock.getHash());
    assertEquals("lastGenesisHash", genesisBlock.getLastHash());
    assertEquals(0, genesisBlock.getNonce());
    assertEquals("genesisData", genesisBlock.getData());
    assertEquals(-1, genesisBlock.getDifficulty());
  }

  @Test
  @DisplayName("mineBlock()")
  void mineBlock() {
    var genesisBlock = Block.getGenesisBlock();
    var block = new Block(genesisBlock.getHash(), "data").mine();
    assertNotNull(genesisBlock.getTimestamp());
    assertNotNull(block.getHash());
    assertEquals(genesisBlock.getHash(), block.getLastHash());
    assertEquals(0, block.getNonce());
    assertEquals("data", block.getData());
  }

}
