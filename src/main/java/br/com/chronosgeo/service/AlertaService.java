package br.com.chronosgeo.service;

import br.com.chronosgeo.entity.Alerta;
import br.com.chronosgeo.entity.Ocorrencia;
import br.com.chronosgeo.exception.ChronosGeoException;
import br.com.chronosgeo.repository.AlertaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service (BO) de Alertas — regras de negocio.
 *
 * Regras principais:
 *  - So e possivel emitir alerta para ocorrencia ATIVA ou MONITORANDO
 *  - Nivel do alerta deve corresponder ao nivel de severidade da ocorrencia
 *    (nao obrigatorio, mas validado como aviso)
 *  - Data de emissao padrao: hoje
 *
 * @author Millena dos Santos Pamio | RM 566664 | 1TDSPS
 */
@ApplicationScoped
public class AlertaService {

    @Inject
    AlertaRepository alertaRepository;

    @Inject
    OcorrenciaService ocorrenciaService;

    // ── Listar ────────────────────────────────────────────────────────────

    public List<Alerta> listarTodos() {
        return alertaRepository.listAll();
    }

    public List<Alerta> listarPorNivel(String nivel) {
        validarNivel(nivel);
        return alertaRepository.findByNivel(nivel);
    }

    public List<Alerta> listarPorOcorrencia(Long idOcorrencia) {
        // Valida que a ocorrencia existe antes de filtrar
        ocorrenciaService.buscarPorId(idOcorrencia);
        return alertaRepository.findByOcorrencia(idOcorrencia);
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────

    public Alerta buscarPorId(Long id) {
        return alertaRepository.findByIdOptional(id)
                .orElseThrow(() -> new ChronosGeoException(
                        "Alerta com ID " + id + " nao encontrado.", 404));
    }

    // ── Criar ─────────────────────────────────────────────────────────────

    /**
     * Emite um novo alerta vinculado a uma ocorrencia.
     *
     * Regra de negocio: so permite emitir alerta para ocorrencias
     * com status ATIVA ou MONITORANDO.
     *
     * @param alerta Dados do alerta a emitir
     * @return Alerta persistido
     */
    @Transactional
    public Alerta criar(Alerta alerta) {
        if (alerta.getOcorrencia() == null
                || alerta.getOcorrencia().getIdOcorrencia() == null) {
            throw new ChronosGeoException(
                    "ID da ocorrencia e obrigatorio para emitir um alerta.");
        }

        Ocorrencia ocorrencia = ocorrenciaService
                .buscarPorId(alerta.getOcorrencia().getIdOcorrencia());

        if ("ENCERRADA".equals(ocorrencia.getStatusOcorrencia())) {
            throw new ChronosGeoException(
                    "Nao e possivel emitir alerta para uma ocorrencia ja ENCERRADA.", 409);
        }

        alerta.setOcorrencia(ocorrencia);

        if (alerta.getDataEmissao() == null) {
            alerta.setDataEmissao(LocalDate.now());
        }
        if (alerta.getStatusAlerta() == null
                || alerta.getStatusAlerta().isBlank()) {
            alerta.setStatusAlerta("EMITIDO");
        }

        alertaRepository.persist(alerta);
        return alerta;
    }

    // ── Atualizar ─────────────────────────────────────────────────────────

    @Transactional
    public Alerta atualizar(Long id, Alerta atualizado) {
        Alerta existente = buscarPorId(id);

        existente.setNivelAlerta(atualizado.getNivelAlerta());
        existente.setOrgaoNotificado(atualizado.getOrgaoNotificado());
        existente.setDescricaoAlerta(atualizado.getDescricaoAlerta());
        existente.setStatusAlerta(atualizado.getStatusAlerta());

        return existente;
    }

    // ── Deletar ───────────────────────────────────────────────────────────

    @Transactional
    public void deletar(Long id) {
        Alerta alerta = buscarPorId(id);
        alertaRepository.delete(alerta);
    }

    // ── Validacao privada ─────────────────────────────────────────────────

    private void validarNivel(String nivel) {
        if (!List.of("ATENCAO", "ALERTA", "PERIGO", "EMERGENCIA").contains(nivel)) {
            throw new ChronosGeoException(
                    "Nivel invalido: " + nivel
                    + ". Use: ATENCAO, ALERTA, PERIGO ou EMERGENCIA.");
        }
    }
}
