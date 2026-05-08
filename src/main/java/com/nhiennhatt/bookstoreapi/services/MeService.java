package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderDto;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderOverviewDto;
import com.nhiennhatt.bookstoreapi.dto.user.MeResponse;
import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import com.nhiennhatt.bookstoreapi.repository.AddressRepository;
import com.nhiennhatt.bookstoreapi.repository.OrderRepository;
import com.nhiennhatt.bookstoreapi.repository.UserRepository;
import com.nhiennhatt.bookstoreapi.validations.address.CreateAddressValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderRepository orderRepository;

    public MeResponse getMe(CurrentUser currentUser) {
        User user = userRepository.findUserById(currentUser.getId());
        return new MeResponse(user);
    }

    @Transactional
    public UserAddress addAddress(CurrentUser currentUser, CreateAddressValidation address) {
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress(address.getAddress());
        userAddress.setProvince(address.getProvince());
        userAddress.setWard(address.getWard());
        userAddress.setDistrict(address.getDistrict());
        userAddress.setProvinceId(address.getProvinceId());
        userAddress.setWardCode(address.getWardCode());
        userAddress.setDistrictId(address.getDistrictId());
        userAddress.setName(address.getName());
        userAddress.setPhone(address.getPhone());
        userAddress.setUser(userRepository.getReferenceById(currentUser.getId()));
        return addressRepository.save(userAddress);
    }

    public List<UserAddress> getAddresses(CurrentUser currentUser) {
        return addressRepository.findUserAddressByUser(userRepository.getReferenceById(currentUser.getId()));
    }

    public List<OrderOverviewDto> getOrders(CurrentUser currentUser) {
        return orderRepository.findOrderOverviewsByUser(currentUser.getId());
    }
}
