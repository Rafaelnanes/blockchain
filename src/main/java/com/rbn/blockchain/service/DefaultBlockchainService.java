package com.rbn.blockchain.service;

import com.rbn.blockchain.exception.NotFoundException;
import com.rbn.blockchain.model.Block;
import com.rbn.blockchain.model.Blockchain;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Getter
@Service
public class DefaultBlockchainService {

  private final Blockchain blockchain = new Blockchain();

  @Autowired
  private DefaultNodeService broadcastService;

  private String currentDataBeingMined = "";

  @PostConstruct
  public void init() {
    updateBlockchain();
  }

  public Block getBlock(String hash) throws NotFoundException {
    log.info("Querying block hash: {}", hash);
    return blockchain.getChain()
                     .stream()
                     .filter(x -> x.getHash().equals(hash))
                     .findAny()
                     .orElseThrow(NotFoundException::new);
  }

  public Blockchain consensus(Blockchain blockchain) {
    return this.blockchain.replaceChain(blockchain);
  }

  public void add(Block block) {
    log.info("Adding block: {}", block);
    blockchain.addBlock(block);
    log.info("Added block: {}", block);
  }

  public Block mineAndBroadcast(String data) {
    log.info("Broadcasting mining");
    broadcastService.mine(data);
    return mine(data);
  }

  public Block mine(String data) {
    if (currentDataBeingMined.equals(data)) {
      log.info("This data has been being mined");
      return null;
    }
    currentDataBeingMined = data;
    log.info("Mining block");
    Block lastBlock = blockchain.getLastBlock();
    Block blockMined = Block.mine(lastBlock.getHash(),
        data,
        lastBlock.getDifficulty());

    log.info("Broadcasting mined block");
    broadcastService.newBlockBroadcast(blockMined);
    currentDataBeingMined = "";
    log.info("Block mined");
    return blockMined;
  }

  private void updateBlockchain() {
    log.info("Updating blockchain");
    broadcastService.updateBlockchain().forEach(this.blockchain::replaceChain);
    log.info("Updated blockchain");
  }

}
