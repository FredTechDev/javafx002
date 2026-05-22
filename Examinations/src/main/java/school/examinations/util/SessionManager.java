package school.examinations.util;

public class SessionManager {
    private static SessionManager instance;
    private String currentUsername;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUser() {
        return currentUsername;
    }

    public void logout() {
        this.currentUsername = null;
    }
}
