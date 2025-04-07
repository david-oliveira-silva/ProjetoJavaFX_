package com.example.projetojavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FuncionarioController {
    @FXML
    private TextField nomeTextField;
    @FXML
    private TextField matriculaTextField;
    @FXML
    private TextField cpfTextField;
    @FXML
    private TextField cargoTextField;
    @FXML
    private TextField salarioTextField;
    @FXML
    private TextField dataNascimentoTextField;
    @FXML
    private TextField logradouroTextField;
    @FXML
    private TextField numeroTextField;
    @FXML
    private TextField complementoTextField;
    @FXML
    private TextField bairroTextField;
    @FXML
    private TextField cidadeTextField;
    @FXML
    private TextField estadoTextField;
    @FXML
    private TextField cepTextField;
    @FXML
    private TextField FiltroCargoTextField;
    @FXML
    private TextField FiltroSalarioMinimoTextField;
    @FXML
    private TextField FiltroSalarioMaximoTextField;
    @FXML
    private TextField FiltroCidadeTextField;
    @FXML TextField MediaSalarioTextField;


    private List<Funcionarios> funcionarios;
    private List<Endereco> endereco;
    private ObservableList<Funcionarios> observableFuncionarios = FXCollections.observableArrayList(); // Criar ObservableList

    @FXML
    private TableView<Funcionarios> funcionariosTableView;
    @FXML
    private TableColumn<Funcionarios, String> nomeColumn;
    @FXML
    private TableColumn<Funcionarios, String> matriculaColumn;
    @FXML
    private TableColumn<Funcionarios, String> cpfColumn;
    @FXML
    private TableColumn<Funcionarios, String> cargoColumn;
    @FXML
    private TableColumn<Funcionarios, BigDecimal> salarioColumn;
    @FXML
    private TableColumn<Funcionarios, LocalDate> dataNascimentoColumn;
    @FXML
    private TableColumn<Funcionarios, String> enderecoColumn;



    @FXML
    private void initialize() {
        try {
            funcionarios = ArquivoCSV.carregarFuncionarios();

            nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
            matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
            cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
            cargoColumn.setCellValueFactory(new PropertyValueFactory<>("cargo"));
            salarioColumn.setCellValueFactory(new PropertyValueFactory<>("salario"));
            dataNascimentoColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
            enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarFuncionario() {
        String nome = nomeTextField.getText().trim();
        String matricula = matriculaTextField.getText().trim();
        String cpf = cpfTextField.getText().trim();
        String cargo = cargoTextField.getText().trim().toUpperCase();
        float salario = Float.parseFloat(salarioTextField.getText().trim());
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoTextField.getText().trim());
        String logradouro = logradouroTextField.getText().trim().toUpperCase();
        String numero = numeroTextField.getText().trim().toUpperCase();
        String complemento = complementoTextField.getText().trim().toUpperCase();
        String bairro = bairroTextField.getText().trim().toUpperCase();
        String cidade = cidadeTextField.getText().trim().toUpperCase();
        String estado = estadoTextField.getText().trim().toUpperCase();
        String cep = cepTextField.getText().trim().toUpperCase();

        Endereco endereco1 = new Endereco(logradouro, numero, complemento, bairro, cidade, estado, cep);
        Funcionarios funcionarios1 = new Funcionarios(matricula, nome, cpf, dataNascimento, cargo, salario, endereco1);
        funcionarios.add(funcionarios1);
        funcionariosTableView.setItems(observableFuncionarios);
        observableFuncionarios.add(funcionarios1);

        // Salvar a lista atualizada de funcionários
        try {
            ArquivoCSV.salvarFuncionarios(funcionarios);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Exibir mensagem de sucesso
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText("Funcionário salvo com sucesso!");
        alert.showAndWait();
    }

    public void carregarValores() throws IOException {

        // Carregar os funcionários e endereços diretamente dentro da classe
        funcionarios = ArquivoCSV.carregarFuncionarios();


        // Populando a ObservableList com os funcionários
        observableFuncionarios.clear();  // Limpa antes de adicionar
        observableFuncionarios.addAll(funcionarios);

        // Atualiza a TableView com a lista de funcionários
        funcionariosTableView.setItems(observableFuncionarios);
    }
    @FXML
    private void excluirFuncionarios() {
        try {
            // Chama o método de excluir todos os funcionários no arquivo CSV
            ArquivoCSV.excluirFuncionarios();

            // Atualiza a TableView, removendo todos os itens
            observableFuncionarios.clear();
            funcionariosTableView.setItems(observableFuncionarios);

            // Exibe uma mensagem de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Todos os funcionários foram excluídos com sucesso!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Exibe uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao excluir os funcionários!");
            alert.showAndWait();
        }
    }



    public void filtrarCargo() {
        // Obtém o cargo a ser filtrado a partir do TextField
        String cargo = FiltroCargoTextField.getText().trim();

        // Filtra a lista de funcionários com base no cargo
        List<Funcionarios> filtrados = observableFuncionarios.stream()
                .filter(c -> c.getCargo().trim().equalsIgnoreCase(cargo)) // Usa equalsIgnoreCase para tornar a comparação insensível a maiúsculas/minúsculas
                .collect(Collectors.toList());

        // Atualiza a TableView com a lista filtrada
        ObservableList<Funcionarios> funcionariosFiltrados = FXCollections.observableArrayList(filtrados);
        funcionariosTableView.setItems(funcionariosFiltrados);
    }


    public void filtrarSalario() {

        if (observableFuncionarios == null || observableFuncionarios.isEmpty()) {
            System.out.println("Não há funcionários na lista para calcular a média.");
            return;
        }
        float salarioMinimo = Float.parseFloat(FiltroSalarioMinimoTextField.getText().trim());// Valor mínimo
        float salarioMaximo = Float.parseFloat(FiltroSalarioMaximoTextField.getText().trim()); // Valor máximo

        // Filtra a lista de funcionários com base no cargo
        List<Funcionarios> filtrados = observableFuncionarios.stream()
                .filter(c -> c.getSalario() >= salarioMinimo && c.getSalario() <= salarioMaximo)
                .collect(Collectors.toList());

        // Atualiza a TableView com a lista filtrada
        ObservableList<Funcionarios> funcionariosFiltrados = FXCollections.observableArrayList(filtrados);
        funcionariosTableView.setItems(funcionariosFiltrados);
    }

    public void mediaSalarial() {

        String cargo = FiltroCargoTextField.getText().trim();

        double mediaSalarial =  observableFuncionarios.stream()
                .filter(c -> c.getCargo().equalsIgnoreCase(cargo)).mapToDouble(Funcionarios::getSalario)
                .average().orElse(0.0);
        // Exibe o resultado em um alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Média Salarial");
        alert.setHeaderText("A média salarial dos funcionários é:");
        alert.setContentText(String.format("R$ %.2f", mediaSalarial));
        alert.showAndWait();
    }


    public void filtrarCidade(){
        // Obtém a cidade a ser filtrada a partir do TextField
        String cidade = FiltroCidadeTextField.getText().trim();

        // Filtra a lista de funcionários com base na cidade
        List<Funcionarios> filtrados = observableFuncionarios.stream()
                .filter(c -> c.getEndereco() != null && c.getEndereco().getCidade().equalsIgnoreCase(cidade)) // Verifica se o endereço e a cidade não são nulos
                .collect(Collectors.toList());

        // Atualiza a TableView com a lista filtrada
        ObservableList<Funcionarios> funcionariosFiltrados = FXCollections.observableArrayList(filtrados);
        funcionariosTableView.setItems(funcionariosFiltrados);
    }
}
