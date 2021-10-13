package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class AmountExceedsBalanceException extends GenericException {

  public AmountExceedsBalanceException(BigDecimal amount) {
    super(String.format("The amount %s exceeds the balance", amount.toString()));
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
