package school.examinations.dao;

import school.examinations.model.ExamResult;
import school.examinations.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamResultsDAO {

    public List<ExamResult> getResultsForUser(String username) {
        List<ExamResult> results = new ArrayList<>();
        String sql = "SELECT * FROM exam_results WHERE username=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    results.add(new ExamResult(
                        rs.getString("unit_code"),
                        rs.getString("unit_name"),
                        rs.getInt("score"),
                        rs.getString("grade")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void seedDummyGradesIfEmpty(String username) {
        if (!getResultsForUser(username).isEmpty()) {
            return; // Already has results
        }
        
        String sql = "INSERT INTO exam_results (username, unit_code, unit_name, score, grade) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            Object[][] dummyData = {
                {"CS101", "Introduction to Programming", 85, "A"},
                {"MATH101", "Calculus I", 78, "B+"},
                {"ENG101", "Academic Writing", 92, "A"},
                {"PHY101", "Mechanics", 65, "C"},
                {"CS102", "Database Systems", 88, "A"}
            };
            
            for (Object[] data : dummyData) {
                pst.setString(1, username);
                pst.setString(2, (String) data[0]);
                pst.setString(3, (String) data[1]);
                pst.setInt(4, (Integer) data[2]);
                pst.setString(5, (String) data[3]);
                pst.addBatch();
            }
            
            pst.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
