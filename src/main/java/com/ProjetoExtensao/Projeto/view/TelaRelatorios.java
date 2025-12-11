package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class TelaRelatorios extends JPanel {

    private final PacienteRepositorio pacienteRepositorio;
    private final ConsultaRepositorio consultaRepositorio;

    //Componentes que precisam ser acessados globalmente na classe
    private JLabel lblNomeResultado;
    private JLabel lblNascResultado;
    private JLabel lblCartaoSus;
    private JButton btnGerarProntuario;
    private JPanel painelResultado;
    private Paciente pacienteEncontrado; //Guarda o paciente da busca atual

    public TelaRelatorios(PacienteRepositorio pacienteRepo, ConsultaRepositorio consultaRepo) {
        this.pacienteRepositorio = pacienteRepo;
        this.consultaRepositorio = consultaRepo;

        setLayout(null);
        setBackground(new Color(245, 245, 245));

        // --- TÍTULO ---
        JLabel titulo = new JLabel("Dashboard e Prontuários");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(50, 50, 50));
        titulo.setBounds(40, 20, 400, 30);
        add(titulo);

        //Primeiro passo: DASHBOARD
        String totalPacientes = String.valueOf(pacienteRepo.count());
        JPanel cardPacientes = criarCard("Total de Idosas", totalPacientes, new Color(100, 149, 237), 40, 70);
        add(cardPacientes);

        String totalConsultas = String.valueOf(consultaRepo.count());
        JPanel cardConsultas = criarCard("Consultas Realizadas", totalConsultas, new Color(60, 179, 113), 260, 70);
        add(cardConsultas);

        //Segundo passo: ÁREA DE BUSCA
        JSeparator separator = new JSeparator();
        separator.setBounds(40, 210, 700, 10);
        add(separator);

        JLabel lblBusca = new JLabel("Consultar Prontuário Individual");
        lblBusca.setFont(new Font("Arial", Font.BOLD, 18));
        lblBusca.setForeground(new Color(50, 50, 50));
        lblBusca.setBounds(40, 230, 400, 30);
        add(lblBusca);

        JLabel lblCpf = new JLabel("Digite o CPF:");
        lblCpf.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCpf.setBounds(40, 270, 100, 30);
        add(lblCpf);

        //CAMPO FORMATADO (MÁSCARA)
        JFormattedTextField txtCpf = criarCampoCpf();
        txtCpf.setBounds(140, 270, 150, 30);
        add(txtCpf);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(300, 270, 100, 30);
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        add(btnBuscar);

        //Terceiro passo: PAINEL DE RESULTADOS (Invisível no início)
        painelResultado = new JPanel();
        painelResultado.setLayout(null);
        painelResultado.setBounds(40, 320, 700, 200);
        painelResultado.setBackground(Color.WHITE);
        painelResultado.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        painelResultado.setVisible(false); // Começa escondido

        JLabel lblTituloRes = new JLabel("Dados da Residente:");
        lblTituloRes.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloRes.setBounds(20, 10, 200, 20);
        painelResultado.add(lblTituloRes);

        lblNomeResultado = new JLabel("Nome: -");
        lblNomeResultado.setBounds(20, 40, 400, 20);
        painelResultado.add(lblNomeResultado);

        lblNascResultado = new JLabel("Nascimento: -");
        lblNascResultado.setBounds(20, 70, 300, 20);
        painelResultado.add(lblNascResultado);

        lblCartaoSus = new JLabel("Cartão SUS: -");
        lblCartaoSus.setBounds(300, 70, 300, 20);
        painelResultado.add(lblCartaoSus);

        btnGerarProntuario = new JButton("Baixar Histórico Completo (.txt)");
        btnGerarProntuario.setBounds(20, 120, 250, 40);
        btnGerarProntuario.setBackground(new Color(34, 139, 34)); // Verde Floresta
        btnGerarProntuario.setForeground(Color.WHITE);
        btnGerarProntuario.setFont(new Font("Arial", Font.BOLD, 13));
        btnGerarProntuario.setFocusPainted(false);
        painelResultado.add(btnGerarProntuario);

        add(painelResultado);

        // --- AÇÕES DOS BOTÕES ---

        btnBuscar.addActionListener(e -> {
            String cpfDigitado = txtCpf.getText();
            buscarPaciente(cpfDigitado);
        });

        btnGerarProntuario.addActionListener(e -> {
            if (pacienteEncontrado != null) {
                gerarRelatorioIndividual(pacienteEncontrado);
            }
        });
    }

    private JFormattedTextField criarCampoCpf() {
        try {
            MaskFormatter mask = new MaskFormatter("###.###.###-##");
            mask.setPlaceholderCharacter('_'); // Mostra ___.___.___-__
            JFormattedTextField field = new JFormattedTextField(mask);
            field.setFont(new Font("Arial", Font.PLAIN, 14));
            return field;
        } catch (ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private void buscarPaciente(String cpf) {
        //Busca no banco usando o novo método findByCpf
        Optional<Paciente> resultado = pacienteRepositorio.findByCpf(cpf);

        if (resultado.isPresent()) {
            pacienteEncontrado = resultado.get();

            //Atualiza a tela
            lblNomeResultado.setText("Nome: " + pacienteEncontrado.getNomeCompleto());
            lblNascResultado.setText("Nascimento: " + pacienteEncontrado.getDataNascimento());
            lblCartaoSus.setText("Cartão SUS: " + pacienteEncontrado.getCartaoSUS());

            painelResultado.setVisible(true); // Mostra o painel
            repaint();
        } else {
            painelResultado.setVisible(false);
            pacienteEncontrado = null;
            JOptionPane.showMessageDialog(this,
                    "CPF não encontrado na base de dados.\nTente novamente com um CPF já cadastrado.",
                    "Não encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void gerarRelatorioIndividual(Paciente p) {
        String nomeArquivo = "Prontuario_" + p.getNomeCompleto().replace(" ", "_") + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("=== PRONTUÁRIO INDIVIDUAL - RECANTO DO SAGRADO CORAÇÃO ===");
            writer.println("Data de Emissão: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            writer.println("========================================================\n");

            writer.println("1. DADOS PESSOAIS");
            writer.println("Nome: " + p.getNomeCompleto());
            writer.println("CPF: " + p.getCpf());
            writer.println("Data de Nascimento: " + p.getDataNascimento());
            writer.println("Nome da Mãe: " + p.getNomeMae());
            writer.println("Cartão SUS: " + p.getCartaoSUS());
            writer.println("Data de Entrada: " + p.getDataEntrada());
            writer.println("\n--------------------------------------------------------\n");

            writer.println("2. HISTÓRICO DE CONSULTAS E EVOLUÇÃO");

            //Busca consultas vinculadas a este paciente (Se a lista estiver carregada)
            if (p.getConsultas() != null && !p.getConsultas().isEmpty()) {
                for (Consulta c : p.getConsultas()) {
                    writer.println("Data: " + c.getData() + " às " + c.getHora());
                    writer.println("Tipo: " + c.getTipoConsulta());
                    writer.println("Médico/Responsável: " + c.getResponsavelSaude().getNomeCompleto());
                    writer.println("--------------------");
                }
            } else {
                writer.println("Nenhum registro de consulta encontrado para este período.");
            }

            writer.println("\n\nAssinatura do Responsável:\n__________________________________");

            JOptionPane.showMessageDialog(this, "Prontuário gerado com sucesso!\nArquivo: " + nomeArquivo);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Método auxiliar visual (mesmo de antes)
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
}