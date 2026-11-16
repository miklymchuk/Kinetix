module com.mycompany.kinetix {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.media;

    opens com.mycompany.kinetix to javafx.fxml;
    exports com.mycompany.kinetix;
}
