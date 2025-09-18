package com.consultoria.imagem.controller;

import com.consultoria.imagem.model.Cliente;
import com.consultoria.imagem.model.ListaDeCompras;
import com.consultoria.imagem.model.Peca;
import com.consultoria.imagem.util.DatabaseManager;
import com.consultoria.imagem.util.PDFGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MontarListaController implements Initializable {

    @FXML
    private Label lblTitulo;

    @FXML
    private TextField txtBuscarPeca;

    @FXML
    private ListView<Peca> listViewCatalogo;

    @FXML
    private ListView<Peca> listViewLista;

    @FXML
    private Label lblTotalValor;

    @FXML
    private Button btnSalvarLista;

    private Cliente cliente;
    private ListaDeCompras listaAtual;
    private List<Peca> catalogoCompleto;
    private ObservableList<Peca> pecasNaLista;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        catalogoCompleto = DatabaseManager.getAllPecas();
        pecasNaLista = FXCollections.observableArrayList();

        carregarCatalogo();
        listViewLista.setItems(pecasNaLista);

        // Listener para busca
        txtBuscarPeca.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarCatalogo(newValue);
        });

        // Listener para atualizar total quando lista muda
        pecasNaLista.addListener((javafx.collections.ListChangeListener<Peca>) change -> {
            atualizarTotal();
        });
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        lblTitulo.setText("Montando Lista para: " + cliente.getNome());
        // Se não for uma lista existente, inicializa uma nova
        if (listaAtual == null) {
            listaAtual = new ListaDeCompras("Nova Lista", cliente, LocalDate.now());
        }
    }

    public void setListaDeCompras(ListaDeCompras lista) {
        this.listaAtual = lista;
        if (listaAtual != null) {
            lblTitulo.setText("Editando Lista: " + listaAtual.getNome() + " para " + listaAtual.getCliente().getNome());
            pecasNaLista.addAll(listaAtual.getPecas());
            btnSalvarLista.setText("Salvar Alterações");
        } else {
            // Se for uma nova lista, setCliente já inicializa
            btnSalvarLista.setText("Salvar Nova Lista");
        }
    }

    private void carregarCatalogo() {
        ObservableList<Peca> observableList = FXCollections.observableArrayList(catalogoCompleto);
        listViewCatalogo.setItems(observableList);
    }

    private void filtrarCatalogo(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            carregarCatalogo();
            return;
        }

        List<Peca> pecasFiltradas = catalogoCompleto.stream()
                .filter(peca -> peca.getNome().toLowerCase().contains(filtro.toLowerCase()) ||
                        peca.getDescricao().toLowerCase().contains(filtro.toLowerCase()) ||
                        peca.getLoja().getNome().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList<Peca> observableList = FXCollections.observableArrayList(pecasFiltradas);
        listViewCatalogo.setItems(observableList);
    }

    @FXML
    private void adicionarPecaALista() {
        Peca pecaSelecionada = listViewCatalogo.getSelectionModel().getSelectedItem();
        if (pecaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma peça do catálogo para adicionar!");
            return;
        }

        if (pecasNaLista.contains(pecaSelecionada)) {
            mostrarAlerta("Aviso", "Esta peça já está na lista!");
            return;
        }

        pecasNaLista.add(pecaSelecionada);
        listaAtual.addPeca(pecaSelecionada);
    }

    @FXML
    private void removerPecaDaLista() {
        Peca pecaSelecionada = listViewLista.getSelectionModel().getSelectedItem();
        if (pecaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma peça da lista para remover!");
            return;
        }

        pecasNaLista.remove(pecaSelecionada);
        listaAtual.removePeca(pecaSelecionada);
    }

    @FXML
    private void salvarLista() {
        if (pecasNaLista.isEmpty()) {
            mostrarAlerta("Aviso", "Adicione pelo menos uma peça à lista antes de salvar!");
            return;
        }

        if (listaAtual.getId() == 0) { // Nova lista
            TextInputDialog dialog = new TextInputDialog("Nova Lista");
            dialog.setTitle("Nome da Lista");
            dialog.setHeaderText("Dê um nome para esta lista de compras:");
            dialog.setContentText("Nome:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                listaAtual.setNome(result.get().trim());
                listaAtual.setPecas(new ArrayList<>(pecasNaLista)); // Garante que a lista de peças está atualizada
                DatabaseManager.addListaDeCompras(listaAtual);
                mostrarAlerta("Sucesso", "Lista salva com sucesso!");
                ((Stage) lblTitulo.getScene().getWindow()).close(); // Fecha a tela após salvar
            } else {
                mostrarAlerta("Aviso", "O nome da lista não pode ser vazio!");
            }
        } else { // Editando lista existente
            listaAtual.setPecas(new ArrayList<>(pecasNaLista)); // Garante que a lista de peças está atualizada
            DatabaseManager.updateListaDeCompras(listaAtual);
            mostrarAlerta("Sucesso", "Lista atualizada com sucesso!");
            ((Stage) lblTitulo.getScene().getWindow()).close(); // Fecha a tela após salvar
        }
    }

    @FXML
    private void gerarPDF() {
        if (pecasNaLista.isEmpty()) {
            mostrarAlerta("Aviso", "Adicione pelo menos uma peça à lista antes de gerar o PDF!");
            return;
        }

        // Garante que a listaAtual tem as peças mais recentes antes de gerar o PDF
        listaAtual.setPecas(new ArrayList<>(pecasNaLista));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar PDF");
        fileChooser.setInitialFileName("Lista_" + listaAtual.getNome().replace(" ", "_") + "_" + cliente.getNome().replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PDFGenerator.gerarPDF(listaAtual, file.getAbsolutePath());
                mostrarAlerta("Sucesso", "PDF gerado com sucesso em: " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Erro ao gerar PDF: " + e.getMessage());
            }
        }
    }

    private void atualizarTotal() {
        double total = pecasNaLista.stream()
                .mapToDouble(Peca::getValor)
                .sum();
        lblTotalValor.setText(String.format("Total: R$ %.2f", total));
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
