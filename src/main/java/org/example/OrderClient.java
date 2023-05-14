package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String PATH = "/api/orders/";

    @Step("Создать заказ с авторизованным пользователем")
    public ValidatableResponse createAuthorizedUserOrder(String accessToken, Order order) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Создать заказ неавторизованным пользователем")
    public ValidatableResponse createUnauthorizedUserOrder(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Создать заказ без ингредиентов")
    public ValidatableResponse createOrderWithoutIngredients(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Получить заказ от авторизованного пользователя")
    public ValidatableResponse getAuthorizedUserOrders(String accessToken, User user) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .get(PATH)
                .then();
    }

    @Step("Получить заказ от неавторизованного пользователя")
    public ValidatableResponse getUnauthorizedUserOrders(User user) {
        return given()
                .spec(getSpec())
                .when()
                .get(PATH)
                .then();
    }
}
