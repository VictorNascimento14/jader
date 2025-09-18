package com.consultoria.imagem.controller;

import com.consultoria.imagem.model.Loja;
import com.consultoria.imagem.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarLojasController implements Initializable {

    @FXML
    private ListView<Loja> listViewLojas;

    @FXML
    private TextField txtNomeLoja;

    @FXML
    private TextField txtEnderecoLoja;

    @FXML
    private TextField txtContatoLoja;

    private Loja lojaEditando = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarLojas();
        
        // Listener para seleção na lista
        listViewLojas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                preencherCampos(newValue);
            }
        });
    }

    private void carregarLojas() {
        List<Loja> lojas = DatabaseManager.getAllLojas();
        ObservableList<Loja> observableList = FXCollections.observableArrayList(lojas);
        listViewLojas.setItems(observableList);
    }

    private void preencherCampos(Loja loja) {
        txtNomeLoja.setText(loja.getNome());
        txtEnderecoLoja.setText(loja.getEndereco());
        txtContatoLoja.setText(loja.getContato());
        lojaEditando = loja;
    }

    @FXML
    private void salvarLoja() {
        String nome = txtNomeLoja.getText().trim();
        String endereco = txtEnderecoLoja.getText().trim();
        String contato = txtContatoLoja.getText().trim();

        if (nome.isEmpty()) {
            mostrarAlerta("Erro", "O nome da loja é obrigatório!");
            return;
        }

        if (lojaEditando == null) {
            // Adicionar nova loja
            Loja novaLoja = new Loja(nome, endereco, contato);
            DatabaseManager.addLoja(novaLoja);
            mostrarAlerta("Sucesso", "Loja adicionada com sucesso!");
        } else {
            // Editar loja existente
            lojaEditando.setNome(nome);
            lojaEditando.setEndereco(endereco);
            lojaEditando.setContato(contato);
            DatabaseManager.updateLoja(lojaEditando);
            mostrarAlerta("Sucesso", "Loja atualizada com sucesso!");
        }

        limparCampos();
        carregarLojas();
    }

    @FXML
    private void editarLoja() {
        Loja lojaSelecionada = listViewLojas.getSelectionModel().getSelectedItem();
        if (lojaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma loja para editar!");
            return;
        }
        preencherCampos(lojaSelecionada);
    }

    @FXML
    private void excluirLoja() {
        Loja lojaSelecionada = listViewLojas.getSelectionModel().getSelectedItem();
        if (lojaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma loja para excluir!");
            return;
        }

        DatabaseManager.deleteLoja(lojaSelecionada.getId());
        mostrarAlerta("Sucesso", "Loja excluída com sucesso!");
        limparCampos();
        carregarLojas();
    }

    @FXML
    private void limparCampos() {
        txtNomeLoja.clear();
        txtEnderecoLoja.clear();
        txtContatoLoja.clear();
        lojaEditando = null;
        listViewLojas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

