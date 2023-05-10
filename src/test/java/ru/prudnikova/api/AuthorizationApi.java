package ru.prudnikova.api;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static ru.prudnikova.specs.Specs.requestSpecForm;
import static ru.prudnikova.specs.Specs.responseSpec200;


public class AuthorizationApi {
    private String xsrfToken;
    private String allureTestopsSession;

    public static String getXsrfToken() {
        open("https://allure.autotests.cloud/login");
        return WebDriverRunner.getWebDriver().manage().getCookieNamed("XSRF-TOKEN").getValue();
    }

    public static String getAllureTestopsSession() {
       return given()
                .spec(requestSpecForm)
                .header("X-XSRF-TOKEN", getXsrfToken())
                .cookie("XSRF-TOKEN", getXsrfToken())
                .formParam("username", "allure8")
                .formParam("password", "allure8")
                .when()
                .post("/api/login/system")
                .then()
                .spec(responseSpec200)
                .extract().cookie("ALLURE_TESTOPS_SESSION");
    }
}
