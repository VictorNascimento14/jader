package com.consultoria.imagem.controller;

import com.consultoria.imagem.model.ListaDeCompras;
import com.consultoria.imagem.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private ListView<ListaDeCompras> listViewUltimasListas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarUltimasListas();
    }

    private void carregarUltimasListas() {
        List<ListaDeCompras> listas = DatabaseManager.getAllListasDeCompras();
        ObservableList<ListaDeCompras> observableList = FXCollections.observableArrayList(listas);
        listViewUltimasListas.setItems(observableList);
    }

    @FXML
    private void abrirGerenciarClientes() {
        abrirNovaJanela("/com/consultoria/imagem/gerenciar-clientes.fxml", "Gerenciar Clientes");
    }

    @FXML
    private void abrirGerenciarPecas() {
        abrirNovaJanela("/com/consultoria/imagem/gerenciar-pecas.fxml", "Gerenciar Peças");
    }

    @FXML
    private void abrirGerenciarLojas() {
        abrirNovaJanela("/com/consultoria/imagem/gerenciar-lojas.fxml", "Gerenciar Lojas");
    }

    private void abrirNovaJanela(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

