package com.consultoria.imagem;

import com.consultoria.imagem.util.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database
        DatabaseManager.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/consultoria/imagem/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Consultoria de Imagem - Sistema de Gestão");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
