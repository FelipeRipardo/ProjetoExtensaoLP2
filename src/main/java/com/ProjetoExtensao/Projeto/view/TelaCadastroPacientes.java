package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.DateTimeFormatter;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

@Component
@NoArgsConstructor
public class TelaCadastroPacientes extends JFrame {
    @Autowired
    private PacienteService pacienteService;

    private JTextField txtNome, txtCartaoSUS, txtCPF, txtDataNasc, txtNomeMae, txtDataEntrada;

    @PostConstruct
    public void initUI() {
        setTitle("Cadastro de Pacientes");
        setSize(900, 550);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel principal (cinza claro)
        JPanel panelMain = new JPanel(new GridBagLayout());
        panelMain.setBackground(Cores.COR_FUNDO_CLARO);

        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(8, 8, 8, 8);
        grid.fill = GridBagConstraints.HORIZONTAL;

        Font fonteLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fonteCampo = new Font("Segoe UI", Font.PLAIN, 13);

        // Nome completo
        grid.gridx = 0;
        grid.gridy = 0;
        panelMain.add(createLabel("Nome Completo:", fonteLabel), grid);
        grid.gridx = 1;
        txtNome = createTextField(fonteCampo);
        panelMain.add(txtNome, grid);

        // Cartão SUS
        grid.gridx = 2;
        panelMain.add(createLabel("Cartão SUS:", fonteLabel), grid);
        grid.gridx = 3;
        txtCartaoSUS = createTextField(fonteCampo);
        panelMain.add(txtCartaoSUS, grid);

        // CPF
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("CPF:", fonteLabel), grid);
        grid.gridx = 1;
        txtCPF = createTextField(fonteCampo);
        panelMain.add(txtCPF, grid);

        // Data de nascimento
        grid.gridx = 2;
        panelMain.add(createLabel("Data de Nascimento:", fonteLabel), grid);
        grid.gridx = 3;
        txtDataNasc = createTextField(fonteCampo);
        panelMain.add(txtDataNasc, grid);

        // Situação
//        grid.gridx = 2;
//        panelMain.add(createLabel("Situação:", fonteLabel), grid);
//        grid.gridx = 3;
//        String[] situacoes = {
//                "Situação de rua / sem moradia",
//                "Abandono familiar",
//                "Violência física / psicológica",
//                "Negligência / maus tratos",
//                "Óbito de responsáveis"
//        };
//        comboSituacao = new JComboBox<>(situacoes);
//        comboSituacao.setFont(fonteCampo);
//        panelMain.add(comboSituacao, grid);

        // Nome da mãe
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Nome da mãe:", fonteLabel), grid);
        grid.gridx = 1;
        txtNomeMae = createTextField(fonteCampo);
        panelMain.add(txtNomeMae, grid);
        grid.gridx = 2;

        // Data de entrada
        grid.gridx = 2;
        panelMain.add(createLabel("Date de Entrada:", fonteLabel), grid);
        grid.gridx = 3;
        txtDataEntrada = createTextField(fonteCampo);
        panelMain.add(txtDataEntrada, grid);

        // Botões
        grid.gridx = 1;
        grid.gridy++;
        JButton btnSalvar = createButton("Salvar");
        JButton btnLimpar = createButton("Limpar");
        JButton btnCancelar = createButton("Cancelar");

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Cores.COR_FUNDO_CLARO);
        btnPanel.add(btnSalvar);
        btnPanel.add(btnLimpar);
        btnPanel.add(btnCancelar);
        grid.gridwidth = 3;
        panelMain.add(btnPanel, grid);

        add(panelMain, BorderLayout.CENTER);

        // Eventos dos botões
        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limparCampos());
        btnCancelar.addActionListener(e -> cancelar());
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField txt = new JTextField(15);
        txt.setFont(font);
        return txt;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Cores.COR_RODAPE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }


    private void salvar() {
        Paciente paciente = new Paciente();

        paciente.setNomeCompleto(txtNome.getText());
        paciente.setCpf(txtCPF.getText());

        System.out.println(txtDataNasc.getText());
        paciente.setDataNascimento(LocalDate.parse(txtDataNasc.getText(), DateTimeFormatter.DATE_TIME_FORMATTER));
        paciente.setCartaoSUS(txtCartaoSUS.getText());
        paciente.setNomeMae(txtNomeMae.getText());
        paciente.setDataEntrada(LocalDate.parse(txtDataEntrada.getText(), DateTimeFormatter.DATE_TIME_FORMATTER));

        pacienteService.salvarPaciente(paciente);

        JOptionPane.showMessageDialog(this, "Paciente salvo com sucesso!");
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCartaoSUS.setText("");
        txtCPF.setText("");
        txtDataNasc.setText("");
        txtNomeMae.setText("");
        txtDataEntrada.setText("");
        //comboSituacao.setSelectedIndex(0);
    }

    private void cancelar() {
        dispose();
    }
}
