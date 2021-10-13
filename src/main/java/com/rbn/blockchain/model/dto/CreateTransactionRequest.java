package com.rbn.blockchain.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {

  private WalletRequest senderWallet;

  private String receiverAddress;

  private BigDecimal amount;

}
