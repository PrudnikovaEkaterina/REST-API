package ru.prudnikova.api;

import ru.prudnikova.generators.TestCaseGenerator;
import org.junit.jupiter.api.Assertions;
import ru.prudnikova.models.scenario.Scenario;
import ru.prudnikova.models.TestCaseModel;

import static io.restassured.RestAssured.given;
import static ru.prudnikova.specs.Specs.requestSpecJson;
import static ru.prudnikova.specs.Specs.responseSpec200;

public class TestCaseApi {
    public static TestCaseModel createTestCaseName(){
        TestCaseModel body = TestCaseGenerator.generationTestCaseName();
        return body;
    }

    public static TestCaseModel createTestCase(TestCaseModel body, String projectId, String xsrfToken, String allureTestopsSession){
        TestCaseModel response = given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .queryParam("projectId", projectId)
                .body(body)
                .when()
                .post("api/rs/testcasetree/leaf")
                .then()
                .spec(responseSpec200)
                .extract().as(TestCaseModel.class);
        return response;
    }

    public static void verifyTestCaseName(TestCaseModel body, TestCaseModel response){
        Assertions.assertEquals(body.getName(), response.getName());
    }

    public static Scenario createScenarioModelBody (){
        Scenario body = Scenario.createScenarioSteps();
        return body;
    }



    public static Scenario addScenarioTestCase (TestCaseModel testCase, Scenario body, String xsrfToken, String allureTestopsSession){
        Scenario response =given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .body(body)
                .when()
                .post("/api/rs/testcase/" + testCase.getId() + "/scenario")
                .then()
                .spec(responseSpec200)
                .extract().as(Scenario.class);
        return response;
    }
    public static void verifyScenarioStepsName (Scenario body, Scenario request){
        Assertions.assertEquals(body.getSteps().get(0).getName(), request.getSteps().get(0).getName());
    }


}
