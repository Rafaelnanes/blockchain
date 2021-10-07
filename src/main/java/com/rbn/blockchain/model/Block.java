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

  public static final long MINE_RATE = 4000;
  private final String lastHash;
  private final String data;
  private final int difficulty;
  @JsonIgnore
  private long timestamp;
  private long nonce;
  private String hash;
  private long effort = 0;

  public Block(String lastHash, String data) {
    this(lastHash, data, 2);
  }

  public Block(String lastHash, String data, int difficulty) {
    this.timestamp = System.currentTimeMillis();
    this.lastHash = lastHash;
    this.data = data;
    this.difficulty = difficulty;
    this.nonce = -1;
  }

  private Block(long timestamp,
                String lastHash,
                String data,
                int difficulty,
                long nonce,
                String hash) {
    this(lastHash, data, difficulty);
    this.hash = hash;
    this.nonce = nonce;
    this.timestamp = timestamp;
  }

  public static Block getGenesisBlock() {
    return new Block(System.currentTimeMillis(), "lastGenesisHash", "genesisData", -1, -1, "00");
  }

  public static String generateHash(Block block) {
    return DigestUtils.sha256Hex(Stream.of(block.getLastHash(),
                                           block.getData(),
                                           String.valueOf(block.getNonce()))
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

  public static String generateHashToBinaryString(Block block) {
    String s = generateHash(block);
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

  public Block mine() {
    String finalHash = null;
    do {
      nonce++;
      effort = System.currentTimeMillis() - this.timestamp;
      finalHash = generateHash(this);
    } while (!Objects.requireNonNull(finalHash).matches(String.format("^\\d{%d}\\w*$", difficulty)));

    this.hash = generateHash(this);
    return this;
  }

  public LocalDateTime getReceivedTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
  }

}
