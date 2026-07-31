package praktikum.utils;

import praktikum.model.User;
import java.util.UUID;

public class UserGenerator {
    public static User getRandomUser() {
        return new User(UUID.randomUUID() + "@mail.com", "password123", "TestUser");
    }
}
