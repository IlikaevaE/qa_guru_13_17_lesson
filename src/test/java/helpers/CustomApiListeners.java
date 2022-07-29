package helpers;

import io.qameta.allure.restassured.AllureRestAssured;

// здесь мы переписываеим шаблоны restAssured на более красивые

public class CustomApiListeners {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    public static AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;
    }
}
