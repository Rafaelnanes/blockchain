package com.rbn.blockchain.hash;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;

public class Main {

  public static void main(String[] args) {
    System.out.println("Java Version: " + getJavaVersion());
    try {
      KeyPairGenerator kpg;
      kpg = KeyPairGenerator.getInstance("EC");
      ECGenParameterSpec ecsp;
      ecsp = new ECGenParameterSpec("secp256k1");
      kpg.initialize(ecsp);

      KeyPair kp = kpg.genKeyPair();
      PrivateKey privKey = kp.getPrivate();
      PublicKey pubKey = kp.getPublic();

      System.out.println(privKey.toString());
      System.out.println(pubKey.toString());
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
