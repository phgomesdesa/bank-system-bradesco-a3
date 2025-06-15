package uam.bradesco_bank_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.bradesco_bank_system.entities.Account;
import uam.bradesco_bank_system.entities.User;
import uam.bradesco_bank_system.repositories.AccountRepository;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void create(User user) {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        user.setAccount(account);
    }

    public Account findById(Long id) {
        // Busca a conta pelo ID e lança uma exceção se não encontrada
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public void delete(Long id) {
        // Deleta a conta do banco de dados
        accountRepository.delete(findById(id));
    }

    public void save(Account account) {
        // Salva a conta no banco de dados
        accountRepository.save(account);
    }
}
