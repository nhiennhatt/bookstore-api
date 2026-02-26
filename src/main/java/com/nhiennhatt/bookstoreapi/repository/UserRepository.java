package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    @Override
    <S extends User> S save(S entity);

    User findUserByUsername(String username);
}
