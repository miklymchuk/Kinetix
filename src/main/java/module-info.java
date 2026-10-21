module com.mycompany.kinetix {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.kinetix to javafx.fxml;
    exports com.mycompany.kinetix;
}
