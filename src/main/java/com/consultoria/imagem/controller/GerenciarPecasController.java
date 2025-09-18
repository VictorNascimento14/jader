package com.consultoria.imagem.controller;

import com.consultoria.imagem.model.Loja;
import com.consultoria.imagem.model.Peca;
import com.consultoria.imagem.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarPecasController implements Initializable {

    @FXML
    private ListView<Peca> listViewPecas;

    @FXML
    private TextField txtNomePeca;

    @FXML
    private TextField txtDescricaoPeca;

    @FXML
    private ComboBox<Loja> comboLojas;

    @FXML
    private TextField txtValorPeca;

    @FXML
    private TextField txtFotoPeca;

    @FXML
    private TextArea txtObservacoesPeca;

    private Peca pecaEditando = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarLojas();
        carregarPecas();
        
        // Listener para seleção na lista
        listViewPecas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                preencherCampos(newValue);
            }
        });
    }

    private void carregarLojas() {
        List<Loja> lojas = DatabaseManager.getAllLojas();
        ObservableList<Loja> observableList = FXCollections.observableArrayList(lojas);
        comboLojas.setItems(observableList);
    }

    private void carregarPecas() {
        List<Peca> pecas = DatabaseManager.getAllPecas();
        ObservableList<Peca> observableList = FXCollections.observableArrayList(pecas);
        listViewPecas.setItems(observableList);
    }

    private void preencherCampos(Peca peca) {
        txtNomePeca.setText(peca.getNome());
        txtDescricaoPeca.setText(peca.getDescricao());
        comboLojas.setValue(peca.getLoja());
        txtValorPeca.setText(String.valueOf(peca.getValor()));
        txtFotoPeca.setText(peca.getFotoPath());
        txtObservacoesPeca.setText(peca.getObservacoes());
        pecaEditando = peca;
    }

    @FXML
    private void salvarPeca() {
        String nome = txtNomePeca.getText().trim();
        String descricao = txtDescricaoPeca.getText().trim();
        Loja lojaSelecionada = comboLojas.getValue();
        String valorTexto = txtValorPeca.getText().trim();
        String fotoPath = txtFotoPeca.getText().trim();
        String observacoes = txtObservacoesPeca.getText().trim();

        if (nome.isEmpty()) {
            mostrarAlerta("Erro", "O nome da peça é obrigatório!");
            return;
        }

        if (lojaSelecionada == null) {
            mostrarAlerta("Erro", "Selecione uma loja!");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorTexto.replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Valor inválido! Use apenas números (ex: 299.90)");
            return;
        }

        if (pecaEditando == null) {
            // Adicionar nova peça
            Peca novaPeca = new Peca(nome, descricao, fotoPath, lojaSelecionada, valor, observacoes);
            DatabaseManager.addPeca(novaPeca);
            mostrarAlerta("Sucesso", "Peça adicionada com sucesso!");
        } else {
            // Editar peça existente
            pecaEditando.setNome(nome);
            pecaEditando.setDescricao(descricao);
            pecaEditando.setLoja(lojaSelecionada);
            pecaEditando.setValor(valor);
            pecaEditando.setFotoPath(fotoPath);
            pecaEditando.setObservacoes(observacoes);
            DatabaseManager.updatePeca(pecaEditando);
            mostrarAlerta("Sucesso", "Peça atualizada com sucesso!");
        }

        limparCampos();
        carregarPecas();
    }

    @FXML
    private void editarPeca() {
        Peca pecaSelecionada = listViewPecas.getSelectionModel().getSelectedItem();
        if (pecaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma peça para editar!");
            return;
        }
        preencherCampos(pecaSelecionada);
    }

    @FXML
    private void excluirPeca() {
        Peca pecaSelecionada = listViewPecas.getSelectionModel().getSelectedItem();
        if (pecaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma peça para excluir!");
            return;
        }

        DatabaseManager.deletePeca(pecaSelecionada.getId());
        mostrarAlerta("Sucesso", "Peça excluída com sucesso!");
        limparCampos();
        carregarPecas();
    }

    @FXML
    private void selecionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Foto da Peça");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Stage stage = (Stage) txtFotoPeca.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            txtFotoPeca.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void limparCampos() {
        txtNomePeca.clear();
        txtDescricaoPeca.clear();
        comboLojas.setValue(null);
        txtValorPeca.clear();
        txtFotoPeca.clear();
        txtObservacoesPeca.clear();
        pecaEditando = null;
        listViewPecas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

