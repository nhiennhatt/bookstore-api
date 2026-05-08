package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<UserAddress, UUID> {
    List<UserAddress> findUserAddressByUser(User user);

    UserAddress findUserAddressById(UUID id);
}
