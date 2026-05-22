module school.examinations {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires com.zaxxer.hikari;

    opens school.examinations to javafx.fxml;
    opens school.examinations.model to javafx.base, javafx.fxml;
    opens school.examinations.dao to javafx.fxml;
    opens school.examinations.util to javafx.fxml;

    exports school.examinations;
    exports school.examinations.model;
}