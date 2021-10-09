package test.com.rbn.blockchain;

import com.rbn.blockchain.exception.InvalidBlockException;
import com.rbn.blockchain.model.Block;
import com.rbn.blockchain.model.Blockchain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockchainTests {

  @Test
  @DisplayName("create a new block")
  void simpleCreation() throws InvalidBlockException {
    //given
    Blockchain blockchain = new Blockchain();
    var lastBlock = blockchain.getLastBlock();

    //when
    Block block = Block.mine(blockchain.getLastBlock().getHash(), "data", lastBlock.getDifficulty());
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
  void replaceChain() throws InvalidBlockException {
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
  @DisplayName("cannot replace smaller chain")
  void doNotReplaceChain() throws InvalidBlockException {
    //given
    Blockchain firstBlockChain = getBlockChainWith2Blocks();
    Blockchain secondBlockChain = getBlockchainWith3Blocks();

    //when
    secondBlockChain.replaceChain(firstBlockChain);

    // then
    assertEquals(3, secondBlockChain.getChain().size());
    assertTrue(secondBlockChain.isChainValid());
  }

  private Blockchain getBlockChainWith2Blocks() throws InvalidBlockException {
    Blockchain secondBlockChain = new Blockchain();
    Block lastBlock = secondBlockChain.getLastBlock();
    Block block = Block.mine(lastBlock.getHash(), "data", lastBlock.getDifficulty());
    secondBlockChain.addBlock(block);
    return secondBlockChain;
  }

  private Blockchain getBlockchainWith3Blocks() throws InvalidBlockException {
    Blockchain firstBlockChain = new Blockchain();
    Block firstBlock = firstBlockChain.getLastBlock();
    Block block = Block.mine(firstBlock.getHash(), "data", firstBlock.getDifficulty());
    firstBlockChain.addBlock(block);
    Block block2 = Block.mine(block.getHash(), "data2", block.getDifficulty());
    firstBlockChain.addBlock(block2);
    return firstBlockChain;
  }

}
