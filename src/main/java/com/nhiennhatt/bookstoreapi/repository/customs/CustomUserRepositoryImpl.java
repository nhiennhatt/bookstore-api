package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.common.enums.UserStatus;
import com.nhiennhatt.bookstoreapi.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.query.NativeQuery;

import java.util.UUID;

public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CurrentUser findCurrentUserByUsername(String username) {
        Query query = entityManager.createNativeQuery("SELECT id, username, role, status, password FROM users WHERE username = ?");
        query.setParameter(1, username);
        query.setMaxResults(1);
        CurrentUser user = (CurrentUser) query.unwrap(NativeQuery.class).setTupleTransformer((tuple, aliases) -> CurrentUser.builder()
                        .id((UUID) tuple[0])
                        .username((String) tuple[1])
                        .role(UserRole.valueOf((String) tuple[2]))
                        .status(UserStatus.valueOf((String) tuple[3]))
                        .password((String) tuple[4])
                        .build())
                .getSingleResultOrNull();
        return user;
    }
}
