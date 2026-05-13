package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNDistrictResponse;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNProvinceResponse;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNWardResponse;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderOverviewDto;
import com.nhiennhatt.bookstoreapi.dto.user.MeResponse;
import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import com.nhiennhatt.bookstoreapi.repository.AddressRepository;
import com.nhiennhatt.bookstoreapi.repository.OrderRepository;
import com.nhiennhatt.bookstoreapi.repository.UserRepository;
import com.nhiennhatt.bookstoreapi.utils.GHNUtil;
import com.nhiennhatt.bookstoreapi.validations.address.CreateAddressValidation;
import com.nhiennhatt.bookstoreapi.validations.user.UpdateUserInform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MeService {
    @Value("${ghn.token}")
    private String ghnToken;

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
        UserAddress userAddress = generateAddressInstance(currentUser, address);
        if (userAddress == null) return null;
        return addressRepository.save(userAddress);
    }

    public List<UserAddress> getAddresses(CurrentUser currentUser) {
        return addressRepository.findUserAddressByUser(userRepository.getReferenceById(currentUser.getId()));
    }

    public List<OrderOverviewDto> getOrders(CurrentUser currentUser) {
        return orderRepository.findOrderOverviewsByUser(currentUser.getId());
    }

    @Transactional
    public void deleteAddress(UUID id, CurrentUser currentUser) {
        addressRepository.removeByIdAndUser(id, userRepository.getReferenceById(currentUser.getId()));
    }

    @Transactional
    public void updateUserInform(UUID id, UpdateUserInform userInform) {
        userRepository.updateUserInform(id, userInform);
    }

    @Transactional
    public UserAddress updateAddress(UUID id, CurrentUser currentUser, CreateAddressValidation address) {
        UserAddress userAddress = generateAddressInstance(currentUser, address);
        if (userAddress == null) return null;
        int result = addressRepository.updateAddress(id, userRepository.getReferenceById(currentUser.getId()), userAddress);
        if (result == 0) throw new RuntimeException("Address not found");
        userAddress.setId(id);
        return userAddress;
    }

    @Transactional
    public void setDefaultAddress(UUID id, CurrentUser currentUser) {
        User user = userRepository.getReferenceById(currentUser.getId());
        addressRepository.resetDefaultAddress(user);
        int result = addressRepository.updateDefaultAddress(id, user);
        if (result == 0) throw new RuntimeException("Address not found");
    }

    private UserAddress generateAddressInstance(CurrentUser currentUser, CreateAddressValidation address) {
        List<GHNProvinceResponse.GHNProvinceData> provinces = GHNUtil.getProvinces(ghnToken);
        if (provinces == null) return null;
        GHNProvinceResponse.GHNProvinceData province = provinces.stream().filter(p -> p.getProvinceId() == address.getProvinceId()).findFirst().orElse(null);
        if (province == null) return null;

        List<GHNDistrictResponse.GHNDistrictData> districts = GHNUtil.getDistricts(ghnToken, province.getProvinceId());
        if (districts == null) return null;
        GHNDistrictResponse.GHNDistrictData district = districts.stream().filter(d -> d.getDistrictId() == address.getDistrictId()).findFirst().orElse(null);
        if (district == null) return null;

        List<GHNWardResponse.GHNWardData> wards = GHNUtil.getWards(ghnToken, district.getDistrictId());
        if (wards == null) return null;
        GHNWardResponse.GHNWardData ward = wards.stream().filter(w -> Objects.equals(w.getWardCode(), address.getWardCode())).findFirst().orElse(null);
        if (ward == null) return null;

        UserAddress userAddress = new UserAddress();
        userAddress.setAddress(address.getAddress());
        userAddress.setProvince(province.getProvinceName());
        userAddress.setWard(ward.getWardName());
        userAddress.setDistrict(district.getDistrictName());
        userAddress.setProvinceId(address.getProvinceId());
        userAddress.setWardCode(address.getWardCode());
        userAddress.setDistrictId(address.getDistrictId());
        userAddress.setName(address.getName());
        userAddress.setPhone(address.getPhone());
        userAddress.setUser(userRepository.getReferenceById(currentUser.getId()));

        return userAddress;
    }
}
