package com.rbn.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbn.blockchain.exception.InvalidBlockException;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Block {

  public static final long MINE_RATE = 1000;
  public static final int DEFAULT_DIFFICULTY = 2;
  private final String lastHash;
  private final String data;
  private final int difficulty;
  @JsonIgnore
  private final long timestamp;
  private final long effortTime;
  private final long nonce;
  private final String hash;

  public Block(String lastHash,
               String hash,
               String data,
               long nonce,
               int difficulty,
               long effortTime,
               long timestamp) {
    this.lastHash = lastHash;
    this.data = data;
    this.difficulty = difficulty;
    this.timestamp = timestamp;
    this.effortTime = effortTime;
    this.nonce = nonce;
    this.hash = hash;
    String generatedHash = generateHash(lastHash, data, nonce, difficulty, effortTime, timestamp);
    if (!generatedHash.equals(hash)) {
      throw new InvalidBlockException();
    }
  }

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
        -1,
        -1,
        "d2b402d8ef34562e8c1391dd5cf0a0da1e902642a23965440953bbe4762b474e",
        0,
        System.currentTimeMillis());
  }

  public static String generateHash(String lastHash,
                                    String data,
                                    long nonce,
                                    int difficulty,
                                    long effortTime,
                                    long timestamp) {
    return DigestUtils.sha256Hex(Stream.of(lastHash,
                                           data,
                                           String.valueOf(nonce),
                                           String.valueOf(difficulty),
                                           String.valueOf(effortTime),
                                           String.valueOf(timestamp))
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

  private static String proofOfWorkHash(String lastHash, String data, long nonce) {
    return DigestUtils.sha256Hex(Stream.of(lastHash,
                                           data,
                                           String.valueOf(nonce))
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

  public static String generateHashToBinaryString(String lastHash, String data, long nonce) {
    String s = proofOfWorkHash(lastHash, data, nonce);
    byte[] bytes = s.getBytes();
    StringBuilder binary = new StringBuilder();
    for (byte b : bytes) {
      int val = b;
      for (int i = 0; i < 8; i++) {
        binary.append((val & 128) == 0 ? 0 : 1);
        val <<= 1;
      }
    }
    return binary.toString();
  }

  public static Block mine(String lastHash, String data) {
    String proofOfWorkHash;
    long nonce = 0;
    long effortTime;
    long initialTime = System.currentTimeMillis();
    int difficulty = DEFAULT_DIFFICULTY;
    do {
      nonce++;
      effortTime = System.currentTimeMillis() - initialTime;
      int maxDifficulty = 60;
      if (difficulty > maxDifficulty) {
        difficulty = maxDifficulty;
      }
      if (effortTime >= Block.MINE_RATE && difficulty > 1) {
        difficulty--;
      } else {
        difficulty++;
      }
      proofOfWorkHash = proofOfWorkHash(lastHash, data, nonce);
    } while (!Objects.requireNonNull(proofOfWorkHash).matches(String.format("^\\d{%d}\\w*$", difficulty)));
    long timestamp = System.currentTimeMillis();
    String finalHash = generateHash(lastHash, data, nonce, difficulty, effortTime, timestamp);
    return new Block(lastHash, data, difficulty, nonce, finalHash, effortTime, timestamp);
  }

  public LocalDateTime getReceivedTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
  }

}
