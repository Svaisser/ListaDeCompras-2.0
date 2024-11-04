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

    if (PUBLIC_ROUTES.contains(path)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Verificar se o método da requisição é GET
    if ("GET".equalsIgnoreCase(request.getMethod())) {
      try {
        if (token != null && token.startsWith("Bearer ")) {
          token = token.substring(7);
          Integer idUser = jwtUtil.extractIdUser(token);

          if (!jwtUtil.validateToken(token, idUser)) {
            throw new JwtException("Token inválido");
          }

          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(idUser, null,
              List.of());
          SecurityContextHolder.getContext().setAuthentication(authentication);

          request.setAttribute("idUser", idUser);
        } else {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.getWriter().write("Token JWT ausente ou inválido. Verifique o Login");
          response.getWriter().flush();
          return;
        }
      } catch (ExpiredJwtException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Token expirado. Logue novamente");
        response.getWriter().flush();
        System.out.println("Token expirado");
        return;
      } catch (JwtException e) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Token inválido. Verifique o Login");
        response.getWriter().flush();
        System.out.println("Token inválido");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

}
