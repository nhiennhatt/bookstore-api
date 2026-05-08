package com.nhiennhatt.bookstoreapi.common.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GHNCalShippingFeeBody {
    @JsonProperty("shop_id")
    private int shopId;
    @JsonProperty("to_ward_code")
    private String toWardCode;
    @JsonProperty("to_district_id")
    private int toDistrictId;
    private int weight;
    @JsonProperty("service_type_id")
    private int serviceTypeId = 2;
}
