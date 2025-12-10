package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaRelatorios extends JPanel {

    private final PacienteRepositorio pacienteRepositorio;
    private final ConsultaRepositorio consultaRepositorio;

    public TelaRelatorios(PacienteRepositorio pacienteRepo, ConsultaRepositorio consultaRepo) {
        this.pacienteRepositorio = pacienteRepo;
        this.consultaRepositorio = consultaRepo;

        setLayout(null);
        setBackground(new Color(245, 245, 245));

        // Título
        JLabel titulo = new JLabel("Dashboard e Indicadores");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(50, 50, 50));
        titulo.setBounds(40, 30, 400, 30);
        add(titulo);

        // --- CARD 1: Total de Idosas ---
        // Se der erro no count(), verifique se o repositorio estende JpaRepository
        String totalPacientes = String.valueOf(pacienteRepo.count());
        JPanel cardPacientes = criarCard("Total de Idosas", totalPacientes, new Color(100, 149, 237), 40, 80);
        add(cardPacientes);

        // --- CARD 2: Total de Consultas ---
        String totalConsultas = String.valueOf(consultaRepo.count());
        JPanel cardConsultas = criarCard("Consultas Realizadas", totalConsultas, new Color(60, 179, 113), 260, 80);
        add(cardConsultas);

        // --- BOTÃO EXPORTAR ---
        JButton btnExportar = new JButton("Baixar Relatório Geral (.txt)");
        btnExportar.setBounds(40, 250, 250, 45);
        btnExportar.setBackground(new Color(70, 130, 180));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFont(new Font("Arial", Font.BOLD, 14));
        btnExportar.setFocusPainted(false);

        btnExportar.addActionListener(e -> gerarRelatorioTxt());
        add(btnExportar);

        JLabel infoExport = new JLabel("Gera um arquivo na pasta do projeto com o resumo das residentes.");
        infoExport.setFont(new Font("Arial", Font.ITALIC, 12));
        infoExport.setBounds(40, 300, 450, 20);
        add(infoExport);
    }

    private JPanel criarCard(String titulo, String valor, Color cor, int x, int y) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(x, y, 200, 120);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, cor));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 40));
        lblValor.setForeground(cor);
        lblValor.setBounds(20, 30, 160, 50);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblValor);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setForeground(Color.GRAY);
        lblTitulo.setBounds(10, 80, 180, 20);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblTitulo);

        return card;
    }

    private void gerarRelatorioTxt() {
        List<Paciente> pacientes = pacienteRepositorio.findAll();
        String nomeArquivo = "Relatorio_Geral_" + System.currentTimeMillis() + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("=== RELATÓRIO GERAL - RECANTO DO SAGRADO CORAÇÃO ===");
            writer.println("Data de Geração: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            writer.println("====================================================\n");

            writer.println("INDICADORES GERAIS:");
            writer.println("- Total de Idosas Residentes: " + pacientes.size());
            writer.println("- Total de Consultas Registradas: " + consultaRepositorio.count());
            writer.println("\n----------------------------------------------------\n");

            writer.println("LISTA DE RESIDENTES:\n");

            for (Paciente p : pacientes) {
                writer.println("ID: " + p.getId());
                writer.println("Nome: " + p.getNomeCompleto());
                writer.println("CPF: " + p.getCpf());
                writer.println("Data Nascimento: " + p.getDataNascimento());
                writer.println("----------------------------------------------------");
            }

            JOptionPane.showMessageDialog(this, "Sucesso! O arquivo foi gerado na pasta do projeto:\n" + nomeArquivo);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}