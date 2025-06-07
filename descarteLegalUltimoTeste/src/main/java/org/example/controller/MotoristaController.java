package org.example.controller;

import org.example.model.Motorista;
import org.example.model.Rota; // NOVO IMPORT
import org.example.service.Sistema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/motorista")
public class MotoristaController {

    private final Sistema sistema;

    @Autowired
    public MotoristaController(Sistema sistema) {
        this.sistema = sistema;
    }

    private boolean isMotorista(HttpSession session) {
        Object user = session.getAttribute("usuarioLogado");
        return user instanceof Motorista;
    }

    // NOVO: Tela principal do Motorista para selecionar a rota
    @GetMapping("/dashboard")
    public String showMotoristaDashboard(HttpSession session, Model model) {
        if (!isMotorista(session)) {
            return "redirect:/login";
        }
        List<Rota> rotasDisponiveis = sistema.getRotas();
        model.addAttribute("rotas", rotasDisponiveis);
        return "motorista_selecao_rota"; // Novo template
    }

    // NOVO: Tela para simular o mapa da rota
    @GetMapping("/ver-rota")
    public String showRotaMapa(@RequestParam String nomeRota, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!isMotorista(session)) {
            return "redirect:/login";
        }
        Rota rotaSelecionada = sistema.buscarRotaPorNome(nomeRota);
        if (rotaSelecionada == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rota não encontrada.");
            return "redirect:/motorista/dashboard";
        }
        model.addAttribute("rota", rotaSelecionada);
        model.addAttribute("enderecos", rotaSelecionada.getEnderecosNaRota()); // Para exibir no mapa simulado
        return "motorista_mapa_rota"; // Novo template
    }

    // NOVO: Lógica para "Concluir Rota"
    @PostMapping("/concluir-rota")
    public String concluirRota(@RequestParam String nomeRota, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isMotorista(session)) {
            return "redirect:/login";
        }
        sistema.registrarConclusaoRotaPeloMotorista(nomeRota);
        redirectAttributes.addFlashAttribute("successMessage", "Rota '" + nomeRota + "' marcada como concluída! Aguarde validação do administrador.");
        return "redirect:/motorista/dashboard"; // Volta para a seleção de rotas
    }
}