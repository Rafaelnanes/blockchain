package com.rbn.blockchain.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;
import java.util.stream.Stream;

@Slf4j
public class Utils {

  public static final String SIGNATURE_INSTANCE = "SHA256withECDSA";
  public static final String KEY_PAIR_INSTANCE = "EC";

  public static KeyPair getKeyPair() {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_PAIR_INSTANCE);
      ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256k1");
      kpg.initialize(ecsp);
      return kpg.genKeyPair();
    } catch (Exception e) {
      log.error("Error generating keyPair", e);
      throw new RuntimeException(e);
    }
  }

  public static String generateHash(String... lastHash) {
    return DigestUtils.sha256Hex(Stream.of(lastHash)
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

}
