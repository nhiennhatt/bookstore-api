package com.nhiennhatt.bookstoreapi.utils;

import com.nhiennhatt.bookstoreapi.common.classes.GHN.*;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
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
            if (response.statusCode() != 200)
                return null;
            return response.body().get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String createShippingOrder(String token, GHNCreateOrderBody body) {
        try {
            URI uri = new URI("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("token", token)
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(body)))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<Supplier<GHNCreateOrderResponse>> response = client.send(request, asJson(GHNCreateOrderResponse.class));

            if (response.statusCode() != 200) {
                return null;
            }

            return response.body().get().getData().getOrderCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GHNProvinceResponse.GHNProvinceData> getProvinces(String token) {
        try {
            URI uri = new URI("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("token", token)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<Supplier<GHNProvinceResponse>> response = client.send(request, asJson(GHNProvinceResponse.class));
            if (response.statusCode() != 200) {
                return null;
            }
            return response.body().get().getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GHNDistrictResponse.GHNDistrictData> getDistricts(String token, int provinceId) {
        try {
            Map<String, Integer> body = Map.of("province_id", provinceId);
            URI uri = new URI("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("token", token)
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(body)))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<Supplier<GHNDistrictResponse>> response = client.send(request, asJson(GHNDistrictResponse.class));
            if (response.statusCode() != 200) {
                return null;
            }
            return response.body().get().getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GHNWardResponse.GHNWardData> getWards(String token, int districtId) {
        try {
            Map<String, Integer> body = Map.of("district_id", districtId);
            URI uri = new URI("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("token", token)
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(body)))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<Supplier<GHNWardResponse>> response = client.send(request, asJson(GHNWardResponse.class));
            if (response.statusCode() != 200) {
                System.out.println(response.statusCode());
                return null;
            }
            return response.body().get().getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
