package com.rbn.blockchain.util;


import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class HashTest {
  public static void main(String[] args) {
    System.out.println("Java Version: " + getJavaVersion());
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
      ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256k1");
      //      SecureRandom secureRandom = new SecureRandom();
      //      kpg.initialize(ecsp, secureRandom);
      kpg.initialize(ecsp);
      KeyPair kp = kpg.genKeyPair();
      PrivateKey privKey = kp.getPrivate();
      PublicKey pubKey = kp.getPublic();
      System.out.println(Hex.encodeHexString(pubKey.getEncoded()));
      System.out.println(Hex.encodeHexString(privKey.getEncoded()));
      //System.out.println(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privKey.getEncoded(), privKey.getAlgorithm());
      PrivateKey anotherPrivateKey = KeyFactory.getInstance("EC").generatePrivate(pkcs8EncodedKeySpec);
      System.out.println("Retrieve privateKey: " + anotherPrivateKey.equals(privKey));
      Signature signature = Signature.getInstance("SHA256withECDSA");
      signature.initSign(privKey);
      signature.update("aaa".getBytes(StandardCharsets.UTF_8));
      byte[] sign = signature.sign();
      String hexString = Hex.encodeHexString(Base64.getEncoder().encodeToString(sign).getBytes());
      System.out.println("Signed: " + hexString);

      byte[] signDecoded = Base64.getDecoder().decode(Hex.decodeHex(hexString));
      System.out.println("Hex decode: " + signDecoded);
      signature.initVerify(pubKey);

      signature.update("aaa".getBytes(StandardCharsets.UTF_8));
      boolean isVerified = signature.verify(signDecoded);
      System.out.println("isVerified: " + isVerified);

    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  public static String getJavaVersion() {
    String[] javaVersionElements = System.getProperty("java.runtime.version").split("\\.|_|-b");
    String main = "", major = "", minor = "", update = "", build = "";
    int elementsSize = javaVersionElements.length;
    if (elementsSize > 0) {
      main = javaVersionElements[0];
    }
    if (elementsSize > 1) {
      major = javaVersionElements[1];
    }
    if (elementsSize > 2) {
      minor = javaVersionElements[2];
    }
    if (elementsSize > 3) {
      update = javaVersionElements[3];
    }
    if (elementsSize > 4) {
      build = javaVersionElements[4];
    }
    return "main: " + main + " major: " + major + " minor: " + minor + " update: " + update + " build: " + build;
  }
}
