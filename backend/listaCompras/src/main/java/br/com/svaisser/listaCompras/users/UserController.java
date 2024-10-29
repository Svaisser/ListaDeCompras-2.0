package br.com.svaisser.listaCompras.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.svaisser.listaCompras.config.ErrorResponse;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody UserModel userModel) {
        try {
            var user = this.userRepository.findByUsername(userModel.getUsername());

            if (user != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Usuário já existe!", HttpStatus.BAD_REQUEST.value()));
            }

            var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
            userModel.setPassword(passwordHashred);

            var userCreated = this.userRepository.save(userModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao criar usuário: " + e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserModel loginRequest) {
        Map<String, String> response = new HashMap<>();
        UserModel user = userRepository.findByUsername(loginRequest.getUsername());

        if (user != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(loginRequest.getPassword().toCharArray(),
                    user.getPassword());
            if (result.verified) {
                response.put("message", "Login bem-sucedido!");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Senha incorreta.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            response.put("message", "Usuário não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
