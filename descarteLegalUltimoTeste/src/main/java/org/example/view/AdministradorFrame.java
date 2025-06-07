package org.example.view;

import org.example.model.Administrador;
import org.example.service.Sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AdministradorFrame extends JFrame {
    private Sistema sistema;
    private Administrador administrador;

    private JTextField enderecoField;
    private JTextField nomeMoradorField;
    private JTextField cpfField;
    private JTextField dataCadastroField;
    private JButton cadastrarButton;

    public AdministradorFrame(Sistema sistema, Administrador administrador) {
        this.sistema = sistema;
        this.administrador = administrador;

        setTitle("Maringá Ecoleta - Cadastro de Residência");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Painel Principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = new JLabel("Maringá Ecoleta");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setForeground(new Color(60, 179, 113));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Olá, Agente");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton sairButton = new JButton("Sair");
        sairButton.setBackground(new Color(60, 179, 113));
        sairButton.setForeground(Color.WHITE);
        sairButton.setFocusPainted(false);
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame(sistema).setVisible(true);
            }
        });
        userPanel.add(welcomeLabel);
        userPanel.add(sairButton);
        headerPanel.add(userPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245)); // Fundo do painel de conteúdo
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título da seção
        JLabel titleLabel = new JLabel("Cadastro de Residência");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(60, 179, 113));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(titleLabel, gbc);

        JLabel descriptionLabel = new JLabel("Cadastre novas residências participantes do programa Descarte Consciente.");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        contentPanel.add(descriptionLabel, gbc);

        // Campos do formulário
        gbc.gridwidth = 1; // Reseta para uma coluna para labels e campos
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 2;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Endereço completo:"), gbc);
        gbc.gridx = 1;
        enderecoField = new JTextField(30);
        enderecoField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        contentPanel.add(enderecoField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Nome do morador responsável:"), gbc);
        gbc.gridx = 1;
        nomeMoradorField = new JTextField(30);
        nomeMoradorField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        contentPanel.add(nomeMoradorField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        cpfField = new JTextField(15);
        cpfField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        contentPanel.add(cpfField, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Data do cadastro:"), gbc);
        gbc.gridx = 1;
        dataCadastroField = new JTextField("dd/mm/aaaa", 10);
        dataCadastroField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        contentPanel.add(dataCadastroField, gbc);

        // Botão Cadastrar
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setBackground(new Color(60, 179, 113));
        cadastrarButton.setForeground(Color.WHITE);
        cadastrarButton.setFont(new Font("Arial", Font.BOLD, 16));
        cadastrarButton.setFocusPainted(false);
        contentPanel.add(cadastrarButton, gbc);

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String endereco = enderecoField.getText();
                String nomeMorador = nomeMoradorField.getText();
                String cpf = cpfField.getText();
                String dataStr = dataCadastroField.getText();

                if (endereco.isEmpty() || nomeMorador.isEmpty() || cpf.isEmpty() || dataStr.isEmpty()) {
                    JOptionPane.showMessageDialog(AdministradorFrame.this,
                            "Todos os campos são obrigatórios.",
                            "Erro de Cadastro",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                LocalDate dataCadastro;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dataCadastro = LocalDate.parse(dataStr, formatter);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(AdministradorFrame.this,
                            "Formato de data inválido. Use dd/mm/aaaa.",
                            "Erro de Data",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (sistema.cadastrarResidencia(endereco, nomeMorador, cpf, dataCadastro)) {
                    JOptionPane.showMessageDialog(AdministradorFrame.this,
                            "Residência cadastrada com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Limpar campos
                    enderecoField.setText("");
                    nomeMoradorField.setText("");
                    cpfField.setText("");
                    dataCadastroField.setText("dd/mm/aaaa");
                } else {
                    JOptionPane.showMessageDialog(AdministradorFrame.this,
                            "Erro ao cadastrar residência. Endereço já existe ou dados inválidos.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
