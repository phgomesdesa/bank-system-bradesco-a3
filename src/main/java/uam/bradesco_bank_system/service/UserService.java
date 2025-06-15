package uam.bradesco_bank_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uam.bradesco_bank_system.entities.User;
import uam.bradesco_bank_system.entities.dto.CreateUserDTO;
import uam.bradesco_bank_system.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public User create(CreateUserDTO request) {
        // transforma o DTO em entidade User
        User user = new User(request);

        // cria uma conta para o usuário
        accountService.create(user);

        // salva no banco de dados
        return userRepository.save(user);
    }

    public User findById(Long id) {
        // busca o usuário pelo ID e lança uma exceção se não encontrado
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<User> findAll() {
        // retorna todos os usuários do banco de dados
        return userRepository.findAll();
    }

    public void delete(Long id) {
        // busca o usuário pelo ID
        User user = findById(id);

        // deleta a conta associada ao usuário
        accountService.delete(user.getAccount().getId());

        // deleta o usuário do banco de dados
        userRepository.delete(user);
    }
}
