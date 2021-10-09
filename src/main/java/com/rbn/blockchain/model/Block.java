package com.rbn.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
@ToString
public class Block {

  public static final long MINE_RATE = 1000;
  private static final int DEFAULT_DIFFICULTY = 2;
  private final String lastHash;
  private final String data;
  private final int difficulty;
  @JsonIgnore
  private final long timestamp;
  private final long effortTime;
  private final long nonce;
  private final String hash;

  private Block(String lastHash,
                String data,
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
    return new Block("lastGenesisHash",
        "genesisData",
        DEFAULT_DIFFICULTY,
        -1,
        "d2b402d8ef34562e8c1391dd5cf0a0da1e902642a23965440953bbe4762b474e",
        0,
        System.currentTimeMillis());
  }


  public static String generateHash(String lastHash, String data, long nonce) {
    return DigestUtils.sha256Hex(Stream.of(lastHash,
                                           data,
                                           String.valueOf(nonce))
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

  public static Block mine(String lastHash, String data, int difficulty) {
    String finalHash;
    long nonce = 0;
    long effortTime;
    long initialTime = System.currentTimeMillis();
    difficulty++;
    int maxDifficulty = 60;
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
      finalHash = generateHash(lastHash, data, nonce);
    } while (!Objects.requireNonNull(finalHash).matches(String.format("^\\d{%d}\\w*$", difficulty)));
    long timestamp = System.currentTimeMillis();
    return new Block(lastHash, data, difficulty, nonce, finalHash, effortTime, timestamp);
  }

  public LocalDateTime getReceivedTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
  }

}
