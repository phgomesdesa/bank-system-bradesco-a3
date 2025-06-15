package uam.bradesco_bank_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.bradesco_bank_system.entities.Account;
import uam.bradesco_bank_system.entities.Transaction;
import uam.bradesco_bank_system.entities.dto.DepositRequestDTO;
import uam.bradesco_bank_system.entities.dto.FraudDecisionDTO;
import uam.bradesco_bank_system.entities.dto.FraudNotificationDTO;
import uam.bradesco_bank_system.entities.dto.TransferRequestDTO;
import uam.bradesco_bank_system.entities.enums.FraudReasonEnum;
import uam.bradesco_bank_system.entities.enums.TransactionStatus;
import uam.bradesco_bank_system.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FraudNotifierService fraudNotifierService;


    public void deposit(DepositRequestDTO request) {
        Account account = accountService.findById(request.accountId());

        account.setBalance(account.getBalance().add(request.amount()));

        accountService.save(account);

        Transaction transaction = new Transaction(account, request.amount());

        transactionRepository.save(transaction);
    }

    public void transfer(TransferRequestDTO request) {

        Account fromAccount = accountService.findById(request.fromAccountId());
        Account toAccount = accountService.findById(request.toAccountId());

        Transaction transaction = new Transaction(toAccount, fromAccount, request.amount());
        transactionRepository.save(transaction);

        validateAccounts(fromAccount, toAccount, transaction);
        validateAccountBalance(fromAccount, request.amount(), transaction);
        validateThirtyPercent(fromAccount, request.amount(), transaction);

        finishTransaction(transaction, fromAccount, toAccount);

        transaction.setStatus(TransactionStatus.APPROVED);
        transactionRepository.save(transaction);
    }

    private void finishTransaction(Transaction transaction, Account fromAccount, Account toAccount) {
        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));

        accountService.save(fromAccount);
        accountService.save(toAccount);
    }

    private boolean validateAccounts(Account fromAccount, Account toAccount, Transaction transaction) {
        if (fromAccount.getId().equals(toAccount.getId())) {
            setStatusRejected(transaction);
            throw new IllegalArgumentException("Não é permitido transferir para a mesma conta.");
        }
        return true;
    }

    public boolean validateAccountBalance(Account fromAccount, BigDecimal amount, Transaction transaction) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            setStatusRejected(transaction);
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transação.");
        }
        return true;
    }

    public void setStatusRejected(Transaction transaction) {
        transaction.setStatus(TransactionStatus.REJECTED);
        transactionRepository.save(transaction);
    }

    public boolean validateThirtyPercent(Account fromAccount, BigDecimal amount, Transaction transaction) {
        BigDecimal thirtyPercent = fromAccount.getBalance().multiply(BigDecimal.valueOf(0.3));
        if (amount.compareTo(thirtyPercent) > 0) {
            fraudNotifierService.notify(createFraudNotification(transaction), fromAccount);
            transaction.setStatus(TransactionStatus.FRAUD_UNDER_INVESTIGATION);
            transactionRepository.save(transaction);
            throw new IllegalArgumentException("Transação negada, veja o motivo no seu email.");
        }
        return true;
    }

    private FraudNotificationDTO createFraudNotification(Transaction transaction) {
        return new FraudNotificationDTO(
                transaction.getId().toString(),
                transaction.getFromAccount().getId(),
                transaction.getToAccount().getId(),
                transaction.getAmount(),
                FraudReasonEnum.THIRTY_PERCENT_AMOUNT.getDescription(),
                LocalDateTime.now().toString()
        );
    }

    public void fraudDecision(FraudDecisionDTO dto) {
        Transaction transaction = transactionRepository.findById(UUID.fromString(dto.transactionId()))
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada."));

        verifyTransactionStatus(transaction.getStatus());

        if (dto.statusDecision()) {
            finishTransaction(transaction, transaction.getFromAccount(), transaction.getToAccount());
            transaction.setStatus(TransactionStatus.APPROVED);
        } else {
            transaction.setStatus(TransactionStatus.REJECTED);
            // criar boletim de ocorrencia
        }
        transactionRepository.save(transaction);
    }

    private void verifyTransactionStatus(TransactionStatus status) {
        if (status != TransactionStatus.FRAUD_UNDER_INVESTIGATION) {
            throw new IllegalArgumentException("A transação não está sob investigação de fraude.");
        }
    }
}
