package com.nhiennhatt.bookstoreapi.common.classes.GHN;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GHNDistrictResponse {
    private int code;
    private String message;
    private List<GHNDistrictData> data;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    public static class GHNDistrictData {
        @JsonProperty("DistrictID")
        private int districtId;
        @JsonProperty("DistrictName")
        private String districtName;
        @JsonProperty("ProvinceID")
        private int provinceId;
    }
}
