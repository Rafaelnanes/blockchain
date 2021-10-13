package test.com.rbn.blockchain;

import com.rbn.blockchain.exception.InvalidBlockException;
import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.wallet.Block;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockchainTests {

  @Test
  @DisplayName("create a new block")
  void simple_creation() throws InvalidBlockException {
    //given
    Blockchain blockchain = new Blockchain();
    var lastBlock = blockchain.getLastBlock();

    //when
    Block block = Block.mine(blockchain.getLastBlock().getHash(), new ArrayList<>(), lastBlock.getDifficulty());
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
  void replace_chain() throws InvalidBlockException {
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
  void do_not_replace_chain() throws InvalidBlockException {
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
    Block block = Block.mine(lastBlock.getHash(), new ArrayList<>(), lastBlock.getDifficulty());
    secondBlockChain.addBlock(block);
    return secondBlockChain;
  }

  private Blockchain getBlockchainWith3Blocks() throws InvalidBlockException {
    Blockchain firstBlockChain = new Blockchain();
    Block firstBlock = firstBlockChain.getLastBlock();
    Block block = Block.mine(firstBlock.getHash(), new ArrayList<>(), firstBlock.getDifficulty());
    firstBlockChain.addBlock(block);
    Block block2 = Block.mine(block.getHash(), new ArrayList<>(), block.getDifficulty());
    firstBlockChain.addBlock(block2);
    return firstBlockChain;
  }

}
