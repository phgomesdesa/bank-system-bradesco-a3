package uam.bradesco_bank_system.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uam.bradesco_bank_system.entities.enums.TransactionStatus;
import uam.bradesco_bank_system.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal amount;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEPOSIT or TRANSFER

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction(Account toAccount, BigDecimal amount) {
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = TransactionType.DEPOSIT;
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(Account fromAccount, Account toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = TransactionType.TRANSFER;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }

}
