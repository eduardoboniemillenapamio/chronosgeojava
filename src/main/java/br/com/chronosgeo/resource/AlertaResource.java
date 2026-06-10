package br.com.chronosgeo.resource;

import br.com.chronosgeo.entity.Alerta;
import br.com.chronosgeo.service.AlertaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * Resource REST para Alertas.
 *
 * Endpoints:
 *  GET    /api/alertas                       lista todos
 *  GET    /api/alertas?nivel=EMERGENCIA      filtra por nivel
 *  GET    /api/alertas?idOcorrencia=1        filtra por ocorrencia
 *  GET    /api/alertas/{id}                  busca por ID
 *  POST   /api/alertas                       emite novo alerta
 *  PUT    /api/alertas/{id}                  atualiza alerta
 *  DELETE /api/alertas/{id}                  remove alerta
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
@Path("/api/alertas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertaResource {

    @Inject
    AlertaService alertaService;

    /**
     * GET /api/alertas
     * Lista todos os alertas ou filtra por nivel ou ocorrencia.
     *
     * @param nivel        (opcional) ATENCAO | ALERTA | PERIGO | EMERGENCIA
     * @param idOcorrencia (opcional) ID da ocorrencia vinculada
     * @return 200 OK com lista de alertas
     */
    @GET
    public Response listar(
            @QueryParam("nivel")        String nivel,
            @QueryParam("idOcorrencia") Long idOcorrencia) {

        List<Alerta> resultado;

        if (nivel != null && !nivel.isBlank()) {
            resultado = alertaService.listarPorNivel(nivel.toUpperCase());
        } else if (idOcorrencia != null) {
            resultado = alertaService.listarPorOcorrencia(idOcorrencia);
        } else {
            resultado = alertaService.listarTodos();
        }

        return Response.ok(resultado).build();
    }

    /**
     * GET /api/alertas/{id}
     * Busca um alerta pelo ID.
     *
     * @param id ID do alerta
     * @return 200 OK | 404 se nao encontrado
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(alertaService.buscarPorId(id)).build();
    }

    /**
     * POST /api/alertas
     * Emite um novo alerta vinculado a uma ocorrencia ativa.
     *
     * @param alerta JSON com dados do alerta
     * @return 201 Created | 409 se ocorrencia encerrada | 422 em erros de validacao
     */
    @POST
    public Response criar(@Valid Alerta alerta) {
        Alerta criado = alertaService.criar(alerta);
        return Response
                .created(UriBuilder.fromPath("/api/alertas/{id}")
                        .build(criado.getIdAlerta()))
                .entity(criado)
                .build();
    }

    /**
     * PUT /api/alertas/{id}
     * Atualiza nivel, orgao, descricao e status de um alerta.
     *
     * @param id     ID do alerta
     * @param alerta JSON com novos dados
     * @return 200 OK | 404 se nao encontrado
     */
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id,
                              @Valid Alerta alerta) {
        return Response.ok(alertaService.atualizar(id, alerta)).build();
    }

    /**
     * DELETE /api/alertas/{id}
     * Remove um alerta.
     *
     * @param id ID do alerta
     * @return 204 No Content | 404 se nao encontrado
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        alertaService.deletar(id);
        return Response.noContent().build();
    }
}
