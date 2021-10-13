package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public class InvalidSignatureException extends GenericException {

  public InvalidSignatureException() {
    super("Invalid signature");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

}
