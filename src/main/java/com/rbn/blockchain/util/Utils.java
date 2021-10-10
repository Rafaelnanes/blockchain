package com.rbn.blockchain.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Stream;

@Slf4j
public class Utils {

  public static final String SIGNATURE_INSTANCE = "SHA256withECDSA";
  public static final String ALGORITHM = "EC";

  public static KeyPair getKeyPair() {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
      ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256k1");
      kpg.initialize(ecsp);
      return kpg.genKeyPair();
    } catch (Exception e) {
      log.error("Error generating keyPair", e);
      throw new RuntimeException(e);
    }
  }

  @SneakyThrows
  public static PrivateKey retrievePrivateKey(String privateKeyHex) {
    byte[] decodeHex = Hex.decodeHex(privateKeyHex);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeHex, ALGORITHM);
    return KeyFactory.getInstance(ALGORITHM).generatePrivate(keySpec);
  }

  @SneakyThrows
  public static PublicKey retrievePublicKey(String privateKeyHex) {
    byte[] decodeHex = Hex.decodeHex(privateKeyHex);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeHex, ALGORITHM);
    return KeyFactory.getInstance(ALGORITHM).generatePublic(keySpec);
  }

  public static String generateHash(String... lastHash) {
    return DigestUtils.sha256Hex(Stream.of(lastHash)
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

}
