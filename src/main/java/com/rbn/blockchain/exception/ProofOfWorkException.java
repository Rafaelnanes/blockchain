package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ProofOfWorkException extends GenericException {

  public ProofOfWorkException() {
    super("Invalid proof of work");
  }

}
