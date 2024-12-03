package br.com.svaisser.listaCompras.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.svaisser.listaCompras.config.ErrorResponse;
import br.com.svaisser.listaCompras.config.JwtUtil;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;
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

            if (userModel.getSecurityQuestion() == null || userModel.getSecurityQuestion().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Pergunta de segurança é obrigatória", HttpStatus.BAD_REQUEST.value()));
            }
            if (userModel.getSecurityAnswer() == null || userModel.getSecurityAnswer().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Resposta de segurança é obrigatória", HttpStatus.BAD_REQUEST.value()));
            }

            var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
            userModel.setPassword(passwordHashed);

            var securityAnswerHashed = BCrypt.withDefaults().hashToString(12, userModel.getSecurityAnswer().toCharArray());
            userModel.setSecurityAnswer(securityAnswerHashed);

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
                // Gerar o token JWT
                String token = jwtUtil.generateToken(user.getId());
                response.put("message", "Login bem-sucedido!");
                response.put("token", token);
                response.put("idUser", String.valueOf(user.getId())); // Converte o ID para String
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

    @PostMapping("/verify-security")
    public ResponseEntity<?> verifySecurity(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");

            // Buscar o usuário no banco de dados
            UserModel user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Usuário não encontrado", HttpStatus.NOT_FOUND.value()));
            }

            // Enviar a pergunta de segurança para o front-end
            Map<String, String> response = new HashMap<>();
            System.out.println("Security Question: " + user.getSecurityQuestion());
            response.put("securityQuestion", user.getSecurityQuestion());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String securityAnswer = request.get("securityAnswer");
            String newPassword = request.get("newPassword");
            System.out.println("username: " + username);
            System.out.println("securityAnswer: " + securityAnswer);
            System.out.println("newPassword: " + newPassword);

            // Buscar o usuário no banco de dados
            UserModel user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Usuário não encontrado", HttpStatus.NOT_FOUND.value()));
            }
            
            // Verificar se a resposta fornecida corresponde ao hash da resposta de segurança
            BCrypt.Result result = BCrypt.verifyer().verify(securityAnswer.toCharArray(), user.getSecurityAnswer());
            if (!result.verified) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Resposta de segurança incorreta", HttpStatus.UNAUTHORIZED.value()));
            }

            // Se a resposta estiver correta, atualizar a senha
            String passwordHash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
            user.setPassword(passwordHash);
            userRepository.save(user);

            return ResponseEntity.ok().body(Map.of("message", "Senha redefinida com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

}
