package org.example.encoder_decoder;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StandartEncryptionController {
    StringBuilder stringBuilder = new StringBuilder();

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text txtGetKey;

    @FXML
    private Text txtEncodedText;

    @FXML
    private Button btnCoding;

    @FXML
    private TextField txtEncoded;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnBruteForce;

    @FXML
    private Button btnDownload;

    @FXML
    private Button btnStandartEncoder;

    @FXML
    private Button btnUpload;

    @FXML
    public TextField txtKey;

    @FXML
    void uploadFile(ActionEvent event) {
        if (!stringBuilder.isEmpty())
            stringBuilder.delete(0, stringBuilder.length());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите текстовый файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());

        if (selectedFile != null) {
            try {
                stringBuilder.append(Files.readString(selectedFile.toPath()));
                System.out.println("Содержимое файла:\n" + stringBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void saveFile(ActionEvent actionEvent) {
        if (txtKey.getText().length() > 0) {
            try {
                int key = Integer.parseInt(txtKey.getText());
                String encryptedText = encryption(stringBuilder.toString(), key);
                System.out.println("Зашифрованный текст:\n" + encryptedText);

                Path downloadsDir = Paths.get(System.getProperty("user.home"), "Downloads");
                Path outputPath = downloadsDir.resolve("encrypted_text.txt");
                int fileCount = 1;
                while (Files.exists(outputPath)) {
                    outputPath = downloadsDir.resolve(String.format("encrypted_text_%d.txt", fileCount++));
                }

                Files.writeString(outputPath, encryptedText);
                System.out.println("Зашифрованный текст сохранен в файл: " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void initialize() {
        assert btnBruteForce != null : "fx:id=\"btnBruteForce\" was not injected: check your FXML file 'standartEncryption.fxml'.";
        assert btnDownload != null : "fx:id=\"btnDownload\" was not injected: check your FXML file 'standartEncryption.fxml'.";
        assert btnStandartEncoder != null : "fx:id=\"btnStandartEncoder\" was not injected: check your FXML file 'standartEncryption.fxml'.";
        assert btnUpload != null : "fx:id=\"btnUpload\" was not injected: check your FXML file 'standartEncryption.fxml'.";
        assert btnCoding != null : "fx:id=\"btnCoding\" was not injected: check your FXML file 'standartEncryption.fxml'.";
        assert txtGetKey != null : "fx:id=\"txtGetKey\" was not injected: check your FXML file 'standartEncryption.fxml'.";
        assert txtEncodedText!= null : "fx:id=\"txtGetKey\" was not injected: check your FXML file 'standartEncryption.fxml'.";
    }

    String encryption(String text, int key) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int baseCode;
                int mod;
                if (Character.toLowerCase(c) >= 'а' && Character.toLowerCase(c) <= 'я') {
                    baseCode = Character.isUpperCase(c) ? 'А' : 'а';
                    mod = 32;
                } else if (Character.toLowerCase(c) >= 'a' && Character.toLowerCase(c) <= 'z') {
                    baseCode = Character.isUpperCase(c) ? 'A' : 'a';
                    mod = 26;
                } else {
                    sb.append(c);
                    continue;
                }
                int shiftedCode = ((c - baseCode + key) % mod + mod) % mod + baseCode;
                sb.append((char) shiftedCode);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void clickBtnCoding(ActionEvent actionEvent) {
        txtEncoded.setText(encryption(stringBuilder.toString(), Integer.parseInt(txtKey.getText())));
    }

    public void toBruteForce(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("start.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}