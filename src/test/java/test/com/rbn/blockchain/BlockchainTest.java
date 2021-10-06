package test.com.rbn.blockchain;

import com.rbn.blockchain.Block;
import com.rbn.blockchain.Blockchain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockchainTest {

  @Test
  @DisplayName("create a new block")
  void simpleCreation() {
    //given
    Blockchain blockchain = new Blockchain();
    var lastBlock = blockchain.getLastBlock();
    var block = new Block(lastBlock.getHash(), "nonce", "data");

    //when
    blockchain.addBlock(block);

    // then
    var lastBlockAdded = blockchain.getLastBlock();
    var genesisBlock = blockchain.getChain().get(0);
    assertEquals(block.getHash(), lastBlockAdded.getHash());
    assertEquals(genesisBlock.getHash(), lastBlockAdded.getLastHash());
    assertEquals(block.getNonce(), block.getNonce());
    assertEquals(block.getData(), block.getData());
    assertEquals(2, blockchain.getChain().size());
    assertTrue(blockchain.isChainValid());
  }

  @Test
  @DisplayName("replace chain")
  void replaceChain() {
    //given
    Blockchain firstBlockChain = getBlockchainWith3Blocks();
    Blockchain secondBlockChain = getBlockChainWith2Blocks();

    //when
    secondBlockChain.replaceChain(firstBlockChain);

    // then
    assertEquals(3, secondBlockChain.getChain().size());
    assertTrue(secondBlockChain.isChainValid());
  }

  @Test
  @DisplayName("cannot replace invalid chain")
  void cannot_replace_invalid_chain() {
    //given
    Blockchain firstBlockChain = getBlockchainWith3BlocksInvalid();
    Blockchain secondBlockChain = getBlockChainWith2Blocks();

    //when
    secondBlockChain.replaceChain(firstBlockChain);

    // then
    assertEquals(2, secondBlockChain.getChain().size());
    assertTrue(secondBlockChain.isChainValid());
  }

  @Test
  @DisplayName("cannot replace smaller chain")
  void doNotReplaceChain() {
    //given
    Blockchain firstBlockChain = getBlockChainWith2Blocks();
    Blockchain secondBlockChain = getBlockchainWith3Blocks();

    //when
    secondBlockChain.replaceChain(firstBlockChain);

    // then
    assertEquals(3, secondBlockChain.getChain().size());
    assertTrue(secondBlockChain.isChainValid());
  }

  private Blockchain getBlockChainWith2Blocks() {
    Blockchain secondBlockChain = new Blockchain();
    secondBlockChain.addBlock(new Block(secondBlockChain.getLastBlock().getHash(), "nonce", "data"));
    return secondBlockChain;
  }

  private Blockchain getBlockchainWith3Blocks() {
    Blockchain firstBlockChain = new Blockchain();
    var lastBlock = firstBlockChain.getLastBlock();
    Block secondBlock = new Block(lastBlock.getHash(), "nonce", "data");
    firstBlockChain.addBlock(secondBlock);
    firstBlockChain.addBlock(new Block(secondBlock.getHash(), "nonce", "data"));
    return firstBlockChain;
  }

  private Blockchain getBlockchainWith3BlocksInvalid() {
    Blockchain firstBlockChain = new Blockchain();
    var lastBlock = firstBlockChain.getLastBlock();
    Block secondBlock = new Block(lastBlock.getHash(), "nonce", "data");
    firstBlockChain.addBlock(secondBlock);
    firstBlockChain.addBlock(new Block(lastBlock.getHash(), "nonce", "data"));
    return firstBlockChain;
  }

}
