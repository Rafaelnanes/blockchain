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
    var block = new Block("lastHash", "nonce", "data");
    assertNotNull(block.getTimestamp());
    assertEquals("3985edc1088558b67b84bc68b5b026590c31652432bc1ca556da7ff69b297c88", block.getHash());
    assertEquals("lastHash", block.getLastHash());
    assertEquals("nonce", block.getNonce());
    assertEquals("data", block.getData());
  }

  @Test
  @DisplayName("getGenesisBlock()")
  void getGenesisBlock() {
    var genesisBlock = Block.getGenesisBlock();
    assertNotNull(genesisBlock.getTimestamp());
    assertEquals("ba15c885714a5df52c90cb291bb9fc02da5301633b31186d8d4b07d801ce06cd", genesisBlock.getHash());
    assertEquals("lastGenesisHash", genesisBlock.getLastHash());
    assertEquals("fistNonce", genesisBlock.getNonce());
    assertEquals("genesisData", genesisBlock.getData());
  }

  @Test
  @DisplayName("mineBlock()")
  void mineBlock() {
    var genesisBlock = Block.getGenesisBlock();
    var block = Block.mine(genesisBlock, "data");
    assertNotNull(genesisBlock.getTimestamp());
    assertEquals("57848a1170a4c81d3c0e6eac0e66813d2de3c7e8066342dc3d3ce60e49a68e7d", block.getHash());
    assertEquals(genesisBlock.getHash(), block.getLastHash());
    assertEquals("nonce", block.getNonce());
    assertEquals("data", block.getData());
  }

}
