package user;

import java.util.Random;

public class UserGenerator {

    private static final Random random = new Random();
    private static int getRandomInt() {
        return random.nextInt(100000);
    }

    public static User createDefault() {
        return new User(
                "testikmail" + getRandomInt() + "@mail.ru",
                "pass" + getRandomInt(),
                "Name" + getRandomInt()
        );
    }

    public static User createWithoutEmail() {
        User user = createDefault();
        user.setEmail(null);

        return user;
    }
    public static User createWithoutPassword() {
        User user = createDefault();
        user.setPassword(null);

        return user;
    }

    public static User createWithoutName() {
        User user = createDefault();
        user.setName(null);

        return user;
    }
}