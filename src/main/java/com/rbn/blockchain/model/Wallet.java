package com.rbn.blockchain.model;

import com.rbn.blockchain.util.Utils;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Wallet {

  private final BigDecimal balance;
  private final String publicKey;
  private final PrivateKey privateKey;
  private final Signature signature;

  @SneakyThrows
  public Wallet(BigDecimal balance) {
    this.balance = balance;
    KeyPair keyPair = Utils.getKeyPair();
    PublicKey pairPublic = keyPair.getPublic();
    this.publicKey = Hex.encodeHexString(pairPublic.getEncoded());
    this.privateKey = keyPair.getPrivate();
    this.signature = Signature.getInstance(Utils.SIGNATURE_INSTANCE);
    this.signature.initSign(this.privateKey);
  }

  @SneakyThrows
  public static boolean verify(String publicKeyHex, String data, String signatureHex) {
    Signature signature = Signature.getInstance(Utils.SIGNATURE_INSTANCE);

    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Hex.decodeHex(publicKeyHex));

    KeyFactory keyFactory = KeyFactory.getInstance(Utils.KEY_PAIR_INSTANCE);
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

    signature.initVerify(publicKey);
    signature.update(data.getBytes(StandardCharsets.UTF_8));
    return signature.verify(Base64.getDecoder().decode(Hex.decodeHex(signatureHex)));
  }

  @SneakyThrows
  public String sign(String data) {
    this.signature.update(data.getBytes(StandardCharsets.UTF_8));
    byte[] sign = this.signature.sign();
    return Hex.encodeHexString(Base64.getEncoder().encodeToString(sign).getBytes());
  }

  public BigDecimal getBalance() {
    return this.balance;
  }

  public String getPublicKey() {
    return this.publicKey;
  }

}
