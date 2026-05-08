package com.nhiennhatt.bookstoreapi.common.classes.GHN;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GHNProvinceResponse {
    private int code;
    private String message;
    private List<GHNProvinceData> data;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    public static class GHNProvinceData {
        @JsonProperty("ProvinceID")
        private int provinceId;
        @JsonProperty("ProvinceName")
        private String provinceName;
    }
}
