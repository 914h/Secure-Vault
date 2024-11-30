package com.example.passwordmanagerfx;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static List<User> GetUser() throws SQLException {
        List<User> entries = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                User entry = new User();
                entry.setId(rs.getInt("id"));
                entry.setName(rs.getString("name"));
                entry.setPassword(rs.getString("password"));
                entries.add(entry);
            }
        }
        return entries;
    }
    public static boolean ValidatedUser(String email, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE email = ?";
        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    return BCrypt.checkpw(password, storedHashedPassword);
                }
            }
        }
        return false;
    }
    public static boolean ValidatedInsertUser(String name, String email, String password) throws SQLException {
        String hashedPassword = hashPassword(password);

        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public static int getUserIdByEmail(String email) throws SQLException {
        String query = "SELECT id FROM users WHERE email = ?";
        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
             stmt.setString(1, email);
             try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("User not found with the provided email.");
    }
    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static void upgradeExistingPasswords() throws SQLException {
        String query = "SELECT id, email, password FROM users";
        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("id");
                String email = rs.getString("email");
                String currentPassword = rs.getString("password");

                // Only hash if not already hashed
                if (!currentPassword.startsWith("$2a$")) {
                    String hashedPassword = BCrypt.hashpw(currentPassword, BCrypt.gensalt());

                    // Update the password in the database
                    String updateQuery = "UPDATE users SET password = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setInt(2, userId);
                        updateStmt.executeUpdate();
                    }
                }
            }
        }
    }
    private static boolean checkPassword(String password, String storedHashedPassword) {
        return BCrypt.checkpw(password, storedHashedPassword);
    }
}

