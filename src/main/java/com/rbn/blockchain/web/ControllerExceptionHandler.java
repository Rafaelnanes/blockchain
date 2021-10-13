package com.rbn.blockchain.web;

import com.rbn.blockchain.exception.GenericException;
import com.rbn.blockchain.model.dto.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler({GenericException.class})
  public ResponseEntity<ErrorMessage> proofOfWork(GenericException exception) {
    log.error("ExceptionHandler exception {}, message: {}", exception.getClass(), exception.getMessage());
    return ResponseEntity.status(exception.getHttpStatus().value())
                         .body(new ErrorMessage(exception.getMessage()));
  }

}
