package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Residencia {
    private String enderecoCompleto;
    private String nomeMoradorResponsavel;
    private String cpf;
    private LocalDate dataCadastro;
    private int participacoes;
    private double desconto;
    private LocalDate ultimaColeta;

    public Residencia(String enderecoCompleto, String nomeMoradorResponsavel, String cpf, LocalDate dataCadastro) {
        this.enderecoCompleto = enderecoCompleto;
        this.nomeMoradorResponsavel = nomeMoradorResponsavel;
        this.cpf = cpf;
        this.dataCadastro = dataCadastro;
        this.participacoes = 0; // Inicia com 0 participações
        this.desconto = 0.0; // Inicia com 0% de desconto
        this.ultimaColeta = null; // Nenhuma coleta ainda
    }

    // Getters
    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public String getNomeMoradorResponsavel() {
        return nomeMoradorResponsavel;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public int getParticipacoes() {
        return participacoes;
    }

    public double getDesconto() {
        return desconto;
    }

    public LocalDate getUltimaColeta() {
        return ultimaColeta;
    }

    // Setters (para atualização dos dados do motorista)
    public void setParticipacoes(int participacoes) {
        this.participacoes = participacoes;
        calcularDesconto(); // Recalcula o desconto sempre que as participações mudam
    }

    public void setUltimaColeta(LocalDate ultimaColeta) {
        this.ultimaColeta = ultimaColeta;
    }

    // Lógica para calcular o desconto baseado nas participações
    private void calcularDesconto() {
        if (participacoes >= 5) { // Exemplo: 5 participações ou mais = 10%
            this.desconto = 10.0;
        } else if (participacoes >= 2) { // Exemplo: 2 a 4 participações = 3%
            this.desconto = 3.0;
        } else {
            this.desconto = 0.0;
        }
    }

    // Método auxiliar para formatar a data de cadastro (útil para exibição)
    public String getDataCadastroFormatada() {
        return dataCadastro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Método auxiliar para formatar a data da última coleta (útil para exibição)
    public String getUltimaColetaFormatada() {
        return ultimaColeta != null ? ultimaColeta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
    }
}