package ru.prudnikova.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static ru.prudnikova.helpers.CustomAllureListener.withCustomTemplates;

public class Specs {
    public static RequestSpecification requestSpecJson = with()
            .filter(withCustomTemplates())
            .baseUri("https://allure.autotests.cloud")
            .log().all()
            .contentType("application/json;charset=UTF-8");

    public static RequestSpecification requestSpecForm = with()
            .filter(withCustomTemplates())
            .baseUri("https://allure.autotests.cloud")
            .log().all()
            .contentType("application/x-www-form-urlencoded");

    public static ResponseSpecification responseSpec200 = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200).build();

}
