package com.example.passwordmanagerfx;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO {
    public static List<PasswordEntry> getAllPassword(int userId) throws SQLException {
        List<PasswordEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM passwords WHERE UserID = ?";

        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PasswordEntry entry = new PasswordEntry();
                    entry.setId(rs.getInt("id"));
                    entry.setUserID(rs.getInt("UserID"));
                    entry.setTitle(rs.getString("title"));
                    entry.setEmail(rs.getString("email"));
                    entry.setPassword(rs.getString("password"));
                    entry.setCreatedAt(rs.getTimestamp("created_at"));
                    entry.setUpdatedAt(rs.getTimestamp("updated_at"));
                    entries.add(entry);
                }
            }
        }
        return entries;
    }
    public static void addPassword(PasswordEntry entry) throws SQLException{
        String query = "Insert into passwords(UserId,title,email,password, created_at, updated_at) VALUES (?,?,?,?, ?, ?)";
        try (Connection con = DataBaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setInt(1,entry.getUserID());
            stmt.setString(2,entry.getTitle());
            stmt.setString(3,entry.getEmail());
            stmt.setString(4,entry.getPassword());
            stmt.setTimestamp(5, new Timestamp(entry.getCreatedAt().getTime()));
            stmt.setTimestamp(6, new Timestamp(entry.getUpdatedAt().getTime()));
            stmt.executeUpdate();
        }
    }
    public static void updatePassword(PasswordEntry passwordEntry) throws SQLException {
        String sql = "UPDATE passwords SET title = ?, email = ?, password = ?, updated_at = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, passwordEntry.getTitle());
            statement.setString(2, passwordEntry.getEmail());
            statement.setString(3, passwordEntry.getPassword());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(5, passwordEntry.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating password entry: " + e.getMessage(), e);
        }
    }
    public static void deletePassword(int entryId) throws SQLException {
        String sql = "DELETE FROM passwords WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entryId);
            statement.executeUpdate();
        }
    }
}
