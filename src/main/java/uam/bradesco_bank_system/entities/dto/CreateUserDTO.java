package uam.bradesco_bank_system.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record CreateUserDTO(

        String name,
        @Email
        String email,
        @CPF
        String document,
        @Size(min = 11, max = 11)
        String phoneNumber
) {
}
