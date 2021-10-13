package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public class InvalidBlockException extends GenericException {

  public InvalidBlockException() {
    super("Invalid block");
  }


  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }

}
