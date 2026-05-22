package school.examinations;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {
    @FXML private TextField fname;
    @FXML private TextField lname;
    @FXML private TextField email;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField confirm;
    @FXML private Label emailError;
    // RFC 5322 official standard regex for email validation
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public void handleRegister(ActionEvent event) throws SQLException, IOException {
        Connection con = Connect.connection();
        String sql = "INSERT INTO users VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, fname.getText());
        pst.setString(2, lname.getText());
        pst.setString(3, email.getText());
        pst.setString(4, username.getText());
        pst.setString(5, password.getText());
        String sqluser = "SELECT username FROM users WHERE username=?";
        PreparedStatement pstuser = con.prepareStatement(sqluser);
        pstuser.setString(1, username.getText());
        ResultSet rs = pstuser.executeQuery();
        if (!rs.next()) {
            if(confirm.getText().equals(password.getText()))
            {
                int x =  pst.executeUpdate();
                if (x > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully registered");
                    alert.show();
                    //set duration of alert to 3 seconds
                    PauseTransition delay = new PauseTransition();
                    delay.setDuration(javafx.util.Duration.seconds(3));
                    delay.setOnFinished(e -> {alert.close();});
                    delay.play();

                    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    stage.setTitle("Login Page");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                    Parent root = loader.load();

                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to register");
                    alert.show();
                    //set duration of alert to 3 seconds
                    PauseTransition delay = new PauseTransition();
                    delay.setDuration(javafx.util.Duration.seconds(3));
                    delay.setOnFinished(e -> {alert.close();});
                    delay.play();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText(null);
                alert.setContentText("Password and confirm password doesn't match");
                alert.show();
                //set duration of alert to 3 seconds
                PauseTransition delay = new PauseTransition();
                delay.setDuration(javafx.util.Duration.seconds(3));
                delay.setOnFinished(e -> {alert.close();});
                delay.play();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText(username.getText() + " username not available");
            alert.show();
            //set duration of alert to 3 seconds
            PauseTransition delay = new PauseTransition();
            delay.setDuration(javafx.util.Duration.seconds(3));
            delay.setOnFinished(e -> {alert.close();});
            delay.play();
        }



    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add listener for real-time validation as the user types
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                emailError.setText("");
                emailError.setStyle(""); // Reset to default style
            } else if (validateEmail(newValue)) {
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
    /**
     * Validates the input string against the email regex pattern.
     */
    private boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
