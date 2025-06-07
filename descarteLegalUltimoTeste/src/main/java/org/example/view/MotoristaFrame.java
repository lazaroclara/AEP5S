package org.example.view;

import org.example.model.Motorista;
import org.example.model.Residencia;
import org.example.service.Sistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class MotoristaFrame extends JFrame {
    private Sistema sistema;
    private Motorista motorista;

    private JTable residenciaTable;
    private DefaultTableModel tableModel;

    public MotoristaFrame(Sistema sistema, Motorista motorista) {
        this.sistema = sistema;
        this.motorista = motorista;

        setTitle("Maringá Ecoleta - Controle de Descontos e Participações");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
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
        JLabel welcomeLabel = new JLabel("Olá, Administrador"); // Na sua imagem está Administrador, mas é para o Motorista
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

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(20, 20)); // Espaçamento
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        contentPanel.setBackground(new Color(245, 245, 245));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Título e descrição
        JPanel titleDescriptionPanel = new JPanel(new BorderLayout());
        titleDescriptionPanel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Controle de Descontos e Participações");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(60, 179, 113));
        titleDescriptionPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel descriptionLabel = new JLabel("Acompanhe a adesão dos moradores ao programa e aplique os descontos conforme o desempenho.");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(Color.GRAY);
        titleDescriptionPanel.add(descriptionLabel, BorderLayout.CENTER);
        contentPanel.add(titleDescriptionPanel, BorderLayout.NORTH);

        // Tabela de residências
        String[] columnNames = {"Endereço", "Participações", "Desconto (%)", "Última Coleta", "Ações"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Apenas a coluna "Ações" é editável (para o botão)
            }
        };
        residenciaTable = new JTable(tableModel);
        residenciaTable.setRowHeight(30); // Altura da linha
        residenciaTable.setFont(new Font("Arial", Font.PLAIN, 14));
        residenciaTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        residenciaTable.getTableHeader().setBackground(new Color(220, 220, 220));
        residenciaTable.setSelectionBackground(new Color(173, 216, 230)); // Cor de seleção

        // Renderizador e Editor de célula para o botão "Atualizar"
        residenciaTable.getColumn("Ações").setCellRenderer(new ButtonRenderer());
        residenciaTable.getColumn("Ações").setCellEditor(new ButtonEditor(new JTextField(), sistema, this));

        JScrollPane scrollPane = new JScrollPane(residenciaTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setBackground(Color.WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        carregarDadosTabela();
    }

    public void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa os dados existentes
        List<Residencia> residencias = sistema.getResidencias();

        for (Residencia res : residencias) {
            Object[] rowData = {
                    res.getEnderecoCompleto(),
                    res.getParticipacoes() + " de 6", // Formato "5 de 6"
                    (int) res.getDesconto() + "%",     // Formato "10%"
                    res.getUltimaColetaFormatada(),
                    "Atualizar" // Texto do botão
            };
            tableModel.addRow(rowData);
        }
    }

    // Classe interna para renderizar o botão na célula da tabela
    class ButtonRenderer extends DefaultCellEditor implements TableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            super(new JTextField()); // Componente dummy
            button = new JButton("Atualizar");
            button.setBackground(new Color(60, 179, 113));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return button;
        }
    }

    // Classe interna para editar o botão na célula da tabela
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private Sistema sistema;
        private MotoristaFrame parentFrame;

        public ButtonEditor(JTextField textField, Sistema sistema, MotoristaFrame parentFrame) {
            super(textField);
            this.sistema = sistema;
            this.parentFrame = parentFrame;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped(); // Notifica que a edição parou
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(new Color(60, 179, 113));
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Lógica de atualização
                int selectedRow = residenciaTable.getSelectedRow();
                if (selectedRow != -1) {
                    String endereco = (String) residenciaTable.getValueAt(selectedRow, 0);
                    Residencia residencia = sistema.buscarResidenciaPorEndereco(endereco);

                    if (residencia != null) {
                        // Simplesmente incrementa a participação e atualiza a data da coleta
                        int novaParticipacao = residencia.getParticipacoes() + 1;
                        if (novaParticipacao > 6) { // Limita as participações a 6, como no exemplo "5 de 6"
                            novaParticipacao = 6;
                        }
                        residencia.setParticipacoes(novaParticipacao);
                        residencia.setUltimaColeta(LocalDate.now()); // Data da coleta de hoje
                        sistema.atualizarResidencia(residencia);
                        parentFrame.carregarDadosTabela(); // Recarrega a tabela para refletir as mudanças
                        JOptionPane.showMessageDialog(parentFrame, "Residência atualizada com sucesso!");
                    }
                }
            }
            isPushed = false;
            return new String(label);
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
