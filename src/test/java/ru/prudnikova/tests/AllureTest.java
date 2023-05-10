package ru.prudnikova.tests;

import io.qameta.allure.Owner;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import ru.prudnikova.api.AuthorizationApi;
import ru.prudnikova.api.TestCaseApi;
import ru.prudnikova.generators.DescriptionGenerator;
import ru.prudnikova.models.DeleteTestCaseModel;
import ru.prudnikova.models.scenario.Scenario;
import ru.prudnikova.models.TestCaseModel;
import ru.prudnikova.models.work.Data;
import ru.prudnikova.web.TestCaseWeb;


import java.util.*;
import java.util.stream.Collectors;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static ru.prudnikova.specs.Specs.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AllureTest {
    @Owner("Prudnikova")
    @Tags({@Tag("Api"), @Tag("Web")})
    @DisplayName("Создание тест кейса по апи и редактирование его в браузере")
    @Test
    void createTestCaseWithScenario() {
        String description = DescriptionGenerator.descriptionGenerate();
        String projectId = "2232";
        TestCaseWeb testCaseWeb=new TestCaseWeb();
        String xsrfToken =  step("Get xsrf token", AuthorizationApi::getXsrfToken);
        String allureTestopsSession = step("Get allure testops session", AuthorizationApi::getAllureTestopsSession);
        TestCaseModel nameTestCase =  step("Create test case name", TestCaseApi::createTestCaseName);
        TestCaseModel responseCreateTestCase =  step("Create test case", () -> TestCaseApi.createTestCase(nameTestCase,projectId, xsrfToken,allureTestopsSession));
        step("Verify test case name", () ->TestCaseApi.verifyTestCaseName(nameTestCase, responseCreateTestCase));
        Scenario bodyScenario=step("Create scenario body with steps", TestCaseApi::createScenarioModelBody);
        Scenario responseAddScenario = step("Add scenario to test case", () -> TestCaseApi.addScenarioTestCase(responseCreateTestCase, bodyScenario, xsrfToken, allureTestopsSession));
        step("Verify scenario step name", () ->TestCaseApi.verifyScenarioStepsName(bodyScenario, responseAddScenario));
        step("Set cookies from get api to browser", () ->TestCaseWeb.setCookiesToBrowser(xsrfToken, allureTestopsSession));
        step("Refactor description in create test case", () -> testCaseWeb.refactorDescription(responseCreateTestCase,description));
        step("verify description after refactor", () -> testCaseWeb.verifyDescription(description));
    }

    @Owner("Prudnikova")
    @Tag ("Api")
    @DisplayName("Удаление тест кейса с предварительным созданием")
    @Test
    void deleteTestCase() {
        String projectId = "2232";
        String xsrfToken =  step("Get xsrf token", AuthorizationApi::getXsrfToken);
        String allureTestopsSession = step("Get allure testops session", AuthorizationApi::getAllureTestopsSession);
        TestCaseModel nameTestCase =  step("Create test case name", TestCaseApi::createTestCaseName);
        TestCaseModel responseCreateTestCase =  step("Create test case", () -> TestCaseApi.createTestCase(nameTestCase,projectId, xsrfToken,allureTestopsSession));
        step("Verify test case name", () ->TestCaseApi.verifyTestCaseName(nameTestCase, responseCreateTestCase));
        DeleteTestCaseModel response = step("Delete test case", () -> given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .body("{\"deleted\":true}")
                .when()
                .patch("/api/rs/testcase/" + responseCreateTestCase.getId())
                .then()
                .spec(responseSpec200)
                .extract().as(DeleteTestCaseModel.class));
        step("verify deleted true", () -> Assertions.assertTrue(response.isDeleted()));
    }

    @Owner("Prudnikova")
    @Tag ("Api")
    @DisplayName("Редактирование имени созданного тест кейса")
    @Test
    void renameTestCase() {
        String projectId = "2232";
        String rename ="{\"name\":\"Rename Test Case\"}";
        String xsrfToken =  step("Get xsrf token", AuthorizationApi::getXsrfToken);
        String allureTestopsSession = step("Get allure testops session", AuthorizationApi::getAllureTestopsSession);
        TestCaseModel nameTestCase =  step("Create test case name", TestCaseApi::createTestCaseName);
        TestCaseModel responseCreateTestCase =  step("Create test case", () -> TestCaseApi.createTestCase(nameTestCase,projectId, xsrfToken,allureTestopsSession));
        step("Verify test case name", () ->TestCaseApi.verifyTestCaseName(nameTestCase, responseCreateTestCase));
        TestCaseModel response = step("Rename test case", () -> given()
                .spec(requestSpecJson)
                .cookies("XSRF-TOKEN", xsrfToken, "ALLURE_TESTOPS_SESSION", allureTestopsSession)
                .header("X-XSRF-TOKEN", xsrfToken)
                .queryParam("projectId", projectId)
                .queryParam("leafId", responseCreateTestCase.getId())
                .body(rename)
                .when()
                .post("/api/rs/testcasetree/leaf/rename")
                .then()
                .spec(responseSpec200).extract().as(TestCaseModel.class));
        step("Verify test case name", () ->assertThat(response.getName(), is ("Rename Test Case")));

    }


    @Test
    void аа () {
        Data response = given()
                .baseUri("https://www.marketcall.ru")
                .log().all()
                .contentType(ContentType.JSON)
                .cookie("laravel_session", "eyJpdiI6Im12Z0RWRW1VN0RDdTJ6VU14MUMya3c9PSIsInZhbHVlIjoielhZc1NheVI5QmI5Q2h0QzhJRFZEY2R4MElzY091YlRyZ2RzQUpBbDFCYVB6VG5sZTlrdTZYWW9pMWs1Wmp4V2F0Y2h5OEcrV0RvU1l6VXdsWkU4VFNtUHZBK0tNUlJEc1Y4MVp4U1VqR0RvMGNtV2s0MW9qWmhzeDlnWGdkcTUiLCJtYWMiOiJjODYyNTA1NWU0YmJjMjkzY2UzYTNkNGQzNmE1MzJjNTM0YTZkNzViNjRmZjNlOGFhMjRlNmMwOGU2ZDk1YWExIn0%3D")
                .header("X-Marketcall-Token", "e13d306a-df68-11ed-b5ea-0242ac120002")
                .when()
                .get("/api/v1/newbuilding-bundles")
                .then()
                .spec(responseSpec200).extract().as(Data.class);
        List<String> list0;
        List<String> list1;
        List<String> list2;
        List<String> list3;
        List<String> list4;

        list0=response.getData().get(0).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toList());
        list1=response.getData().get(1).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toList());
        list2=response.getData().get(2).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toList());
        list3=response.getData().get(3).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toList());
        list4=response.getData().get(4).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toList());
        List<String> listAll = new ArrayList<>(list0);
        listAll.addAll(list1);
        listAll.addAll(list2);
        listAll.addAll(list3);
        listAll.addAll(list4);

        System.out.println(listAll.size());
        Set<String> set0;
        Set<String> set1;
        Set<String> set2;
        Set<String> set3;
        Set<String> set4;
        set0=response.getData().get(0).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toSet());
        set1=response.getData().get(1).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toSet());
        set2=response.getData().get(2).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toSet());
        set3=response.getData().get(3).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toSet());
        set4=response.getData().get(4).getOffers().stream().map(el->el.getMove_newbuilding_secondary_id()).collect(Collectors.toSet());
        System.out.println(set0.size()+set1.size()+set2.size()+set3.size()+set4.size());

        System.out.println(list0.size());
        System.out.println(set0.size());


        HashMap<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, Integer> entry: map.entrySet()){
        int count=1;
        if (map.containsKey(list0.get()))
            map.put(line, (map.get(line)+1));
        else
            map.put(line, count))};





    }



}
