package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public class InvalidWalletException extends GenericException {

  public InvalidWalletException() {
    super("Invalid wallet");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

}
