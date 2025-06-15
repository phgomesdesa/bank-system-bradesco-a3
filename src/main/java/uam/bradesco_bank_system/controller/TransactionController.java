package uam.bradesco_bank_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uam.bradesco_bank_system.entities.dto.DepositRequestDTO;
import uam.bradesco_bank_system.entities.dto.FraudDecisionDTO;
import uam.bradesco_bank_system.entities.dto.TransferRequestDTO;
import uam.bradesco_bank_system.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequestDTO dto) {
        transactionService.deposit(dto);
        return ResponseEntity.ok("Depósito realizado com sucesso.");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDTO dto) {
        transactionService.transfer(dto);
        return ResponseEntity.ok("Transferência realizada com sucesso.");
    }

    @PostMapping("/fraud-decision")
    public String fraudDecision (@RequestBody FraudDecisionDTO dto) {
        transactionService.fraudDecision(dto);
        return "Decisão de fraude processada com sucesso.";
    }


}
