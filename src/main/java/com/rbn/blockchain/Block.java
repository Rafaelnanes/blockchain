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
  private final String nonce;
  private final String data;

  public Block(String lastHash, String nonce, String data) {
    this.timestamp = LocalDateTime.now();
    String reduce = Stream.of(this.timestamp.toString(),
                              data,
                              lastHash,
                              nonce)
                          .reduce("", (a, b) -> String.format("%s %s", a, b));
    this.hash = DigestUtils.sha256Hex(reduce);
    this.lastHash = lastHash;
    this.nonce = nonce;
    this.data = data;
  }

  public static Block getGenesisBlock() {
    return new Block("lastGenesisHash", "fistNonce", "genesisData");
  }

  public static Block mine(Block lastBlock, String data) {
    return new Block(lastBlock.getHash(), "nonce", data);
  }

}
