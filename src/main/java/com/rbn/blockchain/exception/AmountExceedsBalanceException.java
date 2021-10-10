package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AmountExceedsBalanceException extends GenericException {

  public AmountExceedsBalanceException(BigDecimal amount) {
    super(String.format("The amount %s exceeds the balance", amount.toString()));
  }

}
