import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserClient;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class ChangeAuthorizedUserDataTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.createDefault();
        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Изменение имени авторизированного пользователя")
    public void authorizedUserCanChangeName() {
        String expectedNewName = "New_Name";
        user.setName(expectedNewName);
        ValidatableResponse responseUpdateUserData = userClient.updateAuthorizedUser(accessToken, user);
        int actualStatusCode = responseUpdateUserData.extract().statusCode();
        boolean isUserDataChanged = responseUpdateUserData.extract().path("success");
        String actualNewName = responseUpdateUserData.extract().path("user.name");
        assertEquals("Неверный код состояния", actualStatusCode, SC_OK);
        assertTrue("Имя не обновилось", isUserDataChanged);
        assertEquals("Ожидается другое имя", expectedNewName, actualNewName);
    }

    @Test
    @DisplayName("Изменение email авторизированного пользователя")
    public void authorizedUserCanChangeEmail() {
        String expectedNewEmail = "new-name@gmail.com";
        user.setEmail(expectedNewEmail);
        ValidatableResponse responseUpdateUserData = userClient.updateAuthorizedUser(accessToken, user);
        int actualStatusCode = responseUpdateUserData.extract().statusCode();
        boolean isUserDataChanged = responseUpdateUserData.extract().path("success");
        String actualNewEmail = responseUpdateUserData.extract().path("user.email");
        assertEquals("Неверный код состояния", actualStatusCode, SC_OK);
        assertTrue("Email не обновился", isUserDataChanged);
        assertEquals("Ожидается другой Email", expectedNewEmail, actualNewEmail);
    }

    @Test
    @DisplayName("Изменение пароля авторизированного пользователя")
    public void authorizedUserCanChangePassword(){
        user.setPassword("3615qtkjn");
        ValidatableResponse responseUpdateUserData = userClient.updateAuthorizedUser(accessToken, user);
        int actualUpdateUserStatusCode = responseUpdateUserData.extract().statusCode();
        boolean isUserDataChanged = responseUpdateUserData.extract().path("success");
        assertEquals("Неверный код состояния", actualUpdateUserStatusCode, SC_OK);
        assertTrue("Пароль не обновился", isUserDataChanged);

        ValidatableResponse responseLoginWithNewPassword = userClient.login(user);
        int actualLoginStatusCode = responseLoginWithNewPassword.extract().statusCode();
        boolean isUserLoggedIn = responseLoginWithNewPassword.extract().path("success");
        assertEquals("Неверный код авторизации", actualLoginStatusCode, SC_OK);
        assertTrue("Пользователь не авторизирован", isUserLoggedIn);
    }
}
