package br.com.svaisser.listaCompras.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  private static final List<String> PUBLIC_ROUTES = Arrays.asList(
      "/users/login",
      "/users/");

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = request.getHeader("Authorization");
    String path = request.getRequestURI();

    // Permitir acesso a rotas públicas sem validação de token
    if (PUBLIC_ROUTES.contains(path)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Verificar se o método da requisição é GET
    if ("GET".equalsIgnoreCase(request.getMethod())) {
      try {
        if (token != null && token.startsWith("Bearer ")) {
          token = token.substring(7);
          Integer idUser = jwtUtil.extractIdUser(token); // Supondo que você tenha um método para extrair o ID do
                                                         // usuário

          if (!jwtUtil.validateToken(token, idUser)) {
            throw new JwtException("Token inválido");
          }

          // Definindo a autenticação no contexto de segurança
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(idUser, null,
              List.of());
          SecurityContextHolder.getContext().setAuthentication(authentication);

          request.setAttribute("idUser", idUser); // Adicionando o ID do usuário na requisição
        } else {
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token JWT ausente ou inválido");
          return;
        }
      } catch (ExpiredJwtException e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
        return;
      } catch (JwtException e) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token inválido");
        return;
      }
    }

    filterChain.doFilter(request, response); // Continua com a cadeia de filtros
  }

}
