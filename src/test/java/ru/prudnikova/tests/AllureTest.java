package ru.prudnikova.tests;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import ru.prudnikova.api.AuthorizationApi;
import ru.prudnikova.api.TestCaseApi;
import ru.prudnikova.models.DeleteTestCaseModel;
import ru.prudnikova.web.TestCaseWeb;

import static io.restassured.RestAssured.given;
import static ru.prudnikova.specs.Specs.*;

public class AllureTest {
    @Owner("Prudnikova")
    @Tags({@Tag("Api"), @Tag("Web")})
    @DisplayName("Создание тест кейса по апи и редпактирование его в браузере")
    @Test
    void createTestCaseWithScenario() {
        String description ="Тестовое описание !";
        String projectId = "2232";
        TestCaseWeb testCaseWeb=new TestCaseWeb();
        String xsrfToken = AuthorizationApi.getXsrfToken();
        String allureTestopsSession = AuthorizationApi.getAllureTestopsSession();
        int idTestCase = TestCaseApi.createTestCase(projectId, xsrfToken, allureTestopsSession);
        TestCaseApi.addScenarioTestCase(idTestCase, xsrfToken, allureTestopsSession);
        TestCaseWeb.setCookiesToBrowser(xsrfToken, allureTestopsSession);
        testCaseWeb.refactorDescription(idTestCase,description).verifyDescription(description);
    }

    @Owner("Prudnikova")
    @Tag ("Api")
    @DisplayName("Удаление тест кейса с предварительным созданием")
    @Test
    void deleteTestCase() {
        String projectId = "2232";
        String xsrfToken = AuthorizationApi.getXsrfToken();
        String allureTestopsSession = AuthorizationApi.getAllureTestopsSession();
        int idTestCase = TestCaseApi.createTestCase(projectId, xsrfToken, allureTestopsSession);
        DeleteTestCaseModel response = given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .body("{\"deleted\":true}")
                .when()
                .patch("/api/rs/testcase/" + idTestCase)
                .then()
                .spec(responseSpec200)
                .extract().as(DeleteTestCaseModel.class);
        Assertions.assertTrue(response.isDeleted());
    }

    @Owner("Prudnikova")
    @Tag ("Api")
    @DisplayName("Редактирование имени созданного тест кейса")
    @Test
    void renameTestCase() {
        String projectId = "2232";
        String xsrfToken = AuthorizationApi.getXsrfToken();
        String allureTestopsSession = AuthorizationApi.getAllureTestopsSession();
        int idTestCase = TestCaseApi.createTestCase(projectId, xsrfToken, allureTestopsSession);
        given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .queryParam("projectId", projectId)
                .queryParam("leafId", idTestCase)
                .body("{\"name\":\"Rename Test Case\"}")
                .when()
                .post("/api/rs/testcasetree/leaf/rename")
                .then()
                .spec(responseSpec200);

    }

}
