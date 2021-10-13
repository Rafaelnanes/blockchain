package com.rbn.blockchain.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends GenericException {

  public NotFoundException() {
    super("Resource not found");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

}
