package uam.bradesco_bank_system.entities.dto;

import java.math.BigDecimal;

public record FraudNotificationDTO(
        String transactionId,
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        String reason,
        String timestamp
) {
}
