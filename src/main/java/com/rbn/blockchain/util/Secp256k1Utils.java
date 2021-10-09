package com.rbn.blockchain.util;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;

@Slf4j
public class Secp256k1Utils {

  public static KeyPair getKeyPair() {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
      ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256k1");
      kpg.initialize(ecsp);
      return kpg.genKeyPair();
    } catch (Exception e) {
      log.error("Error generating keyPair", e);
      throw new RuntimeException(e);
    }
  }

}
