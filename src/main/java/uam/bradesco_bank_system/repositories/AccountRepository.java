package uam.bradesco_bank_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uam.bradesco_bank_system.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
