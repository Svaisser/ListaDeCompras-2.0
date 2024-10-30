package br.com.svaisser.listaCompras.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, Integer> {

    UserModel findByUsername(String username);
}