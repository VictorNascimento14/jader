package com.consultoria.imagem.controller;

import com.consultoria.imagem.model.Cliente;
import com.consultoria.imagem.model.ListaDeCompras;
import com.consultoria.imagem.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GerenciarListasController implements Initializable {

    @FXML
    private Label lblClienteNome;
    @FXML
    private ListView<ListaDeCompras> listViewListas;

    private Cliente cliente;
    private ObservableList<ListaDeCompras> observableListas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListas = FXCollections.observableArrayList();
        listViewListas.setItems(observableListas);
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        lblClienteNome.setText("Listas para: " + cliente.getNome());
        carregarListasDoCliente();
    }

    private void carregarListasDoCliente() {
        if (cliente != null) {
            List<ListaDeCompras> listas = DatabaseManager.getListasByCliente(cliente.getId());
            observableListas.clear();
            observableListas.addAll(listas);
        }
    }

    @FXML
    private void abrirListaParaEdicao() {
        ListaDeCompras listaSelecionada = listViewListas.getSelectionModel().getSelectedItem();
        if (listaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma lista para editar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultoria/imagem/montar-lista.fxml"));
            Scene scene = new Scene(loader.load());

            MontarListaController controller = loader.getController();
            controller.setCliente(cliente);
            controller.setListaDeCompras(listaSelecionada); // Passa a lista existente

            Stage stage = new Stage();
            stage.setTitle("Editar Lista de " + cliente.getNome());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarListasDoCliente(); // Recarrega as listas após a edição

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir a tela de montagem de lista!\n" + e.getMessage());
        }
    }

    @FXML
    private void excluirLista() {
        ListaDeCompras listaSelecionada = listViewListas.getSelectionModel().getSelectedItem();
        if (listaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma lista para excluir!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Excluir Lista de Compras");
        alert.setContentText("Tem certeza que deseja excluir a lista \"" + listaSelecionada.getNome() + "\"?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DatabaseManager.deleteListaDeCompras(listaSelecionada.getId());
            mostrarAlerta("Sucesso", "Lista excluída com sucesso!");
            carregarListasDoCliente(); // Recarrega as listas após a exclusão
        }
    }

    @FXML
    private void fecharTela() {
        ((Stage) lblClienteNome.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
