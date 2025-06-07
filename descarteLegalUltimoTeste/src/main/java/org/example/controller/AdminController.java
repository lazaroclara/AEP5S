package org.example.controller;

import org.example.model.Administrador;
import org.example.model.Residencia;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List; // NOVO IMPORT

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Sistema sistema;

    @Autowired
    public AdminController(Sistema sistema) {
        this.sistema = sistema;
    }

    private boolean isAdmin(HttpSession session) {
        Object user = session.getAttribute("usuarioLogado");
        return user instanceof Administrador;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        // Aqui você pode adicionar opções de menu para o Admin
        return "admin_dashboard"; // Tela principal do admin com opções de menu
    }

    // NOVO: Tela de Cadastro de Residência (mantém a mesma lógica anterior)
    @GetMapping("/cadastro-residencia")
    public String showCadastroResidenciaForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("message", "Cadastre novas residências participantes do programa Descarte Consciente.");
        return "admin_cadastro_residencia"; // Um novo template para o formulário de cadastro
    }

    @PostMapping("/cadastrarResidencia")
    public String cadastrarResidencia(@RequestParam String enderecoCompleto,
                                      @RequestParam String nomeMoradorResponsavel,
                                      @RequestParam String cpf,
                                      @RequestParam String dataCadastro,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        if (enderecoCompleto.isEmpty() || nomeMoradorResponsavel.isEmpty() || cpf.isEmpty() || dataCadastro.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Todos os campos são obrigatórios.");
            return "redirect:/admin/cadastro-residencia"; // Redireciona para o formulário de cadastro
        }

        LocalDate data;
        try {
            data = LocalDate.parse(dataCadastro, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Formato de data inválido. Use dd/mm/aaaa.");
            return "redirect:/admin/cadastro-residencia";
        }

        if (sistema.cadastrarResidencia(enderecoCompleto, nomeMoradorResponsavel, cpf, data)) {
            redirectAttributes.addFlashAttribute("successMessage", "Residência cadastrada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar residência. Endereço já existe ou dados inválidos.");
        }
        return "redirect:/admin/cadastro-residencia";
    }

    // NOVO: Tela de Controle de Coletas/Validação (antiga tela do motorista)
    @GetMapping("/controle-coletas")
    public String showControleColetas(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        List<Rota> rotasConcluidasNaoValidadas = sistema.getRotas().stream()
                .filter(Rota::isRotaConcluidaPeloMotorista)
                .filter(rota -> !rota.isRotaValidadaPeloAdmin())
                .toList(); // Java 16+ .toList()
        // ou .collect(Collectors.toList()); para versões anteriores

        model.addAttribute("rotasParaValidar", rotasConcluidasNaoValidadas);
        return "admin_controle_coletas"; // Novo template para esta tela
    }

    // NOVO: Ver detalhes da rota para validação
    @GetMapping("/validar-rota")
    public String validarRota(@RequestParam String nomeRota, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        Rota rota = sistema.buscarRotaPorNome(nomeRota);
        if (rota == null || !rota.isRotaConcluidaPeloMotorista() || rota.isRotaValidadaPeloAdmin()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rota inválida ou já validada.");
            return "redirect:/admin/controle-coletas";
        }

        List<Residencia> residenciasNaRota = sistema.getResidenciasParaValidacaoAdmin(nomeRota);
        model.addAttribute("rota", rota);
        model.addAttribute("residencias", residenciasNaRota);
        return "admin_validar_coleta_detalhes"; // Novo template para exibir as residências da rota
    }

    // NOVO: Atualizar participação de residência específica na validação do admin
    @PostMapping("/atualizarParticipacaoAdmin")
    public String atualizarParticipacaoAdmin(@RequestParam String endereco,
                                             @RequestParam String nomeRota, // Precisamos saber a rota para redirecionar
                                             HttpSession session,
                                             RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Residencia residencia = sistema.buscarResidenciaPorEndereco(endereco);
        if (residencia != null) {
            int novaParticipacao = residencia.getParticipacoes() + 1;
            if (novaParticipacao > 6) {
                novaParticipacao = 6;
            }
            residencia.setParticipacoes(novaParticipacao);
            residencia.setUltimaColeta(LocalDate.now());
            sistema.atualizarResidencia(residencia);
            redirectAttributes.addFlashAttribute("successMessage", "Participação de " + endereco + " atualizada!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Residência não encontrada.");
        }
        return "redirect:/admin/validar-rota?nomeRota=" + nomeRota; // Redireciona de volta para os detalhes da rota
    }

    // NOVO: Marcar rota como validada pelo admin
    @PostMapping("/marcarRotaValidada")
    public String marcarRotaValidada(@RequestParam String nomeRota, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        sistema.marcarRotaValidadaPeloAdmin(nomeRota);
        redirectAttributes.addFlashAttribute("successMessage", "Rota '" + nomeRota + "' marcada como validada e descontos aplicados!");
        return "redirect:/admin/controle-coletas";
    }
}