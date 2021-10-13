package com.rbn.blockchain.web;

import com.rbn.blockchain.model.dto.CreateTransactionRequest;
import com.rbn.blockchain.model.wallet.Transaction;
import com.rbn.blockchain.service.DefaultTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

  @Autowired
  private DefaultTransactionService transactionService;

  @PostMapping
  public Transaction get(@RequestBody CreateTransactionRequest createTransactionRequest) {
    return transactionService.create(createTransactionRequest);
  }

}
