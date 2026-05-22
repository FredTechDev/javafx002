package school.examinations.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExamResult {
    private final SimpleStringProperty unitCode;
    private final SimpleStringProperty unitName;
    private final SimpleIntegerProperty score;
    private final SimpleStringProperty grade;

    public ExamResult(String unitCode, String unitName, int score, String grade) {
        this.unitCode = new SimpleStringProperty(unitCode);
        this.unitName = new SimpleStringProperty(unitName);
        this.score = new SimpleIntegerProperty(score);
        this.grade = new SimpleStringProperty(grade);
    }

    public String getUnitCode() { return unitCode.get(); }
    public String getUnitName() { return unitName.get(); }
    public int getScore() { return score.get(); }
    public String getGrade() { return grade.get(); }
}
