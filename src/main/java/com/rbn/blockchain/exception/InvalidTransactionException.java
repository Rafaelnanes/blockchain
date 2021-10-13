package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public class InvalidTransactionException extends GenericException {

  public InvalidTransactionException() {
    super("Invalid transaction");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

}
