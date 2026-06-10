package br.com.chronosgeo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidade que representa a tabela GS_USUARIO.
 * Usuarios do sistema: analistas, gestores, fiscais e admins.
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Entity
@Table(name = "GS_USUARIO")
public class Usuario {

    @Id
    @Column(name = "ID_USUARIO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "seq_usuario_gen")
    @SequenceGenerator(name = "seq_usuario_gen",
                       sequenceName = "seq_usuario",
                       allocationSize = 1)
    private Long idUsuario;

    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email deve ter formato valido")
    @Size(max = 150, message = "Email deve ter no maximo 150 caracteres")
    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Perfil e obrigatorio")
    @Pattern(regexp = "ANALISTA|GESTOR|FISCAL|ADMIN",
             message = "Perfil deve ser: ANALISTA, GESTOR, FISCAL ou ADMIN")
    @Column(name = "PERFIL", nullable = false, length = 20)
    private String perfil;

    @NotBlank(message = "Orgao e obrigatorio")
    @Size(max = 100, message = "Orgao deve ter no maximo 100 caracteres")
    @Column(name = "ORGAO", nullable = false, length = 100)
    private String orgao;

    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDate dataCadastro;

    @Pattern(regexp = "S|N", message = "Ativo deve ser S ou N")
    @Column(name = "ATIVO", length = 1)
    private String ativo = "S";

    // ── Lifecycle ─────────────────────────────────────────────────────────

    @PrePersist
    public void prePersist() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDate.now();
        }
        if (this.ativo == null) {
            this.ativo = "S";
        }
    }

    // ── Construtores ──────────────────────────────────────────────────────

    public Usuario() {}

    // ── Getters e Setters ─────────────────────────────────────────────────

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public String getOrgao() { return orgao; }
    public void setOrgao(String orgao) { this.orgao = orgao; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getAtivo() { return ativo; }
    public void setAtivo(String ativo) { this.ativo = ativo; }
}
