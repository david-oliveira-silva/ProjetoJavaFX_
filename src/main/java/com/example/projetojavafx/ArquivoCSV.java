package com.example.projetojavafx;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArquivoCSV {
    private static final String FILE_PATH = "C:\\Users\\Darve\\IdeaProjects\\ProjetoJavaFx\\src\\main\\java\\com\\example\\projetojavafx\\funcionarios.csv";

    public static void salvarFuncionarios(List<Funcionarios> funcionarios) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Funcionarios funcionario : funcionarios) {
                writer.write(formatFuncionario(funcionario));
                writer.newLine();
            }
        }
    }

    public static List<Funcionarios> carregarFuncionarios() throws IOException {
        List<Funcionarios> funcionarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                funcionarios.add(parseFuncionario(line));
            }
        }
        return funcionarios;
    }
    public static void excluirFuncionarios() throws IOException {
        // Cria uma lista vazia de funcionários
        List<Funcionarios> funcionariosVazios = new ArrayList<>();

        // Salva a lista vazia no arquivo CSV, apagando todos os dados
        salvarFuncionarios(funcionariosVazios);
    }



    private static String formatFuncionario(Funcionarios funcionario) {
        return String.join(";",
                funcionario.getMatricula(),
                funcionario.getNome(),
                funcionario.getCpf(),
                funcionario.getDataNascimento().toString(),
                funcionario.getCargo().toString(),
                funcionario.getSalario().toString(),
                funcionario.getEndereco().toString());
    }

    private static Funcionarios parseFuncionario(String line) {
        String[] data = line.split(";");
        if (data.length < 7) {
            System.out.println("Linha inválida (menos de 7 colunas): " + line);
            return null;  // Linha inválida, retorna null
        }
        return new Funcionarios(
                data[0],
                data[1],
                data[2],
                LocalDate.parse(data[3]),
                data[4],
                Float.parseFloat(data[5]),
                new Endereco(data[6], "", "", "", "", "", ""));

    }

}


