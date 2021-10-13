package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public abstract class GenericException extends RuntimeException {

  public GenericException(String message) {
    super(message);
  }

  public abstract HttpStatus getHttpStatus();
}
