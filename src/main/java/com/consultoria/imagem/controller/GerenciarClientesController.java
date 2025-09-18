package com.consultoria.imagem.controller;

import com.consultoria.imagem.model.Cliente;
import com.consultoria.imagem.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarClientesController implements Initializable {

    @FXML
    private ListView<Cliente> listViewClientes;

    @FXML
    private TextField txtNomeCliente;

    @FXML
    private TextField txtContatoCliente;

    @FXML
    private TextArea txtObservacoesCliente;

    private Cliente clienteEditando = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarClientes();
        
        // Listener para seleção na lista
        listViewClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                preencherCampos(newValue);
            }
        });
    }

    private void carregarClientes() {
        List<Cliente> clientes = DatabaseManager.getAllClientes();
        ObservableList<Cliente> observableList = FXCollections.observableArrayList(clientes);
        listViewClientes.setItems(observableList);
    }

    private void preencherCampos(Cliente cliente) {
        txtNomeCliente.setText(cliente.getNome());
        txtContatoCliente.setText(cliente.getContato());
        txtObservacoesCliente.setText(cliente.getObservacoes());
        clienteEditando = cliente;
    }

    @FXML
    private void salvarCliente() {
        String nome = txtNomeCliente.getText().trim();
        String contato = txtContatoCliente.getText().trim();
        String observacoes = txtObservacoesCliente.getText().trim();

        if (nome.isEmpty()) {
            mostrarAlerta("Erro", "O nome do cliente é obrigatório!");
            return;
        }

        if (clienteEditando == null) {
            // Adicionar novo cliente
            Cliente novoCliente = new Cliente(nome, contato, observacoes);
            DatabaseManager.addCliente(novoCliente);
            mostrarAlerta("Sucesso", "Cliente adicionado com sucesso!");
        } else {
            // Editar cliente existente
            clienteEditando.setNome(nome);
            clienteEditando.setContato(contato);
            clienteEditando.setObservacoes(observacoes);
            DatabaseManager.updateCliente(clienteEditando);
            mostrarAlerta("Sucesso", "Cliente atualizado com sucesso!");
        }

        limparCampos();
        carregarClientes();
    }

    @FXML
    private void editarCliente() {
        Cliente clienteSelecionado = listViewClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            mostrarAlerta("Aviso", "Selecione um cliente para editar!");
            return;
        }
        preencherCampos(clienteSelecionado);
    }

    @FXML
    private void excluirCliente() {
        Cliente clienteSelecionado = listViewClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            mostrarAlerta("Aviso", "Selecione um cliente para excluir!");
            return;
        }

        DatabaseManager.deleteCliente(clienteSelecionado.getId());
        mostrarAlerta("Sucesso", "Cliente excluído com sucesso!");
        limparCampos();
        carregarClientes();
    }

    @FXML
    private void criarListaParaCliente() {
        Cliente clienteSelecionado = listViewClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            mostrarAlerta("Aviso", "Selecione um cliente para criar uma lista!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultoria/imagem/montar-lista.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Passar o cliente selecionado para o controlador da nova tela
            MontarListaController controller = loader.getController();
            controller.setCliente(clienteSelecionado);
            
            Stage stage = new Stage();
            stage.setTitle("Montar Lista para " + clienteSelecionado.getNome());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir a tela de montagem de lista!");
        }
    }

    @FXML
    private void limparCampos() {
        txtNomeCliente.clear();
        txtContatoCliente.clear();
        txtObservacoesCliente.clear();
        clienteEditando = null;
        listViewClientes.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

