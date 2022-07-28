package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class DemowebshopTests {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

    @Test
    void addToNewCardAsAnonymTest() {

        String body = "addtocart_31.EnteredQuantity=1";
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body(body) // data-raw
                .log().all()
                .when()
                .post("/addproducttocart/details/31/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is(("(1)")));

    }

    @Test
    void addToOldCardAsAnonymTest() {
        /*
        curl 'http://demowebshop.tricentis.com/addproducttocart/details/31/1' \
        -H 'Accept: *//*' \
        -H 'Accept-Language: ru,en-US;q=0.9,en;q=0.8,de;q=0.7' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
        -H 'Cookie: Nop.customer=0b41bedd-2e27-407e-8237-a694a497fdc6; __utmc=78382081; __utmz=78382081.1658924488.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=31; __utma=78382081.2144872429.1658924488.1658924488.1658932900.2; __atuvc=4%7C30; __atuvs=62e14ea3f0d6fb51001; __utmt=1; __utmb=78382081.2.10.1658932900' \
        -H 'Origin: http://demowebshop.tricentis.com' \
        -H 'Referer: http://demowebshop.tricentis.com/141-inch-laptop' \
        -H 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36' \
        -H 'X-KL-Ajax-Request: Ajax_Request' \
        -H 'X-Requested-With: XMLHttpRequest' \
        --data-raw 'addtocart_31.EnteredQuantity=1' \
        --compressed \
        --insecure
        */


        // Это функциональный код нажатия кнопки Add to cart на странице demowebshop
        String body = "addtocart_31.EnteredQuantity=1";
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=0b41bedd-2e27-407e-8237-a694a497fdc6;") // для незарегистрированного пользователя
                .body(body) // data-raw
                .log().all()
                .when()
                .post("/addproducttocart/details/31/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"));

    }

    @Test
    void addToOldCardAsAuthorizedTest() {

        // Nop.customer=0b41bedd-2e27-407e-8237-a694a497fdc6;

        // NOPCOMMERCE.AUTH=ACA02E534986AE9EF75B1ADE8881E6CFBDFC24D3C4C2C3F79524C0B2F699ABAC9E9A998230704871032A1117E56A06FD1671F8C796B36C037372D854A36548A1F7E4AB30592B6FD401997CD441F2E1F78BCC0B02D13EE19E1F25DCBF8BAF004A649DCB434E4E9AC65AEA8089AAD26C708A1FA1B4311AF41A33960241A230B878C7782C4E24F8DDEA5E9DCE1695A635FB;

        // ввыполнить запрос на авторизацию и получить инфо


        String authCookie = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("Email", "rest.api@net.com") // это body передаем из Network на странице
                .formParam("Password", "@DPm2E!nG97mL")
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie("NOPCOMMERCE.AUTH");

        String body = "addtocart_31.EnteredQuantity=1";
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                //.cookie("NOPCOMMERCE.AUTH", "ACA02E534986AE9EF75B1ADE8881E6CFBDFC24D3C4C2C3F79524C0B2F699ABAC9E9A998230704871032A1117E56A06FD1671F8C796B36C037372D854A36548A1F7E4AB30592B6FD401997CD441F2E1F78BCC0B02D13EE19E1F25DCBF8BAF004A649DCB434E4E9AC65AEA8089AAD26C708A1FA1B4311AF41A33960241A230B878C7782C4E24F8DDEA5E9DCE1695A635FB")
                // теперь из запроса выше мы заменим длинный ключ на authCookie
                .cookie("NOPCOMMERCE.AUTH", authCookie)
                .body(body) // data-raw
                .log().all()
                .when()
                .post("/addproducttocart/details/31/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"));

    }

    @Test
    void addToOldCardAsAuthorizedSizeInWebTest() {
        String authCookieName = "NOPCOMMERCE.AUTH";
        String authCookieValue = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("Email", "rest.api@net.com") // это body передаем из Network на странице
                .formParam("Password", "@DPm2E!nG97mL")
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie(authCookieName);


        String body = "addtocart_31.EnteredQuantity=1";
        String cartSize = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                //.cookie("NOPCOMMERCE.AUTH", "ACA02E534986AE9EF75B1ADE8881E6CFBDFC24D3C4C2C3F79524C0B2F699ABAC9E9A998230704871032A1117E56A06FD1671F8C796B36C037372D854A36548A1F7E4AB30592B6FD401997CD441F2E1F78BCC0B02D13EE19E1F25DCBF8BAF004A649DCB434E4E9AC65AEA8089AAD26C708A1FA1B4311AF41A33960241A230B878C7782C4E24F8DDEA5E9DCE1695A635FB")
                // теперь из запроса выше мы заменим длинный ключ на authCookie
                .cookie(authCookieName, authCookieValue)
                .body(body) // data-raw
                .log().all()
                .when()
                .post("/addproducttocart/details/31/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .extract()
                .path("updatetopcartsectionhtml"); // мы будем проверять это значение в path

        // Далее мы открываем браузер и эта страница будет уже авторизированна
        open("/Themes/DefaultClean/Content/images/logo.png");
        // Далее мы подложим браузеру куку
        Cookie authCookie = new Cookie(authCookieName, authCookieValue);
        //
        WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        // Мы хотим открыть страницу и выполнить проверку
        open("");
        $(".cart-qty").shouldHave(Condition.text(cartSize));

    }
}
