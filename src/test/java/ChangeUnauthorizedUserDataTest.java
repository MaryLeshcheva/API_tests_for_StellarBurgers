import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import user.User;
import user.UserClient;
import user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class ChangeUnauthorizedUserDataTest {

    private UserClient userClient;
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.createDefault();
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Изменение имени неавторизированного пользователя")
    public void unauthorizedUserCantChangeName() {
        user.setName("New_Name");
        ValidatableResponse responseUpdateUserData = userClient.updateUnauthorizedUser(user);
        int actualStatusCode = responseUpdateUserData.extract().statusCode();
        boolean isUserDataChanged = responseUpdateUserData.extract().path("success");
        String message = responseUpdateUserData.extract().path("message");
        assertEquals("Ожидается 401", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse("Имя обновлено", isUserDataChanged);
        assertEquals("You should be authorised", message);
    }

    @Test
    @DisplayName("Изменение email неавторизованного пользователя")
    public void unauthorizedUserCannotChangeEmail() {
        user.setEmail("mariya@gmail.com");
        ValidatableResponse responseUpdateUserData = userClient.updateUnauthorizedUser(user);
        int actualStatusCode = responseUpdateUserData.extract().statusCode();
        boolean isUserDataChanged = responseUpdateUserData.extract().path("success");
        String message = responseUpdateUserData.extract().path("message");
        assertEquals("Ожидается 401", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse("Email обновлён", isUserDataChanged);
        assertEquals("You should be authorised", message);
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    public void unauthorizedUserCannotChangePassword() {
        user.setPassword("passsssss11");
        ValidatableResponse responseUpdateUserData = userClient.updateUnauthorizedUser(user);
        int actualStatusCode = responseUpdateUserData.extract().statusCode();
        boolean isUserDataChanged = responseUpdateUserData.extract().path("success");
        String message = responseUpdateUserData.extract().path("message");
        assertEquals("Ожидается 401", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse("Пароль обновлён", isUserDataChanged);
        assertEquals("You should be authorised", message);
    }
}
