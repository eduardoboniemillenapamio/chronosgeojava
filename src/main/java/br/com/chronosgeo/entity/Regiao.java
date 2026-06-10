package br.com.chronosgeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidade que representa a tabela GS_REGIAO.
 * Regioes geograficas brasileiras monitoradas pelos satelites.
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Entity
@Table(name = "GS_REGIAO")
public class Regiao {

    @Id
    @Column(name = "ID_REGIAO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "seq_regiao_gen")
    @SequenceGenerator(name = "seq_regiao_gen",
                       sequenceName = "seq_regiao",
                       allocationSize = 1)
    private Long idRegiao;

    @NotBlank(message = "Nome da regiao e obrigatorio")
    @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
    @Column(name = "NOME_REGIAO", nullable = false, length = 100)
    private String nomeRegiao;

    @NotBlank(message = "Estado e obrigatorio")
    @Size(min = 2, max = 2, message = "Estado deve ser a sigla UF com 2 caracteres")
    @Column(name = "ESTADO", nullable = false, length = 2)
    private String estado;

    @NotBlank(message = "Bioma e obrigatorio")
    @Pattern(regexp = "AMAZONIA|CERRADO|MATA_ATLANTICA|CAATINGA|PANTANAL|PAMPA",
             message = "Bioma invalido. Use: AMAZONIA, CERRADO, MATA_ATLANTICA, CAATINGA, PANTANAL ou PAMPA")
    @Column(name = "BIOMA", nullable = false, length = 20)
    private String bioma;

    @NotNull(message = "Area em km2 e obrigatoria")
    @Positive(message = "Area deve ser um valor positivo")
    @Column(name = "AREA_KM2", nullable = false)
    private Double areaKm2;

    @NotNull(message = "Latitude e obrigatoria")
    @Column(name = "LATITUDE", nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude e obrigatoria")
    @Column(name = "LONGITUDE", nullable = false)
    private Double longitude;

    @Min(value = 0, message = "Populacao em risco nao pode ser negativa")
    @Column(name = "POPULACAO_RISCO")
    private Long populacaoRisco = 0L;

    // ── Construtores ──────────────────────────────────────────────────────

    public Regiao() {}

    // ── Getters e Setters ─────────────────────────────────────────────────

    public Long getIdRegiao() { return idRegiao; }
    public void setIdRegiao(Long idRegiao) { this.idRegiao = idRegiao; }

    public String getNomeRegiao() { return nomeRegiao; }
    public void setNomeRegiao(String nomeRegiao) { this.nomeRegiao = nomeRegiao; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getBioma() { return bioma; }
    public void setBioma(String bioma) { this.bioma = bioma; }

    public Double getAreaKm2() { return areaKm2; }
    public void setAreaKm2(Double areaKm2) { this.areaKm2 = areaKm2; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Long getPopulacaoRisco() { return populacaoRisco; }
    public void setPopulacaoRisco(Long populacaoRisco) { this.populacaoRisco = populacaoRisco; }
}
