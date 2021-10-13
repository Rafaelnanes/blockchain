package com.rbn.blockchain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletRequest {

  private String publicKey;

  private String privateKey;

}
