package com.rbn.blockchain.web;

import com.rbn.blockchain.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler({GenericException.class})
  public String proofOfWork(GenericException exception) {
    log.error("ExceptionHandler: {}", exception.getMessage());
    return exception.getMessage();
  }

}
