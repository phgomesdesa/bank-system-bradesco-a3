package uam.bradesco_bank_system.entities.dto;

public record FraudDecisionDTO(
        String transactionId,
        boolean statusDecision
) {
}
