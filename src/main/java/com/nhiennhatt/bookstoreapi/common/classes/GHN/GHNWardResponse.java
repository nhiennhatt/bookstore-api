package com.nhiennhatt.bookstoreapi.common.classes.GHN;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GHNWardResponse {
    private int code;
    private String message;
    private List<GHNWardData> data;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    public static class GHNWardData {
        @JsonProperty("WardCode")
        private String wardCode;

        @JsonProperty("WardName")
        private String wardName;

        @JsonProperty("DistrictID")
        private int districtId;
    }
}
