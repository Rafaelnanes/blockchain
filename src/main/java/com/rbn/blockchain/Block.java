package com.rbn.blockchain;

import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Block {

  public static final long MINE_RATE = 1000;
  private final long timestamp;
  private final String lastHash;
  private final String data;
  private int difficulty;
  private long nonce;
  private String hash;

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

  private Block(String lastHash, String data, int difficulty, String hash, long nonce) {
    this(lastHash, data, difficulty);
    this.hash = hash;
    this.nonce = nonce;
  }

  public static Block getGenesisBlock() {
    return new Block("lastGenesisHash", "genesisData", -1, "00", -1);
  }

  public static String generateHash(Block block) {
    return DigestUtils.sha256Hex(Stream.of(String.valueOf(block.getTimestamp()),
                                           block.getLastHash(),
                                           block.getData(),
                                           String.valueOf(block.getDifficulty()),
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
      long spentTime = System.currentTimeMillis() - this.timestamp;

      if (difficulty > 256) {
        difficulty--;
        continue;
      }

      if (spentTime <= MINE_RATE) {
        difficulty++;
      } else {
        difficulty--;
      }

      finalHash = generateHashToBinaryString(this);
    } while (!Objects.requireNonNull(finalHash).startsWith("0".repeat(difficulty)));
    this.hash = generateHash(this);
    return this;
  }

}
