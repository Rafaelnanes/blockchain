package com.rbn.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  private final String lastHash;
  private final String data;
  private final int difficulty;
  @JsonIgnore
  private final long timestamp;
  private final long effort;
  private final long nonce;
  private final String hash;

  private Block(String lastHash,
                String data,
                int difficulty,
                long nonce,
                String hash,
                long effort) {
    this.difficulty = difficulty;
    this.data = data;
    this.lastHash = lastHash;
    this.timestamp = System.currentTimeMillis();
    this.hash = hash;
    this.nonce = nonce;
    this.effort = effort;
  }

  public static Block getGenesisBlock() {
    return new Block("lastGenesisHash",
        "genesisData",
        -1,
        -1,
        "d2b402d8ef34562e8c1391dd5cf0a0da1e902642a23965440953bbe4762b474e",
        0);
  }

  public static String generateHash(String lastHash, String data, long nonce) {
    return DigestUtils.sha256Hex(Stream.of(lastHash,
                                           data,
                                           String.valueOf(nonce))
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

  public static String generateHashToBinaryString(String lastHash, String data, long nonce) {
    String s = generateHash(lastHash, data, nonce);
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
    String finalHash = null;
    long nonce = 0;
    long effort = 0;
    long initialTime = System.currentTimeMillis();
    int difficulty = 2;
    do {
      nonce++;
      effort = System.currentTimeMillis() - initialTime;
      int maxDifficulty = 60;
      if (difficulty > maxDifficulty) {
        difficulty = maxDifficulty;
      }
      if (effort >= Block.MINE_RATE && difficulty > 1) {
        difficulty--;
      } else {
        difficulty++;
      }
      finalHash = generateHash(lastHash, data, nonce);
    } while (!Objects.requireNonNull(finalHash).matches(String.format("^\\d{%d}\\w*$", difficulty)));

    return new Block(lastHash, data, difficulty, nonce, finalHash, effort);
  }

  public LocalDateTime getReceivedTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
  }

}
