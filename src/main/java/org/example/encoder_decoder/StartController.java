package org.example.encoder_decoder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StartController {
    StringBuilder stringBuilder = new StringBuilder();

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button btnUpload;

    @FXML
    private TextField txtDecoded;

    @FXML
    private Button btnSave;

    @FXML
    private URL location;

    @FXML
    private Button btnBruteForce;

    @FXML
    private Button btnStandartEncoder;

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
    void initialize() {
        assert btnBruteForce != null : "fx:id=\"btnBruteForce\" was not injected: check your FXML file 'start.fxml'.";
        assert btnStandartEncoder != null : "fx:id=\"btnStandartEncoder\" was not injected: check your FXML file 'start.fxml'.";
        assert btnUpload != null : "fx:id=\"btnUpload\" was not injected: check your FXML file 'start.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'start.fxml'.";
        assert txtDecoded != null : "fx:id=\"txtDecoded\" was not injected: check your FXML file 'start.fxml'.";

    }

    public void toStandartEncr(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("standartEncryption.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> bruteForceDecryption(String text) {
        List<String> decryptedTexts = new ArrayList<>();
        boolean hasRussianChars = false;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c) && ((c >= 'а' && c <= 'я') || (c >= 'А' && c <= 'Я'))) {
                hasRussianChars = true;
                break;
            }
        }

        int maxKey = hasRussianChars ? 32 : 26;

        for (int key = 0; key < maxKey; key++) {
            decryptedTexts.add(encryption(text, key));
        }

        return decryptedTexts;
    }

    private static String encryption(String text, int key) {
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

    @FXML
    void saveFile(ActionEvent event) {
        if (!stringBuilder.isEmpty()) {
            try {
                StringBuilder decryptedTextBuilder = new StringBuilder();
                for (String text : bruteForceDecryption(stringBuilder.toString())) {
                    decryptedTextBuilder.append(text).append("\n");
                }

                Path downloadsDir = Paths.get(System.getProperty("user.home"), "Downloads");
                Path outputPath = downloadsDir.resolve("decrypted_text.txt");
                int fileCount = 1;
                while (Files.exists(outputPath)) {
                    outputPath = downloadsDir.resolve(String.format("decrypted_text_%d.txt", fileCount++));
                }

                Files.writeString(outputPath, decryptedTextBuilder.toString());
                System.out.println("Расшифрованный текст сохранен в файл: " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}