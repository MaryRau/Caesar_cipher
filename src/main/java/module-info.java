module org.example.encoder_decoder {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;

    opens org.example.encoder_decoder to javafx.fxml;
    exports org.example.encoder_decoder;
}