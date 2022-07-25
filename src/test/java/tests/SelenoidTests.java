package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {
    /*
       1. make request to https://selenoid.autotests.cloud/status
       2. get response {"total":20,"used":0,"queued":0,"pending":0,"browsers":{"android":{"8.1":{}},"chrome":
       {"100.0":{},"99.0":{}},"chrome-mobile":{"86.0":{}},"firefox":{"97.0":{},"98.0":{}},"opera":{"84.0":{},"85.0":{}}}}
       3. check total is 20
    */

    @Test
    void checkTotal() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)  // проверка
                .body("total", is(20)); // проверка
    }

    @Test
    void checkTotalWithGiven() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)  // проверка
                .body("total", is(20)); // проверка
    }

    @Test
    void checkTotalWithLogs() {
        given()
                .log().all() // отправляем запрос
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all() // получаем ответ
                .statusCode(200)  // проверка
                .body("total", is(20)); // проверка
    }

    @Test
    void checkChromeVersion() {
        given()
                .log().all() // отправляем запрос
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all() // получаем ответ
                .statusCode(200)  // проверка
                .body("browsers.chrome", hasKey("100.0")); // проверка
    }

    @Test
    void checkTotalWithSomeLogs() {
        given()
                .log().uri() // отправляем запрос
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status() // получаем ответ
                .log().body()
                .statusCode(200)  // проверка
                .body("total", is(20)); // проверка
    }

    @Test
    void checkResponseBadPractice() { // такое написание тестов нужно избегать
        String expectedResponseString = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"browsers\":{\"android\":{\"8.1\":{}},\"chrome\":{\"100.0\":{},\"99.0\":{}},\"chrome-mobile\":{\"86.0\":{}},\"firefox\":{\"97.0\":{},\"98.0\":{}},\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";

        Response actualResponse = given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response();

        System.out.println(actualResponse);
        String actualResponseString = actualResponse.asString();
        System.out.println(actualResponseString);

        assertEquals(expectedResponseString, actualResponseString);

    }

    @Test
    void checkTotalGoodPractice() {
        Integer expectedTotal = 20;
        Integer actualTotal = given()
                .log().uri() // отправляем запрос
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status() // получаем ответ
                .log().body()
                .statusCode(200)  // проверка
                .extract()
                .path("total");

        assertEquals(expectedTotal, actualTotal);
    }

     /*
        1. make request to https://selenoid.autotests.cloud/wd/hub/status
        2. get response {"value":{"message":"Selenoid 1.10.7 built at 2021-11-21_05:46:32AM","ready":true}}
        3. check value.ready is true
     */

     @Test
     void check401WdHubStatus() {
         given()
                 .log().uri() // отправляем запрос
                 .log().body()
                 .when()
                 .get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                 .then()
                 .log().status() // получаем ответ
                 .log().body()
                 .statusCode(401);  // проверка

     }

    @Test
    void checkWdHubStatusWithAuthInUrl() {
        given()
                .log().uri() // отправляем запрос
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status() // получаем ответ
                .log().body()
                .statusCode(200)  // проверка
                .body("value.ready", is(true)); // проверка
    }

    @Test
    void checkWdHubStatus() {
        given()
                .auth().basic("user1", "1234")
                .log().all() // отправляем запрос
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status() // получаем ответ
                .log().body()
                .statusCode(200)  // проверка
                .body("value.ready", is(true)); // проверка
    }
}
