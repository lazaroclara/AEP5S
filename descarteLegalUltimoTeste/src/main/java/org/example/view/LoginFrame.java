package org.example.view;

import org.example.model.Administrador;
import org.example.model.Motorista;
import org.example.model.Usuario;
import org.example.service.Sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton entrarButton;
    private Sistema sistema;

    public LoginFrame(Sistema sistema) {
        this.sistema = sistema;
        setTitle("Descarte Legal - Acesso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centraliza a janela

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245)); // Cor de fundo suave
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo (opcional, pode ser uma imagem real depois)
        JLabel logoLabel = new JLabel("<html><center><h1>Descarte Legal</h1><p>Acesso para motoristas e administradores</p></center></html>");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(new Color(60, 179, 113)); // Verde vibrante
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(logoLabel, gbc);

        // Campo de Email
        JLabel emailLabel = new JLabel("Seu Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230))); // Borda azul claro
        emailField.setBackground(new Color(240, 255, 240)); // Verde bem claro
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        // Campo de Senha
        JLabel senhaLabel = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(senhaLabel, gbc);

        senhaField = new JPasswordField(20);
        senhaField.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230))); // Borda azul claro
        senhaField.setBackground(new Color(240, 255, 240)); // Verde bem claro
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(senhaField, gbc);

        // Botão Entrar
        entrarButton = new JButton("<html>&#8594; Entrar</html>"); // Seta para a direita com HTML
        entrarButton.setBackground(new Color(60, 179, 113)); // Verde vibrante
        entrarButton.setForeground(Color.WHITE);
        entrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        entrarButton.setFocusPainted(false); // Remove a borda de foco
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(entrarButton, gbc);

        // Dicas de login (como na imagem)
        JLabel dicasLogin = new JLabel("<html><p>Use 'motorista@ecoleeta.mga.br' / 'senha123' para motorista.<br>" +
                "Use 'admin@ecoleeta.mga.br' / 'admin123' para admin.</p></html>");
        dicasLogin.setFont(new Font("Arial", Font.PLAIN, 10));
        dicasLogin.setForeground(Color.GRAY);
        gbc.gridy = 4;
        panel.add(dicasLogin, gbc);


        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());

                Usuario usuarioLogado = sistema.autenticar(email, senha);

                if (usuarioLogado != null) {
                    dispose(); // Fecha a janela de login
                    if (usuarioLogado instanceof Administrador) {
                        new AdministradorFrame(sistema, (Administrador) usuarioLogado).setVisible(true);
                    } else if (usuarioLogado instanceof Motorista) {
                        new MotoristaFrame(sistema, (Motorista) usuarioLogado).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Email ou senha inválidos.",
                            "Erro de Login",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
