package br.com.chronosgeo.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper global de excecoes — converte excecoes em respostas HTTP
 * padronizadas no formato JSON para o Front-End.
 *
 * Trata:
 *  - ChronosGeoException  → 400 / 404 / 409 (regra de negocio)
 *  - ConstraintViolation  → 422 (validacao Bean Validation)
 *  - Throwable generico   → 500 (erro interno inesperado)
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable excecao) {

        if (excecao instanceof ChronosGeoException cge) {
            return buildResponse(cge.getStatusCode(), cge.getMessage());
        }

        if (excecao instanceof ConstraintViolationException cve) {
            String mensagem = cve.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            return buildResponse(422, "Erro de validacao: " + mensagem);
        }

        // Erro interno generico
        return buildResponse(500,
                "Erro interno do servidor. Contate o suporte.");
    }

    /**
     * Constroi um corpo de resposta de erro padronizado em JSON.
     *
     * @param status   Codigo HTTP
     * @param mensagem Descricao do erro
     * @return Response HTTP com corpo JSON
     */
    private Response buildResponse(int status, String mensagem) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("status", status);
        corpo.put("erro", mensagem);
        corpo.put("timestamp", LocalDateTime.now().toString());

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(corpo)
                .build();
    }
}
