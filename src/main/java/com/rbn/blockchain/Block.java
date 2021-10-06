package com.rbn.blockchain;

import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Getter
public class Block {

  private final LocalDateTime timestamp;
  private final String lastHash;
  private final String data;
  private final long difficulty;
  private long nonce = -1;
  private String hash;

  public Block(String lastHash, String data) {
    this(lastHash, data, 1);
  }

  public Block(String lastHash, String data, long difficulty) {
    this.timestamp = LocalDateTime.now();
    this.lastHash = lastHash;
    this.data = data;
    this.difficulty = difficulty;
  }

  private Block(String lastHash, String data, long difficulty, String hash) {
    this(lastHash, data, difficulty);
    this.hash = hash;
  }

  public static Block getGenesisBlock() {
    return new Block("lastGenesisHash", "genesisData", -1, "00");
  }

  public static String generateHash(Block block) {
    return DigestUtils.sha256Hex(Stream.of(block.getTimestamp().toString(),
                                           block.getLastHash(),
                                           block.getData(),
                                           String.valueOf(block.getDifficulty()),
                                           String.valueOf(block.getNonce()))
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

  public Block mine() {
    String finalHash = null;
    do {
      nonce++;
      finalHash = generateHash(this);
    } while (!finalHash.startsWith("000"));
    this.hash = finalHash;
    return this;
  }

}
