package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, CustomUserRepository {
    @Override
    <S extends User> S save(S entity);

    User findUserByUsername(String username);

    boolean existsUserByUsername(String username);

    User findUserById(UUID id);
}
