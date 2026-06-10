package br.com.chronosgeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidade que representa a tabela GS_TIPO_EVENTO.
 * Catalogo de tipos de eventos ambientais monitorados.
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Entity
@Table(name = "GS_TIPO_EVENTO")
public class TipoEvento {

    @Id
    @Column(name = "ID_TIPO_EVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "seq_tipo_evento_gen")
    @SequenceGenerator(name = "seq_tipo_evento_gen",
                       sequenceName = "seq_tipo_evento",
                       allocationSize = 1)
    private Long idTipoEvento;

    @NotBlank(message = "Nome do evento e obrigatorio")
    @Size(max = 80, message = "Nome deve ter no maximo 80 caracteres")
    @Column(name = "NOME_EVENTO", nullable = false, unique = true, length = 80)
    private String nomeEvento;

    @NotBlank(message = "Categoria e obrigatoria")
    @Pattern(regexp = "INCENDIO|ENCHENTE|SECA|DESLIZAMENTO|DESMATAMENTO",
             message = "Categoria invalida. Use: INCENDIO, ENCHENTE, SECA, DESLIZAMENTO ou DESMATAMENTO")
    @Column(name = "CATEGORIA", nullable = false, length = 20)
    private String categoria;

    @NotBlank(message = "Nivel de perigo e obrigatorio")
    @Pattern(regexp = "BAIXO|MEDIO|ALTO|CRITICO",
             message = "Nivel de perigo deve ser: BAIXO, MEDIO, ALTO ou CRITICO")
    @Column(name = "NIVEL_PERIGO", nullable = false, length = 10)
    private String nivelPerigo;

    @Size(max = 400, message = "Descricao deve ter no maximo 400 caracteres")
    @Column(name = "DESCRICAO", length = 400)
    private String descricao;

    // ── Construtores ──────────────────────────────────────────────────────

    public TipoEvento() {}

    // ── Getters e Setters ─────────────────────────────────────────────────

    public Long getIdTipoEvento() { return idTipoEvento; }
    public void setIdTipoEvento(Long idTipoEvento) { this.idTipoEvento = idTipoEvento; }

    public String getNomeEvento() { return nomeEvento; }
    public void setNomeEvento(String nomeEvento) { this.nomeEvento = nomeEvento; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNivelPerigo() { return nivelPerigo; }
    public void setNivelPerigo(String nivelPerigo) { this.nivelPerigo = nivelPerigo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
