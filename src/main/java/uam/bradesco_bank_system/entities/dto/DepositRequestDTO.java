package uam.bradesco_bank_system.entities.dto;

import java.math.BigDecimal;

public record DepositRequestDTO(
        Long accountId,
        BigDecimal amount
) {
}
