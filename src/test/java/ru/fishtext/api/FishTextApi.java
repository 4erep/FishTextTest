package ru.fishtext.api;

import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class FishTextApi {

    private final static String BASE_URI = "https://fish-text.ru";

    public static Response get(HashMap<String, Object> queryParams) {
        return given().
                baseUri(BASE_URI).
                basePath("/get").
                queryParams(queryParams).
                log().all().
                when().get().
                then().
                log().body().
                statusCode(200).
                extract().response();
    }
}


