package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultaRepositorio extends JpaRepository<Consulta, Long> {
    Optional<Consulta> findByPaciente(Paciente paciente);
}
