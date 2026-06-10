package br.com.chronosgeo.resource;

import br.com.chronosgeo.entity.Usuario;
import br.com.chronosgeo.service.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * Resource REST para Usuarios do Sistema.
 *
 * Endpoints:
 *  GET    /api/usuarios                       lista todos
 *  GET    /api/usuarios/ativos                lista apenas ativos (ativo='S')
 *  GET    /api/usuarios?perfil=ANALISTA       filtra por perfil
 *  GET    /api/usuarios/{id}                  busca por ID
 *  POST   /api/usuarios                       cadastra novo usuario
 *  PUT    /api/usuarios/{id}                  atualiza usuario
 *  PATCH  /api/usuarios/{id}/desativar        soft delete (ativo='N')
 *  DELETE /api/usuarios/{id}                  exclusao fisica
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    /**
     * GET /api/usuarios
     * Lista todos os usuarios ou filtra por perfil.
     *
     * @param perfil (opcional) ANALISTA | GESTOR | FISCAL | ADMIN
     * @return 200 OK com lista de usuarios
     */
    @GET
    public Response listar(@QueryParam("perfil") String perfil) {
        List<Usuario> resultado;
        if (perfil != null && !perfil.isBlank()) {
            resultado = usuarioService.listarPorPerfil(perfil.toUpperCase());
        } else {
            resultado = usuarioService.listarTodos();
        }
        return Response.ok(resultado).build();
    }

    /**
     * GET /api/usuarios/ativos
     * Lista apenas usuarios com ativo = 'S'.
     *
     * @return 200 OK com lista de usuarios ativos
     */
    @GET
    @Path("/ativos")
    public Response listarAtivos() {
        return Response.ok(usuarioService.listarAtivos()).build();
    }

    /**
     * GET /api/usuarios/{id}
     * Busca um usuario pelo ID.
     *
     * @param id ID do usuario
     * @return 200 OK | 404 se nao encontrado
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(usuarioService.buscarPorId(id)).build();
    }

    /**
     * POST /api/usuarios
     * Cadastra um novo usuario validando unicidade do email.
     *
     * @param usuario JSON com dados do usuario
     * @return 201 Created | 409 se email duplicado | 422 em erros de validacao
     */
    @POST
    public Response criar(@Valid Usuario usuario) {
        Usuario criado = usuarioService.criar(usuario);
        return Response
                .created(UriBuilder.fromPath("/api/usuarios/{id}")
                        .build(criado.getIdUsuario()))
                .entity(criado)
                .build();
    }

    /**
     * PUT /api/usuarios/{id}
     * Atualiza nome, email, perfil e orgao de um usuario.
     *
     * @param id      ID do usuario
     * @param usuario JSON com novos dados
     * @return 200 OK | 404 se nao encontrado | 409 se email duplicado
     */
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id,
                              @Valid Usuario usuario) {
        return Response.ok(usuarioService.atualizar(id, usuario)).build();
    }

    /**
     * PATCH /api/usuarios/{id}/desativar
     * Desativa o usuario (ativo='N') sem remover do banco.
     * Preserva historico de auditoria conforme campo ativo da GS_USUARIO.
     *
     * @param id ID do usuario
     * @return 200 OK com usuario desativado | 404/409 em erros
     */
    @PATCH
    @Path("/{id}/desativar")
    public Response desativar(@PathParam("id") Long id) {
        return Response.ok(usuarioService.desativar(id)).build();
    }

    /**
     * DELETE /api/usuarios/{id}
     * Remove fisicamente um usuario do banco.
     *
     * @param id ID do usuario
     * @return 204 No Content | 404 se nao encontrado
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        usuarioService.deletar(id);
        return Response.noContent().build();
    }
}
