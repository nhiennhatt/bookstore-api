package com.nhiennhatt.bookstoreapi.common.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GHNCreateOrderBody {
    @JsonProperty("shop_id")
    private int shopId;

    private String name;

    @JsonProperty("to_name")
    private String toName;

    @JsonProperty("to_phone")
    private String toPhone;

    @JsonProperty("to_address")
    private String toAddress;

    @JsonProperty("to_ward_code")
    private String toWardCode;

    @JsonProperty("to_district_id")
    private int toDistrictId;

    private int weight;

    @JsonProperty("service_type_id")
    private int serviceTypeId = 2;

    @JsonProperty("required_note")
    private String requiredNote;

    private List<GHNOrderItem> items;

    @JsonProperty("from_name")
    private String fromName;

    @JsonProperty("payment_type_id")
    private int paymentTypeId = 1;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @Builder(access = AccessLevel.PUBLIC)
    public static class GHNOrderItem {
        private String name;
        private int quantity;
    }
}
