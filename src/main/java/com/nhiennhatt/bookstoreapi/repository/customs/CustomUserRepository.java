package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserRepository {
    CurrentUser findCurrentUserByUsername(String username);
}
