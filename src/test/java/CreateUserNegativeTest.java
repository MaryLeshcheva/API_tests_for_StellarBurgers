import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import user.User;
import user.UserClient;
import user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CreateUserNegativeTest {
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
    public void cleanUp(){
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание дублированного пользователя")
    public void userCannotBeCreatedTwice() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        int actualStatusCode = responseCreate.extract().statusCode();
        boolean isUserCreated = responseCreate.extract().path("success");
        String actualMessage = responseCreate.extract().path("message" );
        assertEquals("Ожидается 403", SC_FORBIDDEN, actualStatusCode);
        assertFalse(isUserCreated);
        assertEquals("User already exists", actualMessage);
    }
}
