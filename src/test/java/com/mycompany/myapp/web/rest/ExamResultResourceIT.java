package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExamResult;
import com.mycompany.myapp.repository.ExamResultRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExamResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamResultResourceIT {

    private static final Integer DEFAULT_EXAM_RESULT_ID = 1;
    private static final Integer UPDATED_EXAM_RESULT_ID = 2;

    private static final Float DEFAULT_FINAL_RESULT = 1F;
    private static final Float UPDATED_FINAL_RESULT = 2F;

    private static final String DEFAULT_FINAL_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exam-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamResultMockMvc;

    private ExamResult examResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamResult createEntity(EntityManager em) {
        ExamResult examResult = new ExamResult()
            .examResultID(DEFAULT_EXAM_RESULT_ID)
            .finalResult(DEFAULT_FINAL_RESULT)
            .finalStatus(DEFAULT_FINAL_STATUS);
        return examResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamResult createUpdatedEntity(EntityManager em) {
        ExamResult examResult = new ExamResult()
            .examResultID(UPDATED_EXAM_RESULT_ID)
            .finalResult(UPDATED_FINAL_RESULT)
            .finalStatus(UPDATED_FINAL_STATUS);
        return examResult;
    }

    @BeforeEach
    public void initTest() {
        examResult = createEntity(em);
    }

    @Test
    @Transactional
    void createExamResult() throws Exception {
        int databaseSizeBeforeCreate = examResultRepository.findAll().size();
        // Create the ExamResult
        restExamResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examResult)))
            .andExpect(status().isCreated());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeCreate + 1);
        ExamResult testExamResult = examResultList.get(examResultList.size() - 1);
        assertThat(testExamResult.getExamResultID()).isEqualTo(DEFAULT_EXAM_RESULT_ID);
        assertThat(testExamResult.getFinalResult()).isEqualTo(DEFAULT_FINAL_RESULT);
        assertThat(testExamResult.getFinalStatus()).isEqualTo(DEFAULT_FINAL_STATUS);
    }

    @Test
    @Transactional
    void createExamResultWithExistingId() throws Exception {
        // Create the ExamResult with an existing ID
        examResult.setId(1L);

        int databaseSizeBeforeCreate = examResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examResult)))
            .andExpect(status().isBadRequest());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFinalResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = examResultRepository.findAll().size();
        // set the field null
        examResult.setFinalResult(null);

        // Create the ExamResult, which fails.

        restExamResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examResult)))
            .andExpect(status().isBadRequest());

        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamResults() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList
        restExamResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].examResultID").value(hasItem(DEFAULT_EXAM_RESULT_ID)))
            .andExpect(jsonPath("$.[*].finalResult").value(hasItem(DEFAULT_FINAL_RESULT.doubleValue())))
            .andExpect(jsonPath("$.[*].finalStatus").value(hasItem(DEFAULT_FINAL_STATUS)));
    }

    @Test
    @Transactional
    void getExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get the examResult
        restExamResultMockMvc
            .perform(get(ENTITY_API_URL_ID, examResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examResult.getId().intValue()))
            .andExpect(jsonPath("$.examResultID").value(DEFAULT_EXAM_RESULT_ID))
            .andExpect(jsonPath("$.finalResult").value(DEFAULT_FINAL_RESULT.doubleValue()))
            .andExpect(jsonPath("$.finalStatus").value(DEFAULT_FINAL_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingExamResult() throws Exception {
        // Get the examResult
        restExamResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();

        // Update the examResult
        ExamResult updatedExamResult = examResultRepository.findById(examResult.getId()).get();
        // Disconnect from session so that the updates on updatedExamResult are not directly saved in db
        em.detach(updatedExamResult);
        updatedExamResult.examResultID(UPDATED_EXAM_RESULT_ID).finalResult(UPDATED_FINAL_RESULT).finalStatus(UPDATED_FINAL_STATUS);

        restExamResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExamResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExamResult))
            )
            .andExpect(status().isOk());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
        ExamResult testExamResult = examResultList.get(examResultList.size() - 1);
        assertThat(testExamResult.getExamResultID()).isEqualTo(UPDATED_EXAM_RESULT_ID);
        assertThat(testExamResult.getFinalResult()).isEqualTo(UPDATED_FINAL_RESULT);
        assertThat(testExamResult.getFinalStatus()).isEqualTo(UPDATED_FINAL_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingExamResult() throws Exception {
        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();
        examResult.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamResult() throws Exception {
        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();
        examResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamResult() throws Exception {
        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();
        examResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamResultWithPatch() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();

        // Update the examResult using partial update
        ExamResult partialUpdatedExamResult = new ExamResult();
        partialUpdatedExamResult.setId(examResult.getId());

        partialUpdatedExamResult.examResultID(UPDATED_EXAM_RESULT_ID);

        restExamResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamResult))
            )
            .andExpect(status().isOk());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
        ExamResult testExamResult = examResultList.get(examResultList.size() - 1);
        assertThat(testExamResult.getExamResultID()).isEqualTo(UPDATED_EXAM_RESULT_ID);
        assertThat(testExamResult.getFinalResult()).isEqualTo(DEFAULT_FINAL_RESULT);
        assertThat(testExamResult.getFinalStatus()).isEqualTo(DEFAULT_FINAL_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateExamResultWithPatch() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();

        // Update the examResult using partial update
        ExamResult partialUpdatedExamResult = new ExamResult();
        partialUpdatedExamResult.setId(examResult.getId());

        partialUpdatedExamResult.examResultID(UPDATED_EXAM_RESULT_ID).finalResult(UPDATED_FINAL_RESULT).finalStatus(UPDATED_FINAL_STATUS);

        restExamResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamResult))
            )
            .andExpect(status().isOk());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
        ExamResult testExamResult = examResultList.get(examResultList.size() - 1);
        assertThat(testExamResult.getExamResultID()).isEqualTo(UPDATED_EXAM_RESULT_ID);
        assertThat(testExamResult.getFinalResult()).isEqualTo(UPDATED_FINAL_RESULT);
        assertThat(testExamResult.getFinalStatus()).isEqualTo(UPDATED_FINAL_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingExamResult() throws Exception {
        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();
        examResult.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamResult() throws Exception {
        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();
        examResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamResult() throws Exception {
        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();
        examResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(examResult))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        int databaseSizeBeforeDelete = examResultRepository.findAll().size();

        // Delete the examResult
        restExamResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, examResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
