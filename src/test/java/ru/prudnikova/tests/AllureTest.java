package ru.prudnikova.tests;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Owner;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import ru.prudnikova.api.AuthorizationApi;
import ru.prudnikova.api.TestCaseApi;
import ru.prudnikova.dataBase.CallbackPhonesManager;
import ru.prudnikova.dataBase.CallbackPhonesManagerSpring;
import ru.prudnikova.dataBase.DataSourceProvider;
import ru.prudnikova.domain.CallbackPhonesBD;
import ru.prudnikova.generators.DescriptionGenerator;
import ru.prudnikova.models.DeleteTestCaseModel;
import ru.prudnikova.models.TestCaseModel;
import ru.prudnikova.models.scenario.Scenario;
import ru.prudnikova.models.work.Data;
import ru.prudnikova.web.TestCaseWeb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.prudnikova.specs.Specs.requestSpecJson;
import static ru.prudnikova.specs.Specs.responseSpec200;

public class AllureTest {
    private DataSource ds = DataSourceProvider.INSTANCE.getDataSource();
    CallbackPhonesManager callbackPhonesManager=new CallbackPhonesManager();

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


       countIdAndFilteredMapWhereIdNotUnique(list0);
        System.out.println();
        countIdAndFilteredMapWhereIdNotUnique(list1);
        System.out.println();
        countIdAndFilteredMapWhereIdNotUnique(list2);
        System.out.println();
        countIdAndFilteredMapWhereIdNotUnique(list3);
        System.out.println();
        countIdAndFilteredMapWhereIdNotUnique(list4); //только уникальные



    }

    public static Map<String, Integer> countId( List<String> list){
        Map<String, Integer> map = new HashMap<>();
        for (int i=0; i<list.size(); i++){
            int count=1;
            if (map.containsKey(list.get(i)))
                map.put(list.get(i), (map.get(list.get(i))+1));
            else
                map.put(list.get(i), count);
        }
        return map;
    }
    public static Map<String, Integer> filteredMapWhereIdNotUnique( HashMap<String, Integer> map){
        Map<String, Integer> filteredMap =map.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return filteredMap;
    }
    public static Map<String, Integer> countIdAndFilteredMapWhereIdNotUnique( List<String> list){
        Map<String, Integer> map = new HashMap<>();
        for (int i=0; i<list.size(); i++){
            int count=1;
            if (map.containsKey(list.get(i)))
                map.put(list.get(i), (map.get(list.get(i))+1));
            else
                map.put(list.get(i), count);
        }
        Map<String, Integer> filteredMap =map.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        filteredMap.entrySet().stream().forEach(e -> System.out.print(e.getKey() + " - " + e.getValue()+" "));
        return filteredMap;}

    @Test
    void bd(){

        String phone="19419009781";
        String result=null;
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "select name from users where phone = ?"
             )) {
            ps.setString(1, phone);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result= resultSet.getString("name");
            }
            System.out.println(result);
            Assertions.assertEquals("Прудникова Selenide", result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    void bd1(){
        String phone="75555555999";
        open("https://novo-estate.ru/");
        $x("//input[@name='phone']").setValue(phone);
        $(".one-column-callback__call-me").shouldBe(Condition.enabled).click();
        $(".phone-thanks-modal__title").shouldHave(Condition.text("Мы уже обрабатываем вашу заявку"));
        String result =callbackPhonesManager.selectLastEntryPhoneFromCallbackPhonesTables();
        Assertions.assertEquals(phone, result);
    }

    @Test
    void bd2(){
        String phone="75555555978";
        open("https://novo-estate.ru/");
        $x("//input[@name='phone']").setValue(phone);
        $(".one-column-callback__call-me").shouldBe(Condition.enabled).click();
        $(".phone-thanks-modal__title").shouldHave(Condition.text("Мы уже обрабатываем вашу заявку"));
        CallbackPhonesBD result;
        result =callbackPhonesManager.selectLastEntryFromCallbackPhonesTables();
        Assertions.assertEquals(phone, result.getPhone());
        Assertions.assertEquals("https://novo-estate.ru/", result.getLink());
    }
//    public int createOwner(Owner owner) {
//		try (Connection connection = ds.getConnection();
//			 PreparedStatement ps = connection.prepareStatement(
//				 "INSERT INTO owners (first_name, last_name, address, city, telephone)\n" +
//					 "VALUES (?, ?, ?, ?, ?)",
//				 Statement.RETURN_GENERATED_KEYS
//			 )) {
//			ps.setString(1, owner.getFirstName());
//			ps.setString(2, owner.getLastName());
//			ps.setString(3, owner.getAddress());
//			ps.setString(4, owner.getCity());
//			ps.setString(5, owner.getTelephone());
//			ps.executeUpdate();
//
//			ResultSet generatedKeys = ps.getGeneratedKeys();
//			if (generatedKeys.next()) {
//				return generatedKeys.getInt(1);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return -1;
//	}
//
//	@Override
//	public void deleteOwner(int id) {
//		try (Connection connection = ds.getConnection();
//			 PreparedStatement ps = connection.prepareStatement(
//				 "DELETE FROM owners WHERE id = ?"
//			 )) {
//			ps.setInt(1, id);
//			ps.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public List<Owner> findByLastName(String lastName) {
//		List<Owner> result = new ArrayList<>();
//
//		try (Connection connection = ds.getConnection();
//			 PreparedStatement ps = connection.prepareStatement(
//				 "SELECT * FROM owners WHERE last_name = ?"
//			 )) {
//			ps.setString(1, lastName);
//			ResultSet resultSet = ps.executeQuery();
//			while (resultSet.next()) {
//				result.add(Owner.builder()
//					.firstName(resultSet.getString("first_name"))
//					.lastName(resultSet.getString("last_name"))
//					.address(resultSet.getString("address"))
//					.city(resultSet.getString("city"))
//					.telephone(resultSet.getString("telephone"))
//					.build());
//			}
//			return result;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return Collections.emptyList();
//	}
    CallbackPhonesManagerSpring spring=new CallbackPhonesManagerSpring();

    @Test
    void bd5(){
        String phone="75555555990";
        open("https://novo-estate.ru/");
        $x("//input[@name='phone']").setValue(phone);
        $(".one-column-callback__call-me").shouldBe(Condition.enabled).click();
        $(".phone-thanks-modal__title").shouldHave(Condition.text("Мы уже обрабатываем вашу заявку"));
        String result =spring.selectLastEntryPhoneFromCallbackPhonesTables();
        System.out.println(result);
        Assertions.assertEquals(phone, result);
    }
    @Test
    void bd6(){
        String phone="75555555978";
        open("https://novo-estate.ru/");
        $x("//input[@name='phone']").setValue(phone);
        $(".one-column-callback__call-me").shouldBe(Condition.enabled).click();
        $(".phone-thanks-modal__title").shouldHave(Condition.text("Мы уже обрабатываем вашу заявку"));
        List <CallbackPhonesBD> result =spring.selectLastEntryFromCallbackPhonesTables();
        Assertions.assertEquals(phone, result.get(0).getPhone());
        Assertions.assertEquals("https://novo-estate.ru/", result.get(0).getLink());
    }

    }

