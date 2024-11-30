package com.example.passwordmanagerfx;

public class SessionManager {
    private static int loggedInUserId;

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }
    public static void clearSession() {
        loggedInUserId = -1;
    }
}
