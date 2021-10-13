package com.rbn.blockchain.service;

import com.rbn.blockchain.model.dto.CreateTransactionRequest;
import com.rbn.blockchain.model.dto.WalletRequest;
import com.rbn.blockchain.model.wallet.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultTransactionService {

  private final DefaultWalletService walletService;

  private final DefaultBlockchainService blockchainService;

  public Transaction create(CreateTransactionRequest createTransactionRequest) {
    WalletRequest senderWalletData = createTransactionRequest.getSenderWallet();
    var senderWallet = walletService.get(senderWalletData.getPrivateKey(), senderWalletData.getPublicKey());
    Transaction transaction = senderWallet.createTransaction(createTransactionRequest.getReceiverAddress(),
        createTransactionRequest.getAmount());
    blockchainService.add(transaction);
    return transaction;
  }

}
