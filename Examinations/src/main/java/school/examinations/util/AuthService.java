package school.examinations.util;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    /**
     * Hashes a plaintext password using BCrypt.
     */
    public static String hashPassword(String plaintextPassword) {
        return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt(12));
    }

    /**
     * Checks if the plaintext password matches the hashed password.
     */
    public static boolean checkPassword(String plaintextPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false; // Not a valid bcrypt hash
        }
        return BCrypt.checkpw(plaintextPassword, hashedPassword);
    }
}
