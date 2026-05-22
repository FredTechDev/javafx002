package school.examinations;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import school.examinations.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private BorderPane mainPane;
    @FXML private Label userLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            userLabel.setText("Logged in as: " + currentUser);
        }
        
        // Load default view (Student Details)
        loadView("StudentDetails.fxml");
    }

    @FXML
    public void handleProfile(ActionEvent event) {
        loadView("StudentDetails.fxml");
    }

    @FXML
    public void handleResults(ActionEvent event) {
        loadView("Results.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
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

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent view = loader.load();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
