package com.rbn.blockchain.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateWalletRequest {

  private BigDecimal initialAmount;

}
