package br.com.chronosgeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidade que representa a tabela GS_OCORRENCIA.
 * Registro de eventos ambientais detectados pelos satelites.
 * Relacionamentos:
 *   - ManyToOne com GS_REGIAO  (id_regiao)
 *   - ManyToOne com GS_TIPO_EVENTO (id_tipo_evento)
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Entity
@Table(name = "GS_OCORRENCIA")
public class Ocorrencia {

    @Id
    @Column(name = "ID_OCORRENCIA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "seq_ocorrencia_gen")
    @SequenceGenerator(name = "seq_ocorrencia_gen",
                       sequenceName = "seq_ocorrencia",
                       allocationSize = 1)
    private Long idOcorrencia;

    @NotNull(message = "Regiao e obrigatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_REGIAO", nullable = false,
                foreignKey = @ForeignKey(name = "FK_OCO_REGIAO"))
    private Regiao regiao;

    @NotNull(message = "Tipo de evento e obrigatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO_EVENTO", nullable = false,
                foreignKey = @ForeignKey(name = "FK_OCO_TIPO"))
    private TipoEvento tipoEvento;

    @NotNull(message = "Data de deteccao e obrigatoria")
    @Column(name = "DATA_DETECCAO", nullable = false)
    private LocalDate dataDeteccao;

    @Column(name = "DATA_ENCERRAMENTO")
    private LocalDate dataEncerramento;

    @PositiveOrZero(message = "Area afetada nao pode ser negativa")
    @Column(name = "AREA_AFETADA_KM2")
    private Double areaAfetadaKm2;

    @NotBlank(message = "Nivel de severidade e obrigatorio")
    @Pattern(regexp = "BAIXO|MEDIO|ALTO|CRITICO",
             message = "Severidade deve ser: BAIXO, MEDIO, ALTO ou CRITICO")
    @Column(name = "NIVEL_SEVERIDADE", nullable = false, length = 10)
    private String nivelSeveridade;

    @Pattern(regexp = "ATIVA|MONITORANDO|ENCERRADA",
             message = "Status deve ser: ATIVA, MONITORANDO ou ENCERRADA")
    @Column(name = "STATUS_OCORRENCIA", length = 20)
    private String statusOcorrencia = "ATIVA";

    @NotBlank(message = "Fonte dos dados e obrigatoria")
    @Size(max = 100, message = "Fonte deve ter no maximo 100 caracteres")
    @Column(name = "FONTE_DADOS", nullable = false, length = 100)
    private String fonteDados;

    @Size(max = 500, message = "Observacoes devem ter no maximo 500 caracteres")
    @Column(name = "OBSERVACOES", length = 500)
    private String observacoes;

    // ── Construtores ──────────────────────────────────────────────────────

    public Ocorrencia() {}

    // ── Getters e Setters ─────────────────────────────────────────────────

    public Long getIdOcorrencia() { return idOcorrencia; }
    public void setIdOcorrencia(Long idOcorrencia) { this.idOcorrencia = idOcorrencia; }

    public Regiao getRegiao() { return regiao; }
    public void setRegiao(Regiao regiao) { this.regiao = regiao; }

    public TipoEvento getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(TipoEvento tipoEvento) { this.tipoEvento = tipoEvento; }

    public LocalDate getDataDeteccao() { return dataDeteccao; }
    public void setDataDeteccao(LocalDate dataDeteccao) { this.dataDeteccao = dataDeteccao; }

    public LocalDate getDataEncerramento() { return dataEncerramento; }
    public void setDataEncerramento(LocalDate dataEncerramento) { this.dataEncerramento = dataEncerramento; }

    public Double getAreaAfetadaKm2() { return areaAfetadaKm2; }
    public void setAreaAfetadaKm2(Double areaAfetadaKm2) { this.areaAfetadaKm2 = areaAfetadaKm2; }

    public String getNivelSeveridade() { return nivelSeveridade; }
    public void setNivelSeveridade(String nivelSeveridade) { this.nivelSeveridade = nivelSeveridade; }

    public String getStatusOcorrencia() { return statusOcorrencia; }
    public void setStatusOcorrencia(String statusOcorrencia) { this.statusOcorrencia = statusOcorrencia; }

    public String getFonteDados() { return fonteDados; }
    public void setFonteDados(String fonteDados) { this.fonteDados = fonteDados; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
