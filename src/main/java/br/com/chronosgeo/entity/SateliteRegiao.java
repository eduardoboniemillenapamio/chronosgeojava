package br.com.chronosgeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entidade que representa a tabela associativa GS_SATELITE_REGIAO.
 * Implementa o relacionamento N:N entre GS_SATELITE e GS_REGIAO.
 *
 * Chave primária composta: (id_satelite, id_regiao)
 *
 * Banco:
 *   id_satelite     NUMBER(10)  FK -> GS_SATELITE
 *   id_regiao       NUMBER(10)  FK -> GS_REGIAO
 *   data_inicio     DATE        NOT NULL
 *   data_fim        DATE        (nullable)
 *   frequencia_dias NUMBER(3)   CHECK BETWEEN 1 AND 30
 *   resolucao_m     NUMBER(8,1) CHECK > 0
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Entity
@Table(name = "GS_SATELITE_REGIAO")
public class SateliteRegiao {

    // ── Chave primária composta via classe embarcada ───────────────────────
    @EmbeddedId
    private SateliteRegiaoId id = new SateliteRegiaoId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idSatelite")
    @JoinColumn(name = "ID_SATELITE",
                foreignKey = @ForeignKey(name = "FK_SR_SATELITE"))
    private Satelite satelite;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idRegiao")
    @JoinColumn(name = "ID_REGIAO",
                foreignKey = @ForeignKey(name = "FK_SR_REGIAO"))
    private Regiao regiao;

    @NotNull(message = "Data de inicio e obrigatoria")
    @Column(name = "DATA_INICIO", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "DATA_FIM")
    private LocalDate dataFim;

    @NotNull(message = "Frequencia de revisita e obrigatoria")
    @Min(value = 1,  message = "Frequencia minima e 1 dia")
    @Max(value = 30, message = "Frequencia maxima e 30 dias")
    @Column(name = "FREQUENCIA_DIAS", nullable = false)
    private Integer frequenciaDias;

    @NotNull(message = "Resolucao espacial e obrigatoria")
    @Positive(message = "Resolucao deve ser positiva")
    @Column(name = "RESOLUCAO_M", nullable = false)
    private Double resolucaoM;

    // ── Chave composta embarcada ──────────────────────────────────────────

    @Embeddable
    public static class SateliteRegiaoId implements Serializable {

        @Column(name = "ID_SATELITE")
        private Long idSatelite;

        @Column(name = "ID_REGIAO")
        private Long idRegiao;

        public SateliteRegiaoId() {}

        public SateliteRegiaoId(Long idSatelite, Long idRegiao) {
            this.idSatelite = idSatelite;
            this.idRegiao   = idRegiao;
        }

        public Long getIdSatelite() { return idSatelite; }
        public void setIdSatelite(Long idSatelite) { this.idSatelite = idSatelite; }

        public Long getIdRegiao() { return idRegiao; }
        public void setIdRegiao(Long idRegiao) { this.idRegiao = idRegiao; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SateliteRegiaoId)) return false;
            SateliteRegiaoId that = (SateliteRegiaoId) o;
            return java.util.Objects.equals(idSatelite, that.idSatelite)
                && java.util.Objects.equals(idRegiao,   that.idRegiao);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(idSatelite, idRegiao);
        }
    }

    // ── Construtores ──────────────────────────────────────────────────────

    public SateliteRegiao() {}

    // ── Getters e Setters ─────────────────────────────────────────────────

    public SateliteRegiaoId getId() { return id; }
    public void setId(SateliteRegiaoId id) { this.id = id; }

    public Satelite getSatelite() { return satelite; }
    public void setSatelite(Satelite satelite) { this.satelite = satelite; }

    public Regiao getRegiao() { return regiao; }
    public void setRegiao(Regiao regiao) { this.regiao = regiao; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public Integer getFrequenciaDias() { return frequenciaDias; }
    public void setFrequenciaDias(Integer frequenciaDias) { this.frequenciaDias = frequenciaDias; }

    public Double getResolucaoM() { return resolucaoM; }
    public void setResolucaoM(Double resolucaoM) { this.resolucaoM = resolucaoM; }
}
