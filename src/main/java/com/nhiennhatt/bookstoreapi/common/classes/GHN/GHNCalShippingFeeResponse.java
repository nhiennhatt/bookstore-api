package com.nhiennhatt.bookstoreapi.common.classes.GHN;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GHNCalShippingFeeResponse {
    private int code;
    private String message;
    private GHNCalShippingFeeData data;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    public static class GHNCalShippingFeeData {
        private int total;
    }
}
