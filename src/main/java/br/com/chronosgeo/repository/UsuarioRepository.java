package br.com.chronosgeo.repository;

import br.com.chronosgeo.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio (DAO) para a entidade Usuario.
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    /**
     * Busca um usuario pelo email (campo UNIQUE no banco).
     *
     * @param email Email do usuario
     * @return Optional contendo o usuario, ou vazio se nao encontrado
     */
    public Optional<Usuario> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    /**
     * Busca usuarios pelo perfil.
     *
     * @param perfil Perfil (ANALISTA, GESTOR, FISCAL, ADMIN)
     * @return Lista de usuarios com o perfil informado
     */
    public List<Usuario> findByPerfil(String perfil) {
        return list("perfil", perfil);
    }

    /**
     * Busca apenas usuarios ativos.
     *
     * @return Lista de usuarios com ativo = 'S'
     */
    public List<Usuario> findAtivos() {
        return list("ativo", "S");
    }

    /**
     * Verifica se ja existe um usuario com o email informado.
     *
     * @param email Email a verificar
     * @return true se o email ja esta cadastrado
     */
    public boolean existeEmail(String email) {
        return count("email", email) > 0;
    }
}
