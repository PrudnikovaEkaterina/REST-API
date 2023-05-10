package ru.prudnikova.api;

import generators.TestCaseGenerator;
import org.junit.jupiter.api.Assertions;
import ru.prudnikova.models.ScenarioModel;
import ru.prudnikova.models.TestCaseModel;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static ru.prudnikova.models.ScenarioModel.createStepsTestCase;
import static ru.prudnikova.specs.Specs.requestSpecJson;
import static ru.prudnikova.specs.Specs.responseSpec200;

public class TestCaseApi {
    public static int createTestCase(String projectId, String xsrfToken, String allureTestopsSession){
        TestCaseModel testCaseModel = TestCaseGenerator.generationTestCaseName();
        TestCaseModel testCase = given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .queryParam("projectId", projectId)
                .body(testCaseModel)
                .when()
                .post("api/rs/testcasetree/leaf")
                .then()
                .spec(responseSpec200)
                .extract().as(TestCaseModel.class);
        Assertions.assertEquals(testCaseModel.getName(), testCase.getName());
        return testCase.getId();
    }

    public static void addScenarioTestCase (int idTestCase, String xsrfToken, String allureTestopsSession){
        ScenarioModel steps = createStepsTestCase();
        given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .body(steps)
                .when()
                .post("/api/rs/testcase/" + idTestCase + "/scenario")
                .then()
                .spec(responseSpec200)
                .body("steps[0].name", is(steps.getSteps().get(0).getName()));
    }

}
