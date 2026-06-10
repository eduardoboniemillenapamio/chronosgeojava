package br.com.chronosgeo.service;

import br.com.chronosgeo.entity.Usuario;
import br.com.chronosgeo.exception.ChronosGeoException;
import br.com.chronosgeo.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service (BO) de Usuarios — regras de negocio.
 *
 * Regras principais:
 *  - Email e unico (CHECK UNIQUE na tabela GS_USUARIO)
 *  - Desativacao e logica (campo ativo = 'N'), nao exclusao fisica
 *  - Perfis validos: ANALISTA, GESTOR, FISCAL, ADMIN
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    // ── Listar ────────────────────────────────────────────────────────────

    public List<Usuario> listarTodos() {
        return usuarioRepository.listAll();
    }

    public List<Usuario> listarAtivos() {
        return usuarioRepository.findAtivos();
    }

    public List<Usuario> listarPorPerfil(String perfil) {
        return usuarioRepository.findByPerfil(perfil);
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findByIdOptional(id)
                .orElseThrow(() -> new ChronosGeoException(
                        "Usuario com ID " + id + " nao encontrado.", 404));
    }

    // ── Criar ─────────────────────────────────────────────────────────────

    /**
     * Cadastra um novo usuario apos validar unicidade do email.
     *
     * @param usuario Dados do usuario
     * @return Usuario persistido com ID gerado
     * @throws ChronosGeoException 409 se email ja cadastrado
     */
    @Transactional
    public Usuario criar(Usuario usuario) {
        if (usuarioRepository.existeEmail(usuario.getEmail())) {
            throw new ChronosGeoException(
                    "Email '" + usuario.getEmail()
                    + "' ja esta cadastrado no sistema.", 409);
        }

        usuarioRepository.persist(usuario);
        return usuario;
    }

    // ── Atualizar ─────────────────────────────────────────────────────────

    /**
     * Atualiza dados do usuario. Email so pode ser alterado se o novo
     * email nao estiver em uso por outro usuario.
     *
     * @param id         ID do usuario
     * @param atualizado Novos dados
     * @return Usuario atualizado
     */
    @Transactional
    public Usuario atualizar(Long id, Usuario atualizado) {
        Usuario existente = buscarPorId(id);

        // Verifica duplicidade de email somente se email foi alterado
        if (!existente.getEmail().equalsIgnoreCase(atualizado.getEmail())
                && usuarioRepository.existeEmail(atualizado.getEmail())) {
            throw new ChronosGeoException(
                    "Email '" + atualizado.getEmail()
                    + "' ja esta em uso por outro usuario.", 409);
        }

        existente.setNome(atualizado.getNome());
        existente.setEmail(atualizado.getEmail());
        existente.setPerfil(atualizado.getPerfil());
        existente.setOrgao(atualizado.getOrgao());

        return existente;
    }

    // ── Desativar (soft delete) ───────────────────────────────────────────

    /**
     * Desativa um usuario (ativo = 'N') sem remover do banco.
     * Equivale ao soft delete compativel com a regra da tabela GS_USUARIO.
     *
     * @param id ID do usuario a desativar
     * @throws ChronosGeoException 404 se nao encontrado
     * @throws ChronosGeoException 409 se ja inativo
     */
    @Transactional
    public Usuario desativar(Long id) {
        Usuario usuario = buscarPorId(id);

        if ("N".equals(usuario.getAtivo())) {
            throw new ChronosGeoException(
                    "Usuario com ID " + id + " ja esta inativo.", 409);
        }

        usuario.setAtivo("N");
        return usuario;
    }

    // ── Deletar (fisico) ──────────────────────────────────────────────────

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
