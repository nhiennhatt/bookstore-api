package com.nhiennhatt.bookstoreapi.dto.address;

import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNWardResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WardDto {
    public static WardDto from(GHNWardResponse.GHNWardData data) {
        return new WardDto(data.getWardName(), data.getWardCode(), data.getDistrictId());
    }

    private String name;
    private String code;
    private int districtId;
}
