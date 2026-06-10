package br.com.chronosgeo.repository;

import br.com.chronosgeo.entity.Alerta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Repositorio (DAO) para a entidade Alerta.
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
public class AlertaRepository implements PanacheRepository<Alerta> {

    /**
     * Busca alertas pelo nivel informado.
     *
     * @param nivel Nivel do alerta (ATENCAO, ALERTA, PERIGO, EMERGENCIA)
     * @return Lista de alertas com o nivel informado
     */
    public List<Alerta> findByNivel(String nivel) {
        return list("nivelAlerta", nivel);
    }

    /**
     * Busca alertas vinculados a uma ocorrencia especifica.
     *
     * @param idOcorrencia ID da ocorrencia
     * @return Lista de alertas da ocorrencia
     */
    public List<Alerta> findByOcorrencia(Long idOcorrencia) {
        return list("ocorrencia.idOcorrencia", idOcorrencia);
    }

    /**
     * Busca alertas pelo status.
     *
     * @param status Status do alerta (EMITIDO, ATENDIDO, CANCELADO)
     * @return Lista de alertas com o status informado
     */
    public List<Alerta> findByStatus(String status) {
        return list("statusAlerta", status);
    }
}
