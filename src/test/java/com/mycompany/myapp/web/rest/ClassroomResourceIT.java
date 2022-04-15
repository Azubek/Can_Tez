package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Classroom;
import com.mycompany.myapp.repository.ClassroomRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClassroomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClassroomResourceIT {

    private static final Integer DEFAULT_CLASSROOM_ID = 1;
    private static final Integer UPDATED_CLASSROOM_ID = 2;

    private static final LocalDate DEFAULT_YEAR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_YEAR = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_SECTION = false;
    private static final Boolean UPDATED_SECTION = true;

    private static final String ENTITY_API_URL = "/api/classrooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassroomRepository classroomRepository;

    @Mock
    private ClassroomRepository classroomRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassroomMockMvc;

    private Classroom classroom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createEntity(EntityManager em) {
        Classroom classroom = new Classroom().classroomID(DEFAULT_CLASSROOM_ID).year(DEFAULT_YEAR).section(DEFAULT_SECTION);
        return classroom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createUpdatedEntity(EntityManager em) {
        Classroom classroom = new Classroom().classroomID(UPDATED_CLASSROOM_ID).year(UPDATED_YEAR).section(UPDATED_SECTION);
        return classroom;
    }

    @BeforeEach
    public void initTest() {
        classroom = createEntity(em);
    }

    @Test
    @Transactional
    void createClassroom() throws Exception {
        int databaseSizeBeforeCreate = classroomRepository.findAll().size();
        // Create the Classroom
        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isCreated());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeCreate + 1);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getClassroomID()).isEqualTo(DEFAULT_CLASSROOM_ID);
        assertThat(testClassroom.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testClassroom.getSection()).isEqualTo(DEFAULT_SECTION);
    }

    @Test
    @Transactional
    void createClassroomWithExistingId() throws Exception {
        // Create the Classroom with an existing ID
        classroom.setId(1L);

        int databaseSizeBeforeCreate = classroomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = classroomRepository.findAll().size();
        // set the field null
        classroom.setYear(null);

        // Create the Classroom, which fails.

        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isBadRequest());

        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = classroomRepository.findAll().size();
        // set the field null
        classroom.setSection(null);

        // Create the Classroom, which fails.

        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isBadRequest());

        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassrooms() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].classroomID").value(hasItem(DEFAULT_CLASSROOM_ID)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassroomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(classroomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassroomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classroomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassroomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(classroomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassroomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classroomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get the classroom
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL_ID, classroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classroom.getId().intValue()))
            .andExpect(jsonPath("$.classroomID").value(DEFAULT_CLASSROOM_ID))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingClassroom() throws Exception {
        // Get the classroom
        restClassroomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom
        Classroom updatedClassroom = classroomRepository.findById(classroom.getId()).get();
        // Disconnect from session so that the updates on updatedClassroom are not directly saved in db
        em.detach(updatedClassroom);
        updatedClassroom.classroomID(UPDATED_CLASSROOM_ID).year(UPDATED_YEAR).section(UPDATED_SECTION);

        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassroom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getClassroomID()).isEqualTo(UPDATED_CLASSROOM_ID);
        assertThat(testClassroom.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testClassroom.getSection()).isEqualTo(UPDATED_SECTION);
    }

    @Test
    @Transactional
    void putNonExistingClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classroom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassroomWithPatch() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom using partial update
        Classroom partialUpdatedClassroom = new Classroom();
        partialUpdatedClassroom.setId(classroom.getId());

        partialUpdatedClassroom.classroomID(UPDATED_CLASSROOM_ID).year(UPDATED_YEAR).section(UPDATED_SECTION);

        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getClassroomID()).isEqualTo(UPDATED_CLASSROOM_ID);
        assertThat(testClassroom.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testClassroom.getSection()).isEqualTo(UPDATED_SECTION);
    }

    @Test
    @Transactional
    void fullUpdateClassroomWithPatch() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom using partial update
        Classroom partialUpdatedClassroom = new Classroom();
        partialUpdatedClassroom.setId(classroom.getId());

        partialUpdatedClassroom.classroomID(UPDATED_CLASSROOM_ID).year(UPDATED_YEAR).section(UPDATED_SECTION);

        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getClassroomID()).isEqualTo(UPDATED_CLASSROOM_ID);
        assertThat(testClassroom.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testClassroom.getSection()).isEqualTo(UPDATED_SECTION);
    }

    @Test
    @Transactional
    void patchNonExistingClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeDelete = classroomRepository.findAll().size();

        // Delete the classroom
        restClassroomMockMvc
            .perform(delete(ENTITY_API_URL_ID, classroom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
