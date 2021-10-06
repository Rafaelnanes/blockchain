package com.rbn.blockchain.hash;

import org.apache.commons.codec.digest.DigestUtils;

public class SHA256Example {

  public static void main(String[] args) {
    System.out.println(DigestUtils.sha256Hex("data"));
  }
}
