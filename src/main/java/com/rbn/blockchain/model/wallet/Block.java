package com.rbn.blockchain.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbn.blockchain.util.Utils;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class Block {

  public static final long MINE_RATE = 1000;
  private static final int DEFAULT_DIFFICULTY = 2;
  private final String lastHash;
  private final List<Transaction> data;
  private final int difficulty;
  @JsonIgnore
  private final long timestamp;
  private final long effortTime;
  private final long nonce;
  private final String hash;

  private Block(String lastHash,
                List<Transaction> data,
                int difficulty,
                long nonce,
                String hash,
                long effortTime,
                long timestamp) {
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
    var wallet = new Wallet();
    var transaction = wallet.createGenesisTransaction(genesisPublicKey, new BigDecimal(1000));
    List<Transaction> list = new ArrayList<>();
    list.add(transaction);
    return new Block("lastGenesisHash",
        list,
        DEFAULT_DIFFICULTY,
        -1,
        "d2b402d8ef34562e8c1391dd5cf0a0da1e902642a23965440953bbe4762b474e",
        0,
        System.currentTimeMillis());
  }

  public static Block mine(String lastHash, List<Transaction> data, int difficulty) {
    String finalHash;
    long nonce = 0;
    long effortTime;
    long initialTime = System.currentTimeMillis();
    difficulty++;
    int maxDifficulty = 60;
    String parsedData = Utils.convertToString(data);
    do {
      nonce++;
      effortTime = System.currentTimeMillis() - initialTime;
      if (difficulty > maxDifficulty) {
        difficulty = maxDifficulty;
      }
      if (effortTime >= MINE_RATE && difficulty > 1) {
        difficulty--;
      } else {
        difficulty++;
      }
      finalHash = Utils.generateHash(lastHash, parsedData, String.valueOf(nonce));
    } while (!Objects.requireNonNull(finalHash).matches(String.format("^\\d{%d}\\w*$", difficulty)));
    long timestamp = System.currentTimeMillis();
    return new Block(lastHash, data, difficulty, nonce, finalHash, effortTime, timestamp);
  }

  public LocalDateTime getReceivedTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
  }

  public String getParsedData() {
    return Utils.convertToString(data);
  }

}
