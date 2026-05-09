package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNDistrictResponse;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNProvinceResponse;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNWardResponse;
import com.nhiennhatt.bookstoreapi.dto.address.DistrictDto;
import com.nhiennhatt.bookstoreapi.dto.address.ProvinceDto;
import com.nhiennhatt.bookstoreapi.dto.address.WardDto;
import com.nhiennhatt.bookstoreapi.utils.GHNUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@Tag(name = "Addresses")
public class AddressController {
    @Value("${ghn.token}")
    private String ghnToken;

    @GetMapping("/provinces")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProvinceDto>> getProvinces() {
        List<GHNProvinceResponse.GHNProvinceData> provinces = GHNUtil.getProvinces(ghnToken);
        return ResponseEntity.ok(provinces == null ? List.of() : provinces.stream().map(ProvinceDto::from).toList());
    }

    @GetMapping("/districts")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DistrictDto>> getDistricts(@RequestParam int provinceId) {
        List<GHNDistrictResponse.GHNDistrictData> districts = GHNUtil.getDistricts(ghnToken, provinceId);
        return ResponseEntity.ok(districts == null ? List.of() : districts.stream().map(DistrictDto::from).toList());
    }

    @GetMapping("/wards")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WardDto>> getWards(@RequestParam int districtId) {
        List<GHNWardResponse.GHNWardData> wards = GHNUtil.getWards(ghnToken, districtId);

        return ResponseEntity.ok(wards == null ? List.of() : wards.stream().map(WardDto::from).toList());
    }
}
