package uam.bradesco_bank_system.entities.enums;

public enum FraudReasonEnum {
    SUSPICIOUS_ACTIVITY("Atividade suspeita"),
    THIRTY_PERCENT_AMOUNT("Valor acima de 30% do saldo");

    private final String description;

    FraudReasonEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
