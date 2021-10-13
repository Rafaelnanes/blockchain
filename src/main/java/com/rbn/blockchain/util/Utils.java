package com.rbn.blockchain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
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
  public static PublicKey retrievePublicKey(String publicKeyHex) {
    byte[] decodeHex = Hex.decodeHex(publicKeyHex);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeHex, ALGORITHM);
    return KeyFactory.getInstance(ALGORITHM).generatePublic(keySpec);
  }

  @SneakyThrows
  public static PublicKey retrieveFromPrivatecKey(String privateKeyHex) {
    byte[] decodeHex = Hex.decodeHex(privateKeyHex);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeHex, ALGORITHM);
    PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(keySpec);
    return null;
  }

  public static String generateHash(String... values) {
    return DigestUtils.sha256Hex(Stream.of(values)
                                       .reduce("", (a, b) -> String.format("%s %s", a, b)));
  }

  @SneakyThrows
  public static String convertToString(Object data) {
    return getObjectMapper().writeValueAsString(data);
  }

  public static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    //    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    //    objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }

}
