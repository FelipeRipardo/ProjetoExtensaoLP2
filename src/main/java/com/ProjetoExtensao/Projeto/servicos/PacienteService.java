package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PacienteService {
    private PacienteRepositorio pacienteRepositorio;

    public void salvarPaciente(Paciente paciente) {
        pacienteRepositorio.save(paciente);
    }

    public List<Paciente> findPacientesByNomeCompleto(String nome) {
        return pacienteRepositorio.findByNomeCompletoContainingIgnoreCase(nome);
    }

    public List<Paciente> findPacientesByCpf(String cpf) {
        return pacienteRepositorio.findByCpfContaining(cpf);
    }

    public List<Paciente> findAllPacientes() {
        return pacienteRepositorio.findAll();
    }

    public Paciente findPacienteByCpf(String cpf) {
        return pacienteRepositorio.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }
}
