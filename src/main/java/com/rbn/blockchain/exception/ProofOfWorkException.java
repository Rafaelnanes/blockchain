package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public class ProofOfWorkException extends GenericException {

  public ProofOfWorkException() {
    super("Invalid proof of work");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }

}
