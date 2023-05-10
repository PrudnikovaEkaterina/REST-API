package ru.prudnikova.web;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class TestCaseWeb {

    public static void setCookiesToBrowser(String xsrfToken, String allureTestopsSession){
    open("https://allure.autotests.cloud/favicon.ico");
    Cookie cookie=new Cookie("XSRF-TOKEN", xsrfToken);
    Cookie cookie1=new Cookie("ALLURE_TESTOPS_SESSION", allureTestopsSession);
    getWebDriver().manage().addCookie(cookie);
    getWebDriver().manage().addCookie(cookie1);
    }

    public TestCaseWeb refactorDescription(int idTestCase, String description){
        open("https://allure.autotests.cloud/project/2232/test-cases/"+idTestCase);
        $x("//div[text()='Description']//ancestor::section//descendant::button").click();
        $x("//textarea[@name='description']").setValue(description);
        $x("//span[text()='Submit']").click();
        $x("//div[@class='MarkdownArticle']").shouldHave(Condition.text(description));
        return this;
    }

    public TestCaseWeb verifyDescription (String description){
        $x("//div[@class='MarkdownArticle']").shouldHave(Condition.text(description));
        return this;
    }
}
