package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.SchoolTerm;
import org.mlccc.cm.repository.MlcClassRepository;
import org.mlccc.cm.repository.SchoolTermRepository;
import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.service.SchoolTermService;
import org.mlccc.cm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SchoolTermResource REST controller.
 *
 * @see SchoolTermResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class SchoolTermResourceIntTest {

    private static final String DEFAULT_TERM = "AAAAAAAAAA";
    private static final String UPDATED_TERM = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REGISTER = false;
    private static final Boolean UPDATED_REGISTER = true;

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EARLY_BIRD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EARLY_BIRD_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SchoolTermRepository schoolTermRepository;

    @Autowired
    private MlcClassService mlcClassService;

    @Autowired
    private SchoolTermService schoolTermService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchoolTermMockMvc;

    private SchoolTerm schoolTerm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SchoolTermResource schoolTermResource = new SchoolTermResource(schoolTermService, mlcClassService);
        this.restSchoolTermMockMvc = MockMvcBuilders.standaloneSetup(schoolTermResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolTerm createEntity(EntityManager em) {
        SchoolTerm schoolTerm = new SchoolTerm()
            .term(DEFAULT_TERM)
            .status(DEFAULT_STATUS)
            .register(DEFAULT_REGISTER)
            .fromDate(DEFAULT_FROM_DATE)
            .earlyBirdDate(DEFAULT_EARLY_BIRD_DATE);
        return schoolTerm;
    }

    @Before
    public void initTest() {
        schoolTerm = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchoolTerm() throws Exception {
        int databaseSizeBeforeCreate = schoolTermRepository.findAll().size();

        // Create the SchoolTerm
        restSchoolTermMockMvc.perform(post("/api/school-terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolTerm)))
            .andExpect(status().isCreated());

        // Validate the SchoolTerm in the database
        List<SchoolTerm> schoolTermList = schoolTermRepository.findAll();
        assertThat(schoolTermList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolTerm testSchoolTerm = schoolTermList.get(schoolTermList.size() - 1);
        assertThat(testSchoolTerm.getTerm()).isEqualTo(DEFAULT_TERM);
        assertThat(testSchoolTerm.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSchoolTerm.isRegister()).isEqualTo(DEFAULT_REGISTER);
        assertThat(testSchoolTerm.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testSchoolTerm.getEarlyBirdDate()).isEqualTo(DEFAULT_EARLY_BIRD_DATE);
    }

    @Test
    @Transactional
    public void createSchoolTermWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schoolTermRepository.findAll().size();

        // Create the SchoolTerm with an existing ID
        schoolTerm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolTermMockMvc.perform(post("/api/school-terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolTerm)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SchoolTerm> schoolTermList = schoolTermRepository.findAll();
        assertThat(schoolTermList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSchoolTerms() throws Exception {
        // Initialize the database
        schoolTermRepository.saveAndFlush(schoolTerm);

        // Get all the schoolTermList
        restSchoolTermMockMvc.perform(get("/api/school-terms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolTerm.getId().intValue())))
            .andExpect(jsonPath("$.[*].term").value(hasItem(DEFAULT_TERM.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].register").value(hasItem(DEFAULT_REGISTER.booleanValue())))
            .andExpect(jsonPath("$.[*].promDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].earlyBirdDate").value(hasItem(DEFAULT_EARLY_BIRD_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSchoolTerm() throws Exception {
        // Initialize the database
        schoolTermRepository.saveAndFlush(schoolTerm);

        // Get the schoolTerm
        restSchoolTermMockMvc.perform(get("/api/school-terms/{id}", schoolTerm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schoolTerm.getId().intValue()))
            .andExpect(jsonPath("$.term").value(DEFAULT_TERM.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.register").value(DEFAULT_REGISTER.booleanValue()))
            .andExpect(jsonPath("$.promDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.earlyBirdDate").value(DEFAULT_EARLY_BIRD_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSchoolTerm() throws Exception {
        // Get the schoolTerm
        restSchoolTermMockMvc.perform(get("/api/school-terms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchoolTerm() throws Exception {
        // Initialize the database
        schoolTermService.save(schoolTerm);

        int databaseSizeBeforeUpdate = schoolTermRepository.findAll().size();

        // Update the schoolTerm
        SchoolTerm updatedSchoolTerm = schoolTermRepository.findOne(schoolTerm.getId());
        updatedSchoolTerm
            .term(UPDATED_TERM)
            .status(UPDATED_STATUS)
            .register(UPDATED_REGISTER)
            .fromDate(UPDATED_FROM_DATE)
            .earlyBirdDate(UPDATED_EARLY_BIRD_DATE);

        restSchoolTermMockMvc.perform(put("/api/school-terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchoolTerm)))
            .andExpect(status().isOk());

        // Validate the SchoolTerm in the database
        List<SchoolTerm> schoolTermList = schoolTermRepository.findAll();
        assertThat(schoolTermList).hasSize(databaseSizeBeforeUpdate);
        SchoolTerm testSchoolTerm = schoolTermList.get(schoolTermList.size() - 1);
        assertThat(testSchoolTerm.getTerm()).isEqualTo(UPDATED_TERM);
        assertThat(testSchoolTerm.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSchoolTerm.isRegister()).isEqualTo(UPDATED_REGISTER);
        assertThat(testSchoolTerm.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testSchoolTerm.getEarlyBirdDate()).isEqualTo(UPDATED_EARLY_BIRD_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSchoolTerm() throws Exception {
        int databaseSizeBeforeUpdate = schoolTermRepository.findAll().size();

        // Create the SchoolTerm

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchoolTermMockMvc.perform(put("/api/school-terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolTerm)))
            .andExpect(status().isCreated());

        // Validate the SchoolTerm in the database
        List<SchoolTerm> schoolTermList = schoolTermRepository.findAll();
        assertThat(schoolTermList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSchoolTerm() throws Exception {
        // Initialize the database
        schoolTermService.save(schoolTerm);

        int databaseSizeBeforeDelete = schoolTermRepository.findAll().size();

        // Get the schoolTerm
        restSchoolTermMockMvc.perform(delete("/api/school-terms/{id}", schoolTerm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SchoolTerm> schoolTermList = schoolTermRepository.findAll();
        assertThat(schoolTermList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolTerm.class);
        SchoolTerm schoolTerm1 = new SchoolTerm();
        schoolTerm1.setId(1L);
        SchoolTerm schoolTerm2 = new SchoolTerm();
        schoolTerm2.setId(schoolTerm1.getId());
        assertThat(schoolTerm1).isEqualTo(schoolTerm2);
        schoolTerm2.setId(2L);
        assertThat(schoolTerm1).isNotEqualTo(schoolTerm2);
        schoolTerm1.setId(null);
        assertThat(schoolTerm1).isNotEqualTo(schoolTerm2);
    }
}
