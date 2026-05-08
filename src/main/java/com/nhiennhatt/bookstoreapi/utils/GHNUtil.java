package com.nhiennhatt.bookstoreapi.utils;

import com.nhiennhatt.bookstoreapi.common.classes.GHNCalShippingFeeBody;
import com.nhiennhatt.bookstoreapi.common.classes.GHNCalShippingFeeResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

public class GHNUtil {
    public static GHNCalShippingFeeResponse calShippingFee(String token, GHNCalShippingFeeBody body) {
        URI uri = null;
        try {
            uri = new URI("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee");
        } catch (URISyntaxException e) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        String stringBody = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .header("token", token)
                .POST(HttpRequest.BodyPublishers.ofString(stringBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<Supplier<GHNCalShippingFeeResponse>> response = client.send(request, asJson(GHNCalShippingFeeResponse.class));
            return response.body().get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createShippingOrder(String token, String shopId) {

    }

    private static <T> HttpResponse.BodyHandler<Supplier<T>> asJson(Class<T> targetValue) {
        ObjectMapper mapper = new ObjectMapper();

        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofInputStream(),
                inputStream -> () -> {
                    try {
                        return mapper.readValue(inputStream, targetValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
    }
}
