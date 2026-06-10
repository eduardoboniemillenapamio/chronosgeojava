package br.com.chronosgeo.repository;

import br.com.chronosgeo.entity.Ocorrencia;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Repositorio (DAO) para a entidade Ocorrencia.
 * Estende PanacheRepository que fornece CRUD basico automaticamente.
 * Metodos customizados de consulta sao adicionados aqui.
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
public class OcorrenciaRepository implements PanacheRepository<Ocorrencia> {

    /**
     * Busca ocorrencias pelo status informado.
     *
     * @param status Status desejado (ATIVA, MONITORANDO, ENCERRADA)
     * @return Lista de ocorrencias com o status informado
     */
    public List<Ocorrencia> findByStatus(String status) {
        return list("statusOcorrencia", status);
    }

    /**
     * Busca ocorrencias pelo ID da regiao.
     *
     * @param idRegiao ID da regiao
     * @return Lista de ocorrencias da regiao
     */
    public List<Ocorrencia> findByRegiao(Long idRegiao) {
        return list("regiao.idRegiao", idRegiao);
    }

    /**
     * Busca ocorrencias pelo nivel de severidade.
     *
     * @param severidade Nivel de severidade (BAIXO, MEDIO, ALTO, CRITICO)
     * @return Lista de ocorrencias com a severidade informada
     */
    public List<Ocorrencia> findBySeveridade(String severidade) {
        return list("nivelSeveridade", severidade);
    }
}
