package dbataev.nextcode.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
}
