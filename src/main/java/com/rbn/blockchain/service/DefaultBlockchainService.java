package com.rbn.blockchain.service;

import com.rbn.blockchain.exception.NotFoundException;
import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.wallet.Block;
import com.rbn.blockchain.model.wallet.Transaction;
import com.rbn.blockchain.model.wallet.TransactionReward;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;

@Slf4j
@Service
public class DefaultBlockchainService {

  private final Blockchain blockchain;

  private final DefaultNodeService broadcastService;

  public DefaultBlockchainService(DefaultNodeService defaultNodeService) {
    this.broadcastService = defaultNodeService;
    this.blockchain = new Blockchain();
  }

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

  public void add(Transaction transaction) {
    log.info("Adding transaction: {}", transaction);
    blockchain.getTransactionPool().setTransaction(transaction);
    log.info("Added transaction: {}", transaction);
  }

  public Block mine() {
    log.info("Mining block");
    var transactions = blockchain.getTransactionPool().getTransactions();
    Block lastBlock = blockchain.getLastBlock();
    Block blockMined = Block.mine(lastBlock.getHash(),
        new ArrayList<>(transactions),
        lastBlock.getDifficulty());

    log.info("Broadcasting mined block");
    broadcastService.newBlockBroadcast(blockMined);
    log.info("Block mined");
    add(blockMined);
    blockchain.getTransactionPool().clear();

    var wallet = broadcastService.getNodeWallet();
    var reward = new TransactionReward(wallet.getPublicKey(), new BigDecimal(500));
    blockchain.getTransactionPool().setTransaction(reward);
    return blockMined;
  }

  private void updateBlockchain() {
    log.info("Updating blockchain");
    broadcastService.updateBlockchain().forEach(this.blockchain::replaceChain);
    log.info("Updated blockchain");
  }

  public Blockchain getBlockchain() {
    return this.blockchain;
  }

}
