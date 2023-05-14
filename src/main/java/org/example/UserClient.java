package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String REGISTER_PATH = "api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH)
                .then();
    }
    @Step("Обновление данных авторизированного пользователя")
    public ValidatableResponse updateAuthorizedUser(String accessToken, User user) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_PATH)
                .then();
    }

    @Step("Обновление данных неавторизированного пользователя")
    public ValidatableResponse updateUnauthorizedUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(USER_PATH)
                .then();
    }
}
