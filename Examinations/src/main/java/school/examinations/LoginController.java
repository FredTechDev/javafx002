package school.examinations;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import school.examinations.dao.UserDAO;
import school.examinations.util.AuthService;
import school.examinations.util.SessionManager;

import java.io.IOException;

public class LoginController {
    @FXML private Button cancel;
    @FXML private Button login;
    @FXML private TextField user;
    @FXML private PasswordField pass;
    @FXML private Label error;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void handleRegister(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Register Page");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        user.clear();
        pass.clear();
        error.setText("");
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = user.getText();
        String password = pass.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password required.");
            return;
        }

        String storedHash = userDAO.getHashedPassword(username);

        if (storedHash != null && AuthService.checkPassword(password, storedHash)) {
            // Login successful
            SessionManager.getInstance().setCurrentUser(username);
            navigateToDashboard(event);
        } else {
            showError("Invalid username or password.");
        }
    }

    private void navigateToDashboard(ActionEvent event) {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("School Portal Dashboard");
            // maximize the window to fit the dashboard nicely
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.centerOnScreen();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        error.setText(msg);
        error.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        
        PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(3));
        delay.setOnFinished(e -> error.setText(""));
        delay.play();
    }

    public void initialize() {
        if (error != null) {
            error.setText("");
        }
    }
}
