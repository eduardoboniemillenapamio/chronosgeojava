package br.com.chronosgeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidade que representa a tabela GS_SATELITE.
 * Armazena os satelites de monitoramento ambiental utilizados
 * pelo sistema ChronosGeo (NASA, ESA, INPE, NOAA).
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Entity
@Table(name = "GS_SATELITE")
public class Satelite {

    @Id
    @Column(name = "ID_SATELITE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "seq_satelite_gen")
    @SequenceGenerator(name = "seq_satelite_gen",
                       sequenceName = "seq_satelite",
                       allocationSize = 1)
    private Long idSatelite;

    @NotBlank(message = "Nome do satelite e obrigatorio")
    @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Agencia e obrigatoria")
    @Size(max = 50, message = "Agencia deve ter no maximo 50 caracteres")
    @Column(name = "AGENCIA", nullable = false, length = 50)
    private String agencia;

    @NotBlank(message = "Tipo de orbita e obrigatorio")
    @Pattern(regexp = "LEO|MEO|GEO|HEO",
             message = "Tipo de orbita deve ser: LEO, MEO, GEO ou HEO")
    @Column(name = "TIPO_ORBITA", nullable = false, length = 10)
    private String tipoOrbita;

    @NotNull(message = "Altitude e obrigatoria")
    @Positive(message = "Altitude deve ser um valor positivo")
    @Column(name = "ALTITUDE_KM", nullable = false)
    private Double altitudeKm;

    @NotNull(message = "Data de lancamento e obrigatoria")
    @Column(name = "DATA_LANCAMENTO", nullable = false)
    private LocalDate dataLancamento;

    @Pattern(regexp = "ATIVO|INATIVO|MANUTENCAO",
             message = "Status deve ser: ATIVO, INATIVO ou MANUTENCAO")
    @Column(name = "STATUS_OPERACAO", length = 20)
    private String statusOperacao = "ATIVO";

    // ── Construtores ──────────────────────────────────────────────────────

    public Satelite() {}

    // ── Getters e Setters ─────────────────────────────────────────────────

    public Long getIdSatelite() { return idSatelite; }
    public void setIdSatelite(Long idSatelite) { this.idSatelite = idSatelite; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }

    public String getTipoOrbita() { return tipoOrbita; }
    public void setTipoOrbita(String tipoOrbita) { this.tipoOrbita = tipoOrbita; }

    public Double getAltitudeKm() { return altitudeKm; }
    public void setAltitudeKm(Double altitudeKm) { this.altitudeKm = altitudeKm; }

    public LocalDate getDataLancamento() { return dataLancamento; }
    public void setDataLancamento(LocalDate dataLancamento) { this.dataLancamento = dataLancamento; }

    public String getStatusOperacao() { return statusOperacao; }
    public void setStatusOperacao(String statusOperacao) { this.statusOperacao = statusOperacao; }
}
