package test.com.rbn.blockchain;

import com.rbn.blockchain.model.Block;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockTest {

  @Test
  @DisplayName("has a timestamp, lastHash, hash, and data property")
  void simpleCreation() {
    var block = new Block("lastHash", "data");
    assertTrue(block.getTimestamp() > 0);
    assertNull(block.getHash());
    assertEquals("lastHash", block.getLastHash());
    assertEquals(-1, block.getNonce());
    assertEquals("data", block.getData());
    assertEquals(2, block.getDifficulty());
  }

  @Test
  @DisplayName("getGenesisBlock()")
  void getGenesisBlock() {
    var genesisBlock = Block.getGenesisBlock();
    assertTrue(genesisBlock.getTimestamp() > 0);
    assertEquals("00", genesisBlock.getHash());
    assertEquals("lastGenesisHash", genesisBlock.getLastHash());
    assertEquals(-1, genesisBlock.getNonce());
    assertEquals("genesisData", genesisBlock.getData());
    assertEquals(-1, genesisBlock.getDifficulty());
  }

  @Test
  @DisplayName("mineBlock()")
  void mineBlock() {
    long now = System.currentTimeMillis();
    var genesisBlock = Block.getGenesisBlock();
    var block = new Block(genesisBlock.getHash(), "data").mine();
    assertEquals(Block.generateHash(block), block.getHash());
    long time = System.currentTimeMillis() - now;
    assertTrue(time > Block.MINE_RATE);
  }

}
