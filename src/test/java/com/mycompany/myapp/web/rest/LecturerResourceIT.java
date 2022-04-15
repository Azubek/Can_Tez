package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Lecturer;
import com.mycompany.myapp.repository.LecturerRepository;
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
 * Integration tests for the {@link LecturerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LecturerResourceIT {

    private static final Integer DEFAULT_LECTURER_ID = 1;
    private static final Integer UPDATED_LECTURER_ID = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lecturers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LecturerRepository lecturerRepository;

    @Mock
    private LecturerRepository lecturerRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLecturerMockMvc;

    private Lecturer lecturer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lecturer createEntity(EntityManager em) {
        Lecturer lecturer = new Lecturer()
            .lecturerID(DEFAULT_LECTURER_ID)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .mobile(DEFAULT_MOBILE);
        return lecturer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lecturer createUpdatedEntity(EntityManager em) {
        Lecturer lecturer = new Lecturer()
            .lecturerID(UPDATED_LECTURER_ID)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .mobile(UPDATED_MOBILE);
        return lecturer;
    }

    @BeforeEach
    public void initTest() {
        lecturer = createEntity(em);
    }

    @Test
    @Transactional
    void createLecturer() throws Exception {
        int databaseSizeBeforeCreate = lecturerRepository.findAll().size();
        // Create the Lecturer
        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isCreated());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeCreate + 1);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getLecturerID()).isEqualTo(DEFAULT_LECTURER_ID);
        assertThat(testLecturer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLecturer.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testLecturer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLecturer.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testLecturer.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testLecturer.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void createLecturerWithExistingId() throws Exception {
        // Create the Lecturer with an existing ID
        lecturer.setId(1L);

        int databaseSizeBeforeCreate = lecturerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = lecturerRepository.findAll().size();
        // set the field null
        lecturer.setEmail(null);

        // Create the Lecturer, which fails.

        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = lecturerRepository.findAll().size();
        // set the field null
        lecturer.setPassword(null);

        // Create the Lecturer, which fails.

        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lecturerRepository.findAll().size();
        // set the field null
        lecturer.setName(null);

        // Create the Lecturer, which fails.

        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lecturerRepository.findAll().size();
        // set the field null
        lecturer.setSurname(null);

        // Create the Lecturer, which fails.

        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = lecturerRepository.findAll().size();
        // set the field null
        lecturer.setDateOfBirth(null);

        // Create the Lecturer, which fails.

        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLecturers() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        // Get all the lecturerList
        restLecturerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lecturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].lecturerID").value(hasItem(DEFAULT_LECTURER_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLecturersWithEagerRelationshipsIsEnabled() throws Exception {
        when(lecturerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLecturerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lecturerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLecturersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lecturerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLecturerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lecturerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLecturer() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        // Get the lecturer
        restLecturerMockMvc
            .perform(get(ENTITY_API_URL_ID, lecturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lecturer.getId().intValue()))
            .andExpect(jsonPath("$.lecturerID").value(DEFAULT_LECTURER_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE));
    }

    @Test
    @Transactional
    void getNonExistingLecturer() throws Exception {
        // Get the lecturer
        restLecturerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLecturer() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();

        // Update the lecturer
        Lecturer updatedLecturer = lecturerRepository.findById(lecturer.getId()).get();
        // Disconnect from session so that the updates on updatedLecturer are not directly saved in db
        em.detach(updatedLecturer);
        updatedLecturer
            .lecturerID(UPDATED_LECTURER_ID)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .mobile(UPDATED_MOBILE);

        restLecturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLecturer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLecturer))
            )
            .andExpect(status().isOk());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getLecturerID()).isEqualTo(UPDATED_LECTURER_ID);
        assertThat(testLecturer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLecturer.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testLecturer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLecturer.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testLecturer.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testLecturer.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void putNonExistingLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lecturer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLecturerWithPatch() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();

        // Update the lecturer using partial update
        Lecturer partialUpdatedLecturer = new Lecturer();
        partialUpdatedLecturer.setId(lecturer.getId());

        partialUpdatedLecturer.email(UPDATED_EMAIL).password(UPDATED_PASSWORD).dateOfBirth(UPDATED_DATE_OF_BIRTH);

        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLecturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLecturer))
            )
            .andExpect(status().isOk());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getLecturerID()).isEqualTo(DEFAULT_LECTURER_ID);
        assertThat(testLecturer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLecturer.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testLecturer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLecturer.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testLecturer.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testLecturer.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void fullUpdateLecturerWithPatch() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();

        // Update the lecturer using partial update
        Lecturer partialUpdatedLecturer = new Lecturer();
        partialUpdatedLecturer.setId(lecturer.getId());

        partialUpdatedLecturer
            .lecturerID(UPDATED_LECTURER_ID)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .mobile(UPDATED_MOBILE);

        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLecturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLecturer))
            )
            .andExpect(status().isOk());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getLecturerID()).isEqualTo(UPDATED_LECTURER_ID);
        assertThat(testLecturer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLecturer.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testLecturer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLecturer.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testLecturer.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testLecturer.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void patchNonExistingLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lecturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLecturer() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeDelete = lecturerRepository.findAll().size();

        // Delete the lecturer
        restLecturerMockMvc
            .perform(delete(ENTITY_API_URL_ID, lecturer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
