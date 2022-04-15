package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ExamResult;
import com.mycompany.myapp.repository.ExamResultRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExamResult}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExamResultResource {

    private final Logger log = LoggerFactory.getLogger(ExamResultResource.class);

    private static final String ENTITY_NAME = "examResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamResultRepository examResultRepository;

    public ExamResultResource(ExamResultRepository examResultRepository) {
        this.examResultRepository = examResultRepository;
    }

    /**
     * {@code POST  /exam-results} : Create a new examResult.
     *
     * @param examResult the examResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examResult, or with status {@code 400 (Bad Request)} if the examResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-results")
    public ResponseEntity<ExamResult> createExamResult(@Valid @RequestBody ExamResult examResult) throws URISyntaxException {
        log.debug("REST request to save ExamResult : {}", examResult);
        if (examResult.getId() != null) {
            throw new BadRequestAlertException("A new examResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamResult result = examResultRepository.save(examResult);
        return ResponseEntity
            .created(new URI("/api/exam-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-results/:id} : Updates an existing examResult.
     *
     * @param id the id of the examResult to save.
     * @param examResult the examResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examResult,
     * or with status {@code 400 (Bad Request)} if the examResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-results/{id}")
    public ResponseEntity<ExamResult> updateExamResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamResult examResult
    ) throws URISyntaxException {
        log.debug("REST request to update ExamResult : {}, {}", id, examResult);
        if (examResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamResult result = examResultRepository.save(examResult);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examResult.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-results/:id} : Partial updates given fields of an existing examResult, field will ignore if it is null
     *
     * @param id the id of the examResult to save.
     * @param examResult the examResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examResult,
     * or with status {@code 400 (Bad Request)} if the examResult is not valid,
     * or with status {@code 404 (Not Found)} if the examResult is not found,
     * or with status {@code 500 (Internal Server Error)} if the examResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-results/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamResult> partialUpdateExamResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamResult examResult
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamResult partially : {}, {}", id, examResult);
        if (examResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamResult> result = examResultRepository
            .findById(examResult.getId())
            .map(existingExamResult -> {
                if (examResult.getExamResultID() != null) {
                    existingExamResult.setExamResultID(examResult.getExamResultID());
                }
                if (examResult.getFinalResult() != null) {
                    existingExamResult.setFinalResult(examResult.getFinalResult());
                }
                if (examResult.getFinalStatus() != null) {
                    existingExamResult.setFinalStatus(examResult.getFinalStatus());
                }

                return existingExamResult;
            })
            .map(examResultRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examResult.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-results} : get all the examResults.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examResults in body.
     */
    @GetMapping("/exam-results")
    public List<ExamResult> getAllExamResults() {
        log.debug("REST request to get all ExamResults");
        return examResultRepository.findAll();
    }

    /**
     * {@code GET  /exam-results/:id} : get the "id" examResult.
     *
     * @param id the id of the examResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-results/{id}")
    public ResponseEntity<ExamResult> getExamResult(@PathVariable Long id) {
        log.debug("REST request to get ExamResult : {}", id);
        Optional<ExamResult> examResult = examResultRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(examResult);
    }

    /**
     * {@code DELETE  /exam-results/:id} : delete the "id" examResult.
     *
     * @param id the id of the examResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-results/{id}")
    public ResponseEntity<Void> deleteExamResult(@PathVariable Long id) {
        log.debug("REST request to delete ExamResult : {}", id);
        examResultRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
