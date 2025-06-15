package uam.bradesco_bank_system.entities.dto;

import java.math.BigDecimal;

public record TransferRequestDTO(
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount
) {
}
