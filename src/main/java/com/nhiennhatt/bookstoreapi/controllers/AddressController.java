package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNDistrictResponse;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNProvinceResponse;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNWardResponse;
import com.nhiennhatt.bookstoreapi.utils.GHNUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class AddressController {
    @Value("${ghn.token}")
    private String ghnToken;

    @GetMapping("/provinces")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GHNProvinceResponse.GHNProvinceData>> getProvinces() {
        return ResponseEntity.ok(GHNUtil.getProvinces(ghnToken));
    }

    @GetMapping("/districts")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GHNDistrictResponse.GHNDistrictData>> getDistricts(@RequestParam int provinceId) {
        return ResponseEntity.ok(GHNUtil.getDistricts(ghnToken, provinceId));
    }

    @GetMapping("/wards")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GHNWardResponse.GHNWardData>> getWards(@RequestParam int districtId) {
        return ResponseEntity.ok(GHNUtil.getWards(ghnToken, districtId));
    }
}
