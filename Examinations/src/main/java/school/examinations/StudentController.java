package school.examinations;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import school.examinations.dao.StudentDetailsDAO;
import school.examinations.dao.ExamResultsDAO;
import school.examinations.util.SessionManager;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private TextField regNumberField;
    @FXML private TextField courseField;
    @FXML private TextField yearField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private DatePicker dobPicker;
    @FXML private TextField phoneField;
    @FXML private TextField emerNameField;
    @FXML private TextField emerPhoneField;
    @FXML private TextArea addressArea;
    @FXML private Label statusLabel;

    private String currentUsername;
    private StudentDetailsDAO studentDAO = new StudentDetailsDAO();
    private ExamResultsDAO examDAO = new ExamResultsDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUsername = SessionManager.getInstance().getCurrentUser();
        if (currentUsername != null) {
            welcomeLabel.setText("Profile details for " + currentUsername);
            loadExistingDetails();
        }
    }

    private void loadExistingDetails() {
        Map<String, String> details = studentDAO.getStudentDetails(currentUsername);
        
        if (!details.isEmpty()) {
            regNumberField.setText(details.get("reg_number"));
            courseField.setText(details.get("course"));
            yearField.setText(details.get("year_of_study"));
            genderCombo.setValue(details.get("gender"));
            addressArea.setText(details.get("address"));
            
            String dobStr = details.get("date_of_birth");
            if (dobStr != null && !dobStr.isEmpty()) {
                dobPicker.setValue(LocalDate.parse(dobStr));
            }
            phoneField.setText(details.get("phone_number"));
            emerNameField.setText(details.get("emergency_contact_name"));
            emerPhoneField.setText(details.get("emergency_contact_phone"));
            
            showStatus("Existing details loaded.", Color.GREEN);
        }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String reg = regNumberField.getText();
        String course = courseField.getText();
        String yearStr = yearField.getText();
        String gender = genderCombo.getValue();
        String address = addressArea.getText();
        LocalDate dob = dobPicker.getValue();
        String dobString = dob != null ? dob.toString() : null;
        String phone = phoneField.getText();
        String emerName = emerNameField.getText();
        String emerPhone = emerPhoneField.getText();

        if (reg == null || reg.isEmpty() || 
            course == null || course.isEmpty() || 
            yearStr == null || yearStr.isEmpty() || 
            gender == null || address == null || address.isEmpty() ||
            dob == null || phone == null || phone.isEmpty() ||
            emerName == null || emerName.isEmpty() || emerPhone == null || emerPhone.isEmpty()) {
            showStatus("All fields are required.", Color.RED);
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            showStatus("Year of study must be a number.", Color.RED);
            return;
        }

        boolean success = studentDAO.upsertStudentDetails(currentUsername, reg, course, year, gender, address, dobString, phone, emerName, emerPhone);
        
        if (success) {
            showStatus("Details saved successfully!", Color.GREEN);
            // Seed dummy exam results if this is the first successful save
            examDAO.seedDummyGradesIfEmpty(currentUsername);
        } else {
            showStatus("Failed to save details.", Color.RED);
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);
        
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> statusLabel.setText(""));
        delay.play();
    }
}
