package br.com.chronosgeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidade que representa a tabela GS_ALERTA.
 * Alertas emitidos a orgaos responsaveis com base nas ocorrencias.
 * Relacionamento: ManyToOne com GS_OCORRENCIA.
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Entity
@Table(name = "GS_ALERTA")
public class Alerta {

    @Id
    @Column(name = "ID_ALERTA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "seq_alerta_gen")
    @SequenceGenerator(name = "seq_alerta_gen",
                       sequenceName = "seq_alerta",
                       allocationSize = 1)
    private Long idAlerta;

    @NotNull(message = "Ocorrencia e obrigatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_OCORRENCIA", nullable = false,
                foreignKey = @ForeignKey(name = "FK_ALE_OCORRENCIA"))
    private Ocorrencia ocorrencia;

    @NotBlank(message = "Nivel do alerta e obrigatorio")
    @Pattern(regexp = "ATENCAO|ALERTA|PERIGO|EMERGENCIA",
             message = "Nivel de alerta deve ser: ATENCAO, ALERTA, PERIGO ou EMERGENCIA")
    @Column(name = "NIVEL_ALERTA", nullable = false, length = 15)
    private String nivelAlerta;

    @NotNull(message = "Data de emissao e obrigatoria")
    @Column(name = "DATA_EMISSAO", nullable = false)
    private LocalDate dataEmissao;

    @NotBlank(message = "Orgao notificado e obrigatorio")
    @Size(max = 150, message = "Orgao notificado deve ter no maximo 150 caracteres")
    @Column(name = "ORGAO_NOTIFICADO", nullable = false, length = 150)
    private String orgaoNotificado;

    @NotBlank(message = "Descricao do alerta e obrigatoria")
    @Size(max = 500, message = "Descricao deve ter no maximo 500 caracteres")
    @Column(name = "DESCRICAO_ALERTA", nullable = false, length = 500)
    private String descricaoAlerta;

    @Pattern(regexp = "EMITIDO|ATENDIDO|CANCELADO",
             message = "Status deve ser: EMITIDO, ATENDIDO ou CANCELADO")
    @Column(name = "STATUS_ALERTA", length = 20)
    private String statusAlerta = "EMITIDO";

    // ── Construtores ──────────────────────────────────────────────────────

    public Alerta() {}

    // ── Getters e Setters ─────────────────────────────────────────────────

    public Long getIdAlerta() { return idAlerta; }
    public void setIdAlerta(Long idAlerta) { this.idAlerta = idAlerta; }

    public Ocorrencia getOcorrencia() { return ocorrencia; }
    public void setOcorrencia(Ocorrencia ocorrencia) { this.ocorrencia = ocorrencia; }

    public String getNivelAlerta() { return nivelAlerta; }
    public void setNivelAlerta(String nivelAlerta) { this.nivelAlerta = nivelAlerta; }

    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }

    public String getOrgaoNotificado() { return orgaoNotificado; }
    public void setOrgaoNotificado(String orgaoNotificado) { this.orgaoNotificado = orgaoNotificado; }

    public String getDescricaoAlerta() { return descricaoAlerta; }
    public void setDescricaoAlerta(String descricaoAlerta) { this.descricaoAlerta = descricaoAlerta; }

    public String getStatusAlerta() { return statusAlerta; }
    public void setStatusAlerta(String statusAlerta) { this.statusAlerta = statusAlerta; }
}
