package br.com.chronosgeo.resource;

import br.com.chronosgeo.entity.Ocorrencia;
import br.com.chronosgeo.service.OcorrenciaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * Resource REST para Ocorrencias Ambientais.
 *
 * Endpoints:
 *  GET    /api/ocorrencias                    lista todas
 *  GET    /api/ocorrencias?status=ATIVA       filtra por status
 *  GET    /api/ocorrencias?severidade=ALTO    filtra por severidade
 *  GET    /api/ocorrencias/{id}               busca por ID
 *  POST   /api/ocorrencias                    cria nova
 *  PUT    /api/ocorrencias/{id}               atualiza
 *  DELETE /api/ocorrencias/{id}               remove
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
@Path("/api/ocorrencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OcorrenciaResource {

    @Inject
    OcorrenciaService ocorrenciaService;

    /**
     * GET /api/ocorrencias
     * Lista todas as ocorrencias ou filtra por status ou severidade.
     *
     * @param status     (opcional) ATIVA | MONITORANDO | ENCERRADA
     * @param severidade (opcional) BAIXO | MEDIO | ALTO | CRITICO
     * @return 200 OK com lista de ocorrencias
     */
    @GET
    public Response listar(
            @QueryParam("status")     String status,
            @QueryParam("severidade") String severidade) {

        List<Ocorrencia> resultado;

        if (status != null && !status.isBlank()) {
            resultado = ocorrenciaService.listarPorStatus(status.toUpperCase());
        } else if (severidade != null && !severidade.isBlank()) {
            resultado = ocorrenciaService.listarPorSeveridade(severidade.toUpperCase());
        } else {
            resultado = ocorrenciaService.listarTodas();
        }

        return Response.ok(resultado).build();
    }

    /**
     * GET /api/ocorrencias/{id}
     * Busca uma ocorrencia pelo ID.
     *
     * @param id ID da ocorrencia
     * @return 200 OK | 404 se nao encontrada
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(ocorrenciaService.buscarPorId(id)).build();
    }

    /**
     * POST /api/ocorrencias
     * Cria uma nova ocorrencia ambiental.
     *
     * @param ocorrencia JSON com dados da ocorrencia
     * @return 201 Created com Location | 400/422 em erros de validacao
     */
    @POST
    public Response criar(@Valid Ocorrencia ocorrencia) {
        Ocorrencia criada = ocorrenciaService.criar(ocorrencia);
        return Response
                .created(UriBuilder.fromPath("/api/ocorrencias/{id}")
                        .build(criada.getIdOcorrencia()))
                .entity(criada)
                .build();
    }

    /**
     * PUT /api/ocorrencias/{id}
     * Atualiza uma ocorrencia existente.
     *
     * @param id         ID da ocorrencia
     * @param ocorrencia JSON com novos dados
     * @return 200 OK | 404 se nao encontrada
     */
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id,
                              @Valid Ocorrencia ocorrencia) {
        return Response.ok(ocorrenciaService.atualizar(id, ocorrencia)).build();
    }

    /**
     * DELETE /api/ocorrencias/{id}
     * Remove uma ocorrencia encerrada.
     *
     * @param id ID da ocorrencia
     * @return 204 No Content | 404 se nao encontrada | 409 se ainda ativa
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        ocorrenciaService.deletar(id);
        return Response.noContent().build();
    }
}
