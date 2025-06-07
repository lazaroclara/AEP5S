package org.example.service;

import org.example.model.Administrador;
import org.example.model.Motorista;
import org.example.model.Residencia;
import org.example.model.Rota; // NOVO IMPORT
import org.example.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays; // NOVO IMPORT
import java.util.List;
import java.util.stream.Collectors; // NOVO IMPORT

@Service
public class Sistema {
    private List<Usuario> usuarios;
    private List<Residencia> residencias;
    private List<Rota> rotas; // NOVA LISTA DE ROTAS

    public Sistema() {
        this.usuarios = new ArrayList<>();
        this.residencias = new ArrayList<>();
        this.rotas = new ArrayList<>(); // Inicializa a lista de rotas
        inicializarDados(); // Adiciona alguns dados de exemplo
    }

    private void inicializarDados() {
        // Usuários de exemplo
        usuarios.add(new Administrador("admin@ecoleeta.mga.br", "admin123"));
        usuarios.add(new Motorista("motorista@ecoleeta.mga.br", "senha123"));

        // Residências de exemplo
        // Vamos criar residências que podem ser parte de rotas
        Residencia res1 = new Residencia("Rua das Flores, 123", "Maria Silva", "111.111.111-11", LocalDate.of(2025, 3, 10));
        res1.setParticipacoes(5);
        res1.setUltimaColeta(LocalDate.of(2025, 5, 20));
        residencias.add(res1);

        Residencia res2 = new Residencia("Av. Brasil, 456", "João Souza", "222.222.222-22", LocalDate.of(2025, 4, 1));
        res2.setParticipacoes(2);
        res2.setUltimaColeta(LocalDate.of(2025, 5, 19));
        residencias.add(res2);

        Residencia res3 = new Residencia("Rua XV de Novembro, 789", "Ana Costa", "333.333.333-33", LocalDate.of(2025, 2, 15));
        res3.setParticipacoes(0);
        residencias.add(res3);

        Residencia res4 = new Residencia("Rua A, 10", "Carlos Pereira", "444.444.444-44", LocalDate.of(2025, 1, 5));
        res4.setParticipacoes(1);
        residencias.add(res4);

        Residencia res5 = new Residencia("Av. Central, 500", "Fernanda Lima", "555.555.555-55", LocalDate.of(2025, 4, 20));
        res5.setParticipacoes(3);
        residencias.add(res5);

        // Rotas de exemplo
        Rota rota1 = new Rota("Rota Centro Histórico", Arrays.asList("Rua das Flores, 123", "Rua XV de Novembro, 789"));
        rotas.add(rota1);

        Rota rota2 = new Rota("Rota Zona Sul", Arrays.asList("Av. Brasil, 456", "Rua A, 10", "Av. Central, 500"));
        rotas.add(rota2);
    }

    public Usuario autenticar(String email, String senha) {
        for (Usuario usuario : usuarios) {
            if (usuario.autenticar(email, senha)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean cadastrarResidencia(String enderecoCompleto, String nomeMoradorResponsavel, String cpf, LocalDate dataCadastro) {
        for (Residencia res : residencias) {
            if (res.getEnderecoCompleto().equalsIgnoreCase(enderecoCompleto)) {
                return false;
            }
        }
        Residencia novaResidencia = new Residencia(enderecoCompleto, nomeMoradorResponsavel, cpf, dataCadastro);
        residencias.add(novaResidencia);
        return true;
    }

    public List<Residencia> getResidencias() {
        return new ArrayList<>(residencias);
    }

    public void atualizarResidencia(Residencia residenciaAtualizada) {
        for (int i = 0; i < residencias.size(); i++) {
            if (residencias.get(i).getEnderecoCompleto().equals(residenciaAtualizada.getEnderecoCompleto())) {
                residencias.set(i, residenciaAtualizada);
                return;
            }
        }
    }

    public Residencia buscarResidenciaPorEndereco(String endereco) {
        for (Residencia residencia : residencias) {
            if (residencia.getEnderecoCompleto().equalsIgnoreCase(endereco)) {
                return residencia;
            }
        }
        return null;
    }

    // Métodos para Rotas
    public List<Rota> getRotas() {
        return new ArrayList<>(rotas);
    }

    public Rota buscarRotaPorNome(String nome) {
        for (Rota rota : rotas) {
            if (rota.getNome().equalsIgnoreCase(nome)) {
                return rota;
            }
        }
        return null;
    }

    // Novo método: Atualizar participação de residências em uma rota
    public void registrarConclusaoRotaPeloMotorista(String nomeRota) {
        Rota rota = buscarRotaPorNome(nomeRota);
        if (rota != null) {
            rota.setRotaConcluidaPeloMotorista(true);
            // Aqui podemos adicionar lógica para "simular" que o motorista passou e coletou.
            // Para simplificar, vamos deixar a atualização individual para o admin.
        }
    }

    // Novo método: Obter residências de uma rota que o motorista já "passou" para o admin validar
    public List<Residencia> getResidenciasParaValidacaoAdmin(String nomeRota) {
        Rota rota = buscarRotaPorNome(nomeRota);
        if (rota != null && rota.isRotaConcluidaPeloMotorista() && !rota.isRotaValidadaPeloAdmin()) {
            return rota.getEnderecosNaRota().stream()
                    .map(this::buscarResidenciaPorEndereco)
                    .filter(r -> r != null) // Filtra endereços que podem não estar cadastrados como residências
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    // Novo método: Marcar rota como validada pelo admin
    public void marcarRotaValidadaPeloAdmin(String nomeRota) {
        Rota rota = buscarRotaPorNome(nomeRota);
        if (rota != null) {
            rota.setRotaValidadaPeloAdmin(true);
        }
    }
}