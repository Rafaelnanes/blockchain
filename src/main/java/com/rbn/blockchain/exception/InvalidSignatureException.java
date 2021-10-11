package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSignatureException extends GenericException {

  public InvalidSignatureException() {
    super("Invalid signature");
  }

}
