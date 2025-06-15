package uam.bradesco_bank_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.bradesco_bank_system.entities.Account;
import uam.bradesco_bank_system.entities.User;
import uam.bradesco_bank_system.entities.dto.FraudNotificationDTO;

@Service
public class FraudNotifierService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    public void notify(FraudNotificationDTO dto, Account fromAccount) {

        User fromAccountUser = userService.findById(fromAccount.getUser().getId());

        String content = String.format("""
                🚨 Alerta de fraude detectado 🚨
                
                Conta de origem: %d
                Conta de destino: %d
                Valor: R$ %.2f
                Motivo: %s
                Horário: %s
                """, dto.fromAccountId(), dto.toAccountId(), dto.amount(), dto.reason(), dto.timestamp());

        emailService.sendFraudAlert(fromAccountUser.getEmail(), "🚨 Fraude detectada!", content);

    }

}
