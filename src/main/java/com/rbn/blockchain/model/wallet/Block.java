package com.rbn.blockchain.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbn.blockchain.util.Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Block {

  public static final long MINE_RATE = 2000;
  private static final int DEFAULT_DIFFICULTY = 1;
  private final String lastHash;
  private final List<Transaction> data;
  private final int difficulty;
  @JsonIgnore
  private final LocalDateTime timestamp;
  private final long effortTime;
  private final long nonce;
  private final String hash;

  private Block(String lastHash,
                List<Transaction> data,
                int difficulty,
                long nonce,
                String hash,
                long effortTime,
                LocalDateTime timestamp) {
    this.difficulty = difficulty;
    this.data = data;
    this.lastHash = lastHash;
    this.timestamp = timestamp;
    this.hash = hash;
    this.nonce = nonce;
    this.effortTime = effortTime;
  }

  public static Block getGenesisBlock() {
    String genesisPublicKey =
        "3056301006072a8648ce3d020106052b8104000a034200046321eefa7bbc34d4b12177ac0720bded7da9a042219b93a8a290d30abdfcaa00f9257d131aae1c11dd28ca369f5e8e3b9ab527570ee8f1637e9a61d3b4638ba9";
    var transaction = new TransactionReward(genesisPublicKey, new BigDecimal(1000));
    List<Transaction> list = new ArrayList<>();
    list.add(transaction);
    return new Block("lastGenesisHash",
        list,
        DEFAULT_DIFFICULTY,
        -1,
        "d2b402d8ef34562e8c1391dd5cf0a0da1e902642a23965440953bbe4762b474e",
        0,
        LocalDateTime.now());
  }

  public static Block mine(Block lastBlock, List<Transaction> data) {
    String lastHash = lastBlock.getHash();
    int difficulty = lastBlock.getDifficulty() + 1;
    if (lastBlock.getEffortTime() > MINE_RATE) {
      difficulty = lastBlock.getDifficulty() - 1;
    }

    String finalHash;
    long nonce = 0;
    long effortTime;
    long initialTime = System.currentTimeMillis();
    String parsedData = Utils.convertToString(data);
    LocalDateTime now = LocalDateTime.now();
    String currentDate = now.toString();
    do {
      nonce++;
      effortTime = System.currentTimeMillis() - initialTime;
      finalHash = Utils.generateHash(lastHash, parsedData, String.valueOf(nonce), currentDate);
    } while (!finalHash.startsWith("0".repeat(difficulty)));
    return new Block(lastHash, data, difficulty, nonce, finalHash, effortTime, now);
  }

  @JsonIgnore
  public String getParsedData() {
    return Utils.convertToString(data);
  }

}
