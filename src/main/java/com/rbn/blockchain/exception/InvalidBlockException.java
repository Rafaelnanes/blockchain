package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidBlockException extends GenericException {

  public InvalidBlockException() {
    super("Invalid block");
  }

}
