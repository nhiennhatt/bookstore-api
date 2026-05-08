package com.nhiennhatt.bookstoreapi.common.classes.GHN;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GHNCreateOrderResponse {
    private int code;
    private String message;
    private GHNCalShippingFeeData data;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    public static class GHNCalShippingFeeData {
        @JsonProperty("order_code")
        private String orderCode;
    }
}
