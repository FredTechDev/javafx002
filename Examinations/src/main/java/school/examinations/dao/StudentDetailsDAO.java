package school.examinations.dao;

import school.examinations.util.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StudentDetailsDAO {
    
    public Map<String, String> getStudentDetails(String username) {
        String sql = "SELECT * FROM student_details WHERE username=?";
        Map<String, String> details = new HashMap<>();
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    details.put("reg_number", rs.getString("reg_number"));
                    details.put("course", rs.getString("course"));
                    details.put("year_of_study", String.valueOf(rs.getInt("year_of_study")));
                    details.put("gender", rs.getString("gender"));
                    details.put("address", rs.getString("address"));
                    
                    // Added new fields
                    details.put("date_of_birth", rs.getString("date_of_birth"));
                    details.put("phone_number", rs.getString("phone_number"));
                    details.put("emergency_contact_name", rs.getString("emergency_contact_name"));
                    details.put("emergency_contact_phone", rs.getString("emergency_contact_phone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public boolean upsertStudentDetails(String username, String regNumber, String course, int yearOfStudy, 
                                        String gender, String address, String dob, String phone, 
                                        String emerName, String emerPhone) {
        String sql = "INSERT INTO student_details (username, reg_number, course, year_of_study, gender, address, date_of_birth, phone_number, emergency_contact_name, emergency_contact_phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE reg_number=?, course=?, year_of_study=?, gender=?, address=?, date_of_birth=?, phone_number=?, emergency_contact_name=?, emergency_contact_phone=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, username);
            pst.setString(2, regNumber);
            pst.setString(3, course);
            pst.setInt(4, yearOfStudy);
            pst.setString(5, gender);
            pst.setString(6, address);
            // new fields
            if (dob != null && !dob.isEmpty()) {
                pst.setDate(7, java.sql.Date.valueOf(dob));
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }
            pst.setString(8, phone);
            pst.setString(9, emerName);
            pst.setString(10, emerPhone);
            
            pst.setString(11, regNumber);
            pst.setString(12, course);
            pst.setInt(13, yearOfStudy);
            pst.setString(14, gender);
            pst.setString(15, address);
            // new fields update
            if (dob != null && !dob.isEmpty()) {
                pst.setDate(16, java.sql.Date.valueOf(dob));
            } else {
                pst.setNull(16, java.sql.Types.DATE);
            }
            pst.setString(17, phone);
            pst.setString(18, emerName);
            pst.setString(19, emerPhone);

            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
