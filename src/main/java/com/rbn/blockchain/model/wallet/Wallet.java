package com.rbn.blockchain.model.wallet;

import com.rbn.blockchain.exception.AmountExceedsBalanceException;
import com.rbn.blockchain.model.Blockchain;
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

  private final String publicKey;
  private final String privateKey;
  private final Signature signature;
  private BigDecimal balance;

  @SneakyThrows
  public Wallet() {
    this.balance = BigDecimal.ZERO;
    KeyPair keyPair = Utils.getKeyPair();
    PublicKey pairPublic = keyPair.getPublic();
    this.publicKey = Hex.encodeHexString(pairPublic.getEncoded());
    PrivateKey pairPrivate = keyPair.getPrivate();
    this.privateKey = Hex.encodeHexString(pairPrivate.getEncoded());
    this.signature = Signature.getInstance(Utils.SIGNATURE_INSTANCE);
    this.signature.initSign(pairPrivate);
  }

  @SneakyThrows
  public Wallet(String privateKey, String publicKey, Blockchain blockchain) {
    PrivateKey pairPrivate = Utils.retrievePrivateKey(privateKey);
    PublicKey pairPublic = Utils.retrievePublicKey(publicKey);
    this.publicKey = Hex.encodeHexString(pairPublic.getEncoded());
    this.privateKey = Hex.encodeHexString(pairPrivate.getEncoded());
    this.signature = Signature.getInstance(Utils.SIGNATURE_INSTANCE);
    this.signature.initSign(pairPrivate);
    this.balance = blockchain.getBalance(publicKey);
  }

  @SneakyThrows
  public static boolean verify(String publicKeyHex, String data, String signatureHex) {
    Signature signature = Signature.getInstance(Utils.SIGNATURE_INSTANCE);

    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Hex.decodeHex(publicKeyHex));

    KeyFactory keyFactory = KeyFactory.getInstance(Utils.ALGORITHM);
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

    signature.initVerify(publicKey);
    signature.update(data.getBytes(StandardCharsets.UTF_8));
    return signature.verify(Base64.getDecoder().decode(Hex.decodeHex(signatureHex)));
  }

  public Transaction createTransaction(String recipientPublicKey, BigDecimal amount) {
    if (!hasBalance(amount)) {
      throw new AmountExceedsBalanceException(amount);
    }
    this.balance = this.balance.subtract(amount);
    return new Transaction(this, recipientPublicKey, amount);
  }

  Transaction createGenesisTransaction(String recipientPublicKey, BigDecimal amount) {
    this.balance = this.balance.subtract(amount);
    return new Transaction(this, recipientPublicKey, amount);
  }

  @SneakyThrows
  public String sign(String data) {
    this.signature.update(data.getBytes(StandardCharsets.UTF_8));
    byte[] sign = this.signature.sign();
    return Hex.encodeHexString(Base64.getEncoder().encodeToString(sign).getBytes());
  }

  public void updateBalance(Blockchain blockchain) {
    this.balance = blockchain.getBalance(publicKey);
  }

  boolean hasBalance(BigDecimal amount) {
    return balance.compareTo(amount) >= 0;
  }

  public BigDecimal getBalance() {
    return this.balance;
  }

  public String getPublicKey() {
    return this.publicKey;
  }

  public String getPrivateKey() {
    return this.privateKey;
  }

}
