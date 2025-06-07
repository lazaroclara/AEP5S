package org.example.controller;

import org.example.model.Administrador;
import org.example.model.Motorista;
import org.example.model.Usuario;
import org.example.service.Sistema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession; // Para gerenciar a sessão do usuário

@Controller
public class LoginController {

    private final Sistema sistema;

    @Autowired // O Spring injeta a instância de Sistema aqui
    public LoginController(Sistema sistema) {
        this.sistema = sistema;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Nada de especial para passar para o formulário de login por enquanto
        return "login"; // Retorna o nome do template Thymeleaf (login.html)
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String senha,
                               HttpSession session, // Para armazenar o usuário na sessão
                               RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = sistema.autenticar(email, senha);

        if (usuarioLogado != null) {
            session.setAttribute("usuarioLogado", usuarioLogado); // Armazena o usuário na sessão
            if (usuarioLogado instanceof Administrador) {
                return "redirect:/admin/dashboard"; // Redireciona para o dashboard do admin
            } else if (usuarioLogado instanceof Motorista) {
                return "redirect:/motorista/dashboard"; // Redireciona para o dashboard do motorista
            }
        }

        // Se o login falhar
        redirectAttributes.addFlashAttribute("errorMessage", "Email ou senha inválidos.");
        return "redirect:/login"; // Redireciona de volta para a tela de login
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalida a sessão (remove o usuário logado)
        return "redirect:/login";
    }
}