package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Rota {
    private String nome;
    private List<String> enderecosNaRota; // Lista de endereços que compõem esta rota
    private boolean rotaConcluidaPeloMotorista; // Indica se o motorista já "passou" por esta rota
    private boolean rotaValidadaPeloAdmin; // Indica se o administrador já validou esta rota

    public Rota(String nome, List<String> enderecosNaRota) {
        this.nome = nome;
        this.enderecosNaRota = new ArrayList<>(enderecosNaRota); // Copia para evitar modificações externas
        this.rotaConcluidaPeloMotorista = false;
        this.rotaValidadaPeloAdmin = false;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getEnderecosNaRota() {
        return enderecosNaRota;
    }

    public boolean isRotaConcluidaPeloMotorista() {
        return rotaConcluidaPeloMotorista;
    }

    public void setRotaConcluidaPeloMotorista(boolean rotaConcluidaPeloMotorista) {
        this.rotaConcluidaPeloMotorista = rotaConcluidaPeloMotorista;
    }

    public boolean isRotaValidadaPeloAdmin() {
        return rotaValidadaPeloAdmin;
    }

    public void setRotaValidadaPeloAdmin(boolean rotaValidadaPeloAdmin) {
        this.rotaValidadaPeloAdmin = rotaValidadaPeloAdmin;
    }

    // Método para adicionar um endereço à rota
    public void adicionarEndereco(String endereco) {
        if (!enderecosNaRota.contains(endereco)) {
            enderecosNaRota.add(endereco);
        }
    }
}