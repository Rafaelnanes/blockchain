package com.rbn.blockchain;

import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Getter
public class Block {

  private final LocalDateTime timestamp;
  private final String hash;
  private final String lastHash;
  private final String data;
  private final long difficulty;
  private long nonce;

  public Block(String lastHash, String data) {
    this(lastHash, data, 1);
  }

  public Block(String lastHash, String data, long difficulty) {
    this.timestamp = LocalDateTime.now();
    String reduce = Stream.of(String.valueOf(difficulty),
                              this.timestamp.toString(),
                              data,
                              lastHash)
                          .reduce("", (a, b) -> String.format("%s %s", a, b));
    this.hash = DigestUtils.sha256Hex(reduce);
    this.lastHash = lastHash;
    this.data = data;
    this.difficulty = difficulty;
  }

  public static Block getGenesisBlock() {
    return new Block("lastGenesisHash", "genesisData", -1);
  }

  public Block mine() {
    String result = "";
    do {

    } while (result.startsWith("000"));
    //    return new Block(this.getHash(), 0, data);
    return this;
  }

}
