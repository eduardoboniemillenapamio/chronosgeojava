package br.com.chronosgeo.exception;

/**
 * Excecao de negocio do sistema ChronosGeo.
 * Lancada quando uma regra de negocio e violada
 * (ex: recurso nao encontrado, dados invalidos, duplicidade).
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
public class ChronosGeoException extends RuntimeException {

    private final int statusCode;

    public ChronosGeoException(String mensagem, int statusCode) {
        super(mensagem);
        this.statusCode = statusCode;
    }

    public ChronosGeoException(String mensagem) {
        super(mensagem);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
