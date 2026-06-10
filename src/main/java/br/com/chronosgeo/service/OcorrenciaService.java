package br.com.chronosgeo.service;

import br.com.chronosgeo.entity.Ocorrencia;
import br.com.chronosgeo.entity.Regiao;
import br.com.chronosgeo.entity.TipoEvento;
import br.com.chronosgeo.exception.ChronosGeoException;
import br.com.chronosgeo.repository.OcorrenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service (BO) de Ocorrencias — encapsula as regras de negocio.
 *
 * Responsabilidades:
 *  - Validar dados antes de persistir
 *  - Garantir consistencia dos relacionamentos
 *  - Aplicar regras de negocio (ex: encerramento so se ATIVA/MONITORANDO)
 *  - Delegar persistencia ao OcorrenciaRepository (padrao DAO)
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
public class OcorrenciaService {

    @Inject
    OcorrenciaRepository ocorrenciaRepository;

    // ── Listar ────────────────────────────────────────────────────────────

    /**
     * Retorna todas as ocorrencias cadastradas.
     *
     * @return Lista completa de ocorrencias
     */
    public List<Ocorrencia> listarTodas() {
        return ocorrenciaRepository.listAll();
    }

    /**
     * Retorna ocorrencias pelo status.
     *
     * @param status Status desejado
     * @return Lista filtrada por status
     */
    public List<Ocorrencia> listarPorStatus(String status) {
        validarStatusOcorrencia(status);
        return ocorrenciaRepository.findByStatus(status);
    }

    /**
     * Retorna ocorrencias pelo nivel de severidade.
     *
     * @param severidade Nivel de severidade
     * @return Lista filtrada por severidade
     */
    public List<Ocorrencia> listarPorSeveridade(String severidade) {
        validarSeveridade(severidade);
        return ocorrenciaRepository.findBySeveridade(severidade);
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────

    /**
     * Busca uma ocorrencia pelo ID.
     *
     * @param id ID da ocorrencia
     * @return Ocorrencia encontrada
     * @throws ChronosGeoException 404 se nao encontrada
     */
    public Ocorrencia buscarPorId(Long id) {
        return ocorrenciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new ChronosGeoException(
                        "Ocorrencia com ID " + id + " nao encontrada.", 404));
    }

    // ── Criar ─────────────────────────────────────────────────────────────

    /**
     * Cria uma nova ocorrencia apos validacoes de negocio.
     *
     * Regras:
     *  - Data de deteccao nao pode ser futura
     *  - Data de encerramento, se informada, deve ser >= data de deteccao
     *  - Regiao e TipoEvento devem ser fornecidos
     *
     * @param ocorrencia Dados da nova ocorrencia
     * @return Ocorrencia persistida com ID gerado
     */
    @Transactional
    public Ocorrencia criar(Ocorrencia ocorrencia) {
        validarOcorrencia(ocorrencia);

        if (ocorrencia.getStatusOcorrencia() == null
                || ocorrencia.getStatusOcorrencia().isBlank()) {
            ocorrencia.setStatusOcorrencia("ATIVA");
        }

        ocorrenciaRepository.persist(ocorrencia);
        return ocorrencia;
    }

    // ── Atualizar ─────────────────────────────────────────────────────────

    /**
     * Atualiza uma ocorrencia existente.
     *
     * @param id         ID da ocorrencia a atualizar
     * @param atualizada Dados novos
     * @return Ocorrencia atualizada
     * @throws ChronosGeoException 404 se nao encontrada
     */
    @Transactional
    public Ocorrencia atualizar(Long id, Ocorrencia atualizada) {
        Ocorrencia existente = buscarPorId(id);
        validarOcorrencia(atualizada);

        existente.setRegiao(atualizada.getRegiao());
        existente.setTipoEvento(atualizada.getTipoEvento());
        existente.setDataDeteccao(atualizada.getDataDeteccao());
        existente.setDataEncerramento(atualizada.getDataEncerramento());
        existente.setAreaAfetadaKm2(atualizada.getAreaAfetadaKm2());
        existente.setNivelSeveridade(atualizada.getNivelSeveridade());
        existente.setStatusOcorrencia(atualizada.getStatusOcorrencia());
        existente.setFonteDados(atualizada.getFonteDados());
        existente.setObservacoes(atualizada.getObservacoes());

        return existente;
    }

    // ── Deletar ───────────────────────────────────────────────────────────

    /**
     * Remove uma ocorrencia pelo ID.
     *
     * Regra: nao permite remover ocorrencias ATIVAS ou em MONITORAMENTO.
     *
     * @param id ID da ocorrencia
     * @throws ChronosGeoException 404 se nao encontrada
     * @throws ChronosGeoException 409 se ainda ativa/monitorando
     */
    @Transactional
    public void deletar(Long id) {
        Ocorrencia ocorrencia = buscarPorId(id);

        if ("ATIVA".equals(ocorrencia.getStatusOcorrencia())
                || "MONITORANDO".equals(ocorrencia.getStatusOcorrencia())) {
            throw new ChronosGeoException(
                    "Nao e possivel remover uma ocorrencia com status "
                    + ocorrencia.getStatusOcorrencia()
                    + ". Encerre-a antes de deletar.", 409);
        }

        ocorrenciaRepository.delete(ocorrencia);
    }

    // ── Validacoes privadas ───────────────────────────────────────────────

    private void validarOcorrencia(Ocorrencia o) {
        if (o.getRegiao() == null || o.getRegiao().getIdRegiao() == null) {
            throw new ChronosGeoException("ID da regiao e obrigatorio.");
        }
        if (o.getTipoEvento() == null || o.getTipoEvento().getIdTipoEvento() == null) {
            throw new ChronosGeoException("ID do tipo de evento e obrigatorio.");
        }
        if (o.getDataDeteccao() != null
                && o.getDataDeteccao().isAfter(LocalDate.now())) {
            throw new ChronosGeoException(
                    "Data de deteccao nao pode ser uma data futura.");
        }
        if (o.getDataDeteccao() != null
                && o.getDataEncerramento() != null
                && o.getDataEncerramento().isBefore(o.getDataDeteccao())) {
            throw new ChronosGeoException(
                    "Data de encerramento nao pode ser anterior a data de deteccao.");
        }
    }

    private void validarStatusOcorrencia(String status) {
        if (!List.of("ATIVA", "MONITORANDO", "ENCERRADA").contains(status)) {
            throw new ChronosGeoException(
                    "Status invalido: " + status
                    + ". Use: ATIVA, MONITORANDO ou ENCERRADA.");
        }
    }

    private void validarSeveridade(String sev) {
        if (!List.of("BAIXO", "MEDIO", "ALTO", "CRITICO").contains(sev)) {
            throw new ChronosGeoException(
                    "Severidade invalida: " + sev
                    + ". Use: BAIXO, MEDIO, ALTO ou CRITICO.");
        }
    }
}
