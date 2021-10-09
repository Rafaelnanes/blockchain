package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidChainException extends GenericException {

  public InvalidChainException() {
    super("Invalid chain");
  }

}
