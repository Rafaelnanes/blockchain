package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public class InvalidChainException extends GenericException {

  public InvalidChainException() {
    super("Invalid chain");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }

}
