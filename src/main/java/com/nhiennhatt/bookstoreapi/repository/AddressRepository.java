package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<UserAddress, UUID> {
    List<UserAddress> findUserAddressByUser(User user);

    UserAddress findUserAddressById(UUID id);

    int removeByIdAndUser(UUID id, User user);


    @Modifying
    @Query("""
            UPDATE UserAddress
            SET name = :#{#address.name},
                address = :#{#address.address},
                district = :#{#address.district},
                ward = :#{#address.ward},
                provinceId = :#{#address.provinceId},
                districtId = :#{#address.districtId},
                wardCode = :#{#address.wardCode},
                phone = :#{#address.phone}
            WHERE id = :id AND user = :user
            """)
    int updateAddress(@Param("id") UUID id, @Param("user") User user, @Param("address") UserAddress address);

    @Modifying
    @Query("UPDATE UserAddress SET isDefault = true WHERE id = :id AND user = :user")
    int updateDefaultAddress(@Param("id") UUID id, @Param("user") User user);

    @Modifying
    @Query("UPDATE UserAddress SET isDefault = false WHERE user = :user AND isDefault = true")
    void resetDefaultAddress(@Param("user") User user);
}
