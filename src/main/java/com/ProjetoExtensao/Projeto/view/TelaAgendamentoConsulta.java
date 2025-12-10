package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.IconManager;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.models.TipoConsulta;
import com.ProjetoExtensao.Projeto.servicos.ConsultaService;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.ResponsavelService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaAgendamentoConsulta extends JFrame {
    @Autowired
    private PanelsFactory panelsFactory;
    @Autowired
    private IconManager iconManager;
    @Autowired
    private ResponsavelService responsavelService;
    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private NavigationService navigationService;

    private JComboBox<String> especialidadeComboBox;
    private JComboBox<String> medicosComboBox;

    private JTextField cpfField;
    private JTextField dataField;
    private JTextField horarioField;

    @PostConstruct
    public void initUI() {
        setTitle("Recanto do Sagrado Coração - Agendamento de Consulta");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Usando PanelsFactory para cabeçalho e rodapé
        JPanel headerPanel = panelsFactory.getHeaderPanel();
        JPanel footerPanel = panelsFactory.getFooterPanel();

        // Painel principal (formulário)
        JPanel mainPanel = criarPainelPrincipal();
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Adiciona tudo ao frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel criarPainelPrincipal() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Cores.COR_FUNDO_CLARO);

        Color azulEscuro = Cores.COR_RODAPE;
        Color cinzaTitulo = Cores.COR_LETRA_PAINEL;

        // --- CABEÇALHO DE SEÇÃO --- //
        JPanel sectionHeader = new JPanel(new BorderLayout(10, 0));
        sectionHeader.setBackground(Cores.COR_FUNDO_CLARO);
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        sectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.COR_LETRA_PAINEL));
        mainPanel.add(sectionHeader);

        JLabel sectionTitle = new JLabel("Agendamento de Consulta");
        sectionTitle.setFont(new Font("Arial", Font.PLAIN, 36));
        sectionTitle.setForeground(cinzaTitulo);
        sectionTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        JPanel sectionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sectionButtonsPanel.setOpaque(false);

        JButton voltarConsultaBtn = new JButton("\u2190 Voltar para a Consulta");
        voltarConsultaBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        voltarConsultaBtn.setForeground(cinzaTitulo);
        voltarConsultaBtn.setContentAreaFilled(false);
        voltarConsultaBtn.setBorderPainted(false);
        voltarConsultaBtn.setFocusPainted(false);
        sectionButtonsPanel.add(voltarConsultaBtn);

        sectionHeader.add(sectionButtonsPanel, BorderLayout.EAST);

        // --- AÇÃO DO BOTÃO "Voltar para a Consulta" ---
        voltarConsultaBtn.addActionListener(e -> {
            navigationService.abrirTelaConsultas();
            dispose();
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- FORMULÁRIO --- //
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(true);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- NOME DO PACIENTE ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel nomeLabel = new JLabel("Cpf do Paciente");
        nomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nomeLabel.setForeground(azulEscuro);
        formPanel.add(nomeLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        cpfField = new JTextField();
        cpfField.setFont(new Font("Arial", Font.PLAIN, 18));
        cpfField.setForeground(azulEscuro);
        cpfField.setToolTipText("Digite o cpf do paciente");
        addPlaceholder(cpfField, "Digite o cpf do paciente");
        formPanel.add(cpfField, gbc);

        // Reset gridwidth for subsequent rows, keep weightx
        gbc.gridwidth = 1;

        // --- SELECIONAR MÉDICO ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel medicoLabel = new JLabel("Selecionar Médico");
        medicoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        medicoLabel.setForeground(azulEscuro);
        formPanel.add(medicoLabel, gbc);

        // --- SELECIONAR ESPECIALIDADE ---
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        JLabel especialidadeLabel = new JLabel("Selecionar o tipo de Consulta");
        especialidadeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        especialidadeLabel.setForeground(azulEscuro);
        formPanel.add(especialidadeLabel, gbc);

        medicosComboBox = new JComboBox<>();
        medicosComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        medicosComboBox.setForeground(cinzaTitulo);
        popularMedicos();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(medicosComboBox, gbc);

        especialidadeComboBox = new JComboBox<>();
        especialidadeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        especialidadeComboBox.setForeground(cinzaTitulo);
        popularEspecialidades();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 20, 0);
        formPanel.add(especialidadeComboBox, gbc);

        // --- DATA ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel dataLabel = new JLabel("Data");
        dataLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dataLabel.setForeground(azulEscuro);
        formPanel.add(dataLabel, gbc);

        // --- HORÁRIO ---
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        JLabel horarioLabel = new JLabel("Horário");
        horarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        horarioLabel.setForeground(azulEscuro);
        formPanel.add(horarioLabel, gbc);

        // Nested panel for data field and calendar icon
        JPanel dataFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dataFieldPanel.setOpaque(false);
        dataField = new JTextField();
        dataField.setFont(new Font("Arial", Font.PLAIN, 18));
        dataField.setToolTipText("dd/mm/aaaa");
        dataField.setForeground(azulEscuro);
        addPlaceholder(dataField, "dd/MM/yyyy");
        dataField.setPreferredSize(new Dimension(170, 35));
        dataFieldPanel.add(dataField);

        // Re-enable calendar icon
        ImageIcon calendarIcon = iconManager.createScaledIcon("/assets/calendar.png", 20, 20);
        if (calendarIcon != null) {
            JLabel calendarIconLabel = new JLabel(calendarIcon);
            dataFieldPanel.add(calendarIconLabel);
        }

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(dataFieldPanel, gbc);

        horarioField = new JTextField();
        horarioField.setFont(new Font("Arial", Font.PLAIN, 18));
        horarioField.setForeground(azulEscuro);
        addPlaceholder(horarioField, "HH:mm");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 20, 0);
        formPanel.add(horarioField, gbc);

        // --- BOTÃO ATUALIZAR ---
        JButton refreshButton = panelsFactory.getRefreshButton();
        JButton adminButton = panelsFactory.getAdminButton();

        adminButton.addActionListener(e -> {
            navigationService.abrirTelaGeral();
            dispose();
        });
        if (refreshButton != null) {
            refreshButton.addActionListener(e -> {
                if (cpfField != null) {
                    cpfField.setText("");
                    for (FocusListener fl : cpfField.getFocusListeners()) cpfField.removeFocusListener(fl);
                    addPlaceholder(cpfField, "Digite o cpf do paciente");
                }
                if (dataField != null) {
                    dataField.setText("");
                    for (FocusListener fl : dataField.getFocusListeners()) dataField.removeFocusListener(fl);
                    addPlaceholder(dataField, "dd/MM/yyyy");
                }
                if (horarioField != null) {
                    horarioField.setText("");
                    for (FocusListener fl : horarioField.getFocusListeners()) horarioField.removeFocusListener(fl);
                    addPlaceholder(horarioField, "HH:mm");
                }
                if (medicosComboBox != null) medicosComboBox.setSelectedIndex(0);
                if (especialidadeComboBox != null) especialidadeComboBox.setSelectedIndex(0);

            });
        }

        // Adiciona o botão Agendar ao formPanel com GridBagLayout
        JButton agendarBtn = new JButton("Agendar");
        agendarBtn.setFont(new Font("Arial", Font.BOLD, 20));
        agendarBtn.setBackground(Cores.COR_RODAPE);
        agendarBtn.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(agendarBtn, gbc);

        agendarBtn.addActionListener(e -> salvarConsulta());
        return mainPanel;
    }

    private void salvarConsulta() {
        String pacienteCpf = cpfField.getText();
        String data = dataField.getText();
        String hora = horarioField.getText();

        String medicoNome = (String) medicosComboBox.getSelectedItem();
        String tipoConsulta = (String) especialidadeComboBox.getSelectedItem();

        consultaService.salvarConsulta(pacienteCpf, data, hora, medicoNome, tipoConsulta);

        JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso!");
    }

    private void popularMedicos() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Selecione o médico");

        List<ResponsavelSaude> responsavelSaudeList = responsavelService.findAllResponsaveis();
        for (ResponsavelSaude rs : responsavelSaudeList) {
            model.addElement(rs.getNomeCompleto());
        }
        medicosComboBox.setModel(model);
    }

    private void popularEspecialidades() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Selecione o tipo de consulta");

        List<TipoConsulta> tipos = Arrays.stream(TipoConsulta.values()).toList();
        for (TipoConsulta tipo : tipos) {
            model.addElement(tipo.toString());
        }
        especialidadeComboBox.setModel(model);
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        Color placeholderColor = Color.GRAY;
        Color originalColor = new Color(0x1C2F5C);

        textField.setText(placeholder);
        textField.setForeground(placeholderColor);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(originalColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(placeholderColor);
                }
            }
        });
    }
}
