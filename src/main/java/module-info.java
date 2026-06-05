module omr.uacm.sistemafitguide {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.media;
<<<<<<< HEAD
    requires java.sql;

    opens omr.uacm.sistemafitguide to javafx.fxml;
    exports omr.uacm.sistemafitguide;
    exports omr.uacm.sistemafitguide.controlador;
    opens omr.uacm.sistemafitguide.controlador to javafx.fxml;
    exports omr.uacm.sistemafitguide.excepciones;
    opens omr.uacm.sistemafitguide.excepciones to javafx.fxml;
=======

    opens omr.uacm.sistemafitguide to javafx.fxml;
    exports omr.uacm.sistemafitguide;
>>>>>>> 5412f6ef5012c127b0b6076e70790963819f6f8d
}
