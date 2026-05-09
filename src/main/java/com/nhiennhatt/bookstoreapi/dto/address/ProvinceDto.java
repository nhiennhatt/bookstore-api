package com.nhiennhatt.bookstoreapi.dto.address;

import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNProvinceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProvinceDto {
    public static ProvinceDto from(GHNProvinceResponse.GHNProvinceData data) {
        return new ProvinceDto(data.getProvinceId(), data.getProvinceName());
    }

    private int id;
    private String name;
}
