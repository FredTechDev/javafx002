package school.examinations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import school.examinations.dao.ExamResultsDAO;
import school.examinations.model.ExamResult;
import school.examinations.util.SessionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {

    @FXML private Label studentNameLabel;
    @FXML private TableView<ExamResult> resultsTable;
    @FXML private TableColumn<ExamResult, String> codeColumn;
    @FXML private TableColumn<ExamResult, String> nameColumn;
    @FXML private TableColumn<ExamResult, Integer> scoreColumn;
    @FXML private TableColumn<ExamResult, String> gradeColumn;

    private ExamResultsDAO resultsDAO = new ExamResultsDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username != null) {
            studentNameLabel.setText("Academic Record for " + username);
            
            // Set up columns
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("unitCode"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("unitName"));
            scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
            gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

            // Load data
            List<ExamResult> results = resultsDAO.getResultsForUser(username);
            ObservableList<ExamResult> observableResults = FXCollections.observableArrayList(results);
            resultsTable.setItems(observableResults);
        }
    }
}
