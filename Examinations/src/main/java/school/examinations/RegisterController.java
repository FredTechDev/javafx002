package school.examinations;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import school.examinations.dao.UserDAO;
import school.examinations.util.AuthService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.Initializable;

public class RegisterController implements Initializable {
    @FXML private TextField fname;
    @FXML private TextField lname;
    @FXML private TextField email;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private PasswordField confirm;
    @FXML private Label emailError;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void handleRegister(ActionEvent event) {
        if (username.getText().isEmpty() || password.getText().isEmpty() || fname.getText().isEmpty() || lname.getText().isEmpty() || email.getText().isEmpty()) {
            showAlert("Error", "All fields are required.", Alert.AlertType.ERROR);
            return;
        }

        if (!password.getText().equals(confirm.getText())) {
            showAlert("Error", "Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        if (userDAO.usernameExists(username.getText())) {
            showAlert("Error", "Username already taken.", Alert.AlertType.ERROR);
            return;
        }

        String hashedPassword = AuthService.hashPassword(password.getText());
        boolean success = userDAO.registerUser(fname.getText(), lname.getText(), email.getText(), username.getText(), hashedPassword);

        if (success) {
            showAlert("Success", "Account created successfully! You can now login.", Alert.AlertType.INFORMATION);
            navigateToLogin(event);
        } else {
            showAlert("Error", "Database error. Could not register.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(3));
        delay.setOnFinished(e -> alert.close());
        delay.play();
    }

    private void navigateToLogin(ActionEvent event) {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Login Page");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                emailError.setText("");
                emailError.setStyle(""); 
            } else if (EMAIL_PATTERN.matcher(newValue).matches()) {
                emailError.setText("Valid email address!");
                emailError.setStyle("-fx-text-fill: green; -fx-font-size: 11px;");
                email.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
            } else {
                emailError.setText("Invalid email format.");
                emailError.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");
                email.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            }
        });
    }
}
