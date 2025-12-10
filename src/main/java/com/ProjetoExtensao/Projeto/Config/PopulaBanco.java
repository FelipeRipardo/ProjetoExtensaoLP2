package com.ProjetoExtensao.Projeto.Config;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.ResponsavelRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class PopulaBanco implements CommandLineRunner {

    private final PacienteRepositorio pacienteRepositorio;
    private final ConsultaRepositorio consultaRepositorio;
    private final ResponsavelRepositorio responsavelRepositorio;

    public PopulaBanco(PacienteRepositorio pacienteRepositorio,
                       ConsultaRepositorio consultaRepositorio,
                       ResponsavelRepositorio responsavelRepositorio) {
        this.pacienteRepositorio = pacienteRepositorio;
        this.consultaRepositorio = consultaRepositorio;
        this.responsavelRepositorio = responsavelRepositorio;
    }

    @Override
    public void run(String... args) throws Exception {
        //Evita duplicidade
        if (responsavelRepositorio.count() > 0) {
            return;
        }

        System.out.println("--- INICIANDO POPULAÇÃO DO BANCO H2 (10 PACIENTES) ---");

        //1 - Criar usuário admin
        ResponsavelSaude admin = new ResponsavelSaude();
        admin.setNomeCompleto("Enfermeira Chefe");
        admin.setEmail("admin@email.com"); // LOGIN
        admin.setSenha("1234");            // SENHA

        responsavelRepositorio.save(admin);
        System.out.println(">>> USUÁRIO CRIADO: admin@email.com | 1234");

        //2 - Criar pacientes(10 para popular o banco)
        Paciente p1 = new Paciente("Maria da Silva", "123.456.789-01", LocalDate.of(1945, 5, 20), "Ana da Silva", "700100000000001", LocalDate.of(2020, 1, 10));
        Paciente p2 = new Paciente("Josefa Santos", "987.654.321-02", LocalDate.of(1950, 8, 15), "Maria Santos", "700200000000002", LocalDate.of(2021, 3, 22));
        Paciente p3 = new Paciente("Francisca Oliveira", "111.222.333-03", LocalDate.of(1938, 12, 25), "Luzia Oliveira", "700300000000003", LocalDate.of(2019, 11, 05));
        Paciente p4 = new Paciente("Antonia Costa", "222.333.444-04", LocalDate.of(1942, 6, 10), "Rita Costa", "700400000000004", LocalDate.of(2022, 5, 14));
        Paciente p5 = new Paciente("Raimunda Pereira", "333.444.555-05", LocalDate.of(1935, 2, 28), "Joana Pereira", "700500000000005", LocalDate.of(2018, 9, 30));
        Paciente p6 = new Paciente("Tereza de Jesus", "444.555.666-06", LocalDate.of(1955, 10, 12), "Clara de Jesus", "700600000000006", LocalDate.of(2023, 1, 15));
        Paciente p7 = new Paciente("Sebastiana Lima", "555.666.777-07", LocalDate.of(1948, 7, 7), "Rosa Lima", "700700000000007", LocalDate.of(2021, 12, 01));
        Paciente p8 = new Paciente("Ana Paula Souza", "666.777.888-08", LocalDate.of(1952, 4, 18), "Vera Souza", "700800000000008", LocalDate.of(2020, 6, 20));
        Paciente p9 = new Paciente("Lucia Ferreira", "777.888.999-09", LocalDate.of(1940, 9, 9), "Elena Ferreira", "700900000000009", LocalDate.of(2019, 2, 28));
        Paciente p10 = new Paciente("Geralda Alves", "888.999.000-10", LocalDate.of(1939, 1, 30), "Julia Alves", "701000000000010", LocalDate.of(2022, 8, 10));

        List<Paciente> listaPacientes = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
        pacienteRepositorio.saveAll(listaPacientes);

        //3 - Criar consultas
        try {
            List<Consulta> listaConsultas = Arrays.asList(
                    //Consultas Recentes
                    new Consulta(LocalDate.now().minusDays(1), LocalTime.of(0, 0), "Rotina", admin, p1),
                    new Consulta(LocalDate.now().minusDays(2), LocalTime.of(0, 0), "Emergencia", admin, p2),
                    new Consulta(LocalDate.now().minusDays(3), LocalTime.of(0, 0), "Rotina", admin, p3),
                    new Consulta(LocalDate.now().minusDays(5), LocalTime.of(0, 0), "Acompanhamento", admin, p4),

                    //Consultas Antigas (Histórico)
                    new Consulta(LocalDate.now().minusMonths(1), LocalTime.of(0, 0), "Rotina", admin, p5),
                    new Consulta(LocalDate.now().minusMonths(2), LocalTime.of(0, 0), "Emergencia", admin, p6),
                    new Consulta(LocalDate.now().minusWeeks(2), LocalTime.of(0, 0), "Rotina", admin, p1), // Maria de novo
                    new Consulta(LocalDate.now().minusWeeks(3), LocalTime.of(0, 0), "Exames", admin, p7)
            );

            consultaRepositorio.saveAll(listaConsultas);
            System.out.println("--- CONSULTAS E PACIENTES INSERIDOS COM SUCESSO ---");

        } catch (Exception e) {
            System.out.println("Erro ao criar consultas de teste: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- BANCO H2 PRONTO PARA APRESENTAÇÃO ---");
    }
}