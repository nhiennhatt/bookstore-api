package com.nhiennhatt.bookstoreapi.dto.address;

import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNDistrictResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DistrictDto {
    public static DistrictDto from (GHNDistrictResponse.GHNDistrictData data) {
        return new DistrictDto(data.getDistrictId(), data.getDistrictName(), data.getProvinceId());
    }

    private int id;
    private String name;
    private int provinceId;
}
