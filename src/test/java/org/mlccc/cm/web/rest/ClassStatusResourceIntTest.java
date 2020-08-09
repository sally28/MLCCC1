package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.ClassStatus;
import org.mlccc.cm.repository.ClassStatusRepository;
import org.mlccc.cm.service.ClassStatusService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClassStatusResource REST controller.
 *
 * @see ClassStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class ClassStatusResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR_DISPLAYED = "AAAAAAAAAA";
    private static final String UPDATED_COLOR_DISPLAYED = "BBBBBBBBBB";

    @Autowired
    private ClassStatusRepository classStatusRepository;

    @Autowired
    private ClassStatusService classStatusService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClassStatusMockMvc;

    private ClassStatus classStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassStatusResource classStatusResource = new ClassStatusResource(classStatusService);
        this.restClassStatusMockMvc = MockMvcBuilders.standaloneSetup(classStatusResource)
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
    public static ClassStatus createEntity(EntityManager em) {
        ClassStatus classStatus = new ClassStatus()
            .status(DEFAULT_STATUS)
            .colorDisplayed(DEFAULT_COLOR_DISPLAYED);
        return classStatus;
    }

    @Before
    public void initTest() {
        classStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassStatus() throws Exception {
        int databaseSizeBeforeCreate = classStatusRepository.findAll().size();

        // Create the ClassStatus
        restClassStatusMockMvc.perform(post("/api/class-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classStatus)))
            .andExpect(status().isCreated());

        // Validate the ClassStatus in the database
        List<ClassStatus> classStatusList = classStatusRepository.findAll();
        assertThat(classStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ClassStatus testClassStatus = classStatusList.get(classStatusList.size() - 1);
        assertThat(testClassStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testClassStatus.getColorDisplayed()).isEqualTo(DEFAULT_COLOR_DISPLAYED);
    }

    @Test
    @Transactional
    public void createClassStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classStatusRepository.findAll().size();

        // Create the ClassStatus with an existing ID
        classStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassStatusMockMvc.perform(post("/api/class-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classStatus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ClassStatus> classStatusList = classStatusRepository.findAll();
        assertThat(classStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClassStatuses() throws Exception {
        // Initialize the database
        classStatusRepository.saveAndFlush(classStatus);

        // Get all the classStatusList
        restClassStatusMockMvc.perform(get("/api/class-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].colorDisplayed").value(hasItem(DEFAULT_COLOR_DISPLAYED.toString())));
    }

    @Test
    @Transactional
    public void getClassStatus() throws Exception {
        // Initialize the database
        classStatusRepository.saveAndFlush(classStatus);

        // Get the classStatus
        restClassStatusMockMvc.perform(get("/api/class-statuses/{id}", classStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.colorDisplayed").value(DEFAULT_COLOR_DISPLAYED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClassStatus() throws Exception {
        // Get the classStatus
        restClassStatusMockMvc.perform(get("/api/class-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassStatus() throws Exception {
        // Initialize the database
        classStatusService.save(classStatus);

        int databaseSizeBeforeUpdate = classStatusRepository.findAll().size();

        // Update the classStatus
        ClassStatus updatedClassStatus = classStatusRepository.findOne(classStatus.getId());
        updatedClassStatus
            .status(UPDATED_STATUS)
            .colorDisplayed(UPDATED_COLOR_DISPLAYED);

        restClassStatusMockMvc.perform(put("/api/class-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassStatus)))
            .andExpect(status().isOk());

        // Validate the ClassStatus in the database
        List<ClassStatus> classStatusList = classStatusRepository.findAll();
        assertThat(classStatusList).hasSize(databaseSizeBeforeUpdate);
        ClassStatus testClassStatus = classStatusList.get(classStatusList.size() - 1);
        assertThat(testClassStatus.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testClassStatus.getColorDisplayed()).isEqualTo(UPDATED_COLOR_DISPLAYED);
    }

    @Test
    @Transactional
    public void updateNonExistingClassStatus() throws Exception {
        int databaseSizeBeforeUpdate = classStatusRepository.findAll().size();

        // Create the ClassStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClassStatusMockMvc.perform(put("/api/class-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classStatus)))
            .andExpect(status().isCreated());

        // Validate the ClassStatus in the database
        List<ClassStatus> classStatusList = classStatusRepository.findAll();
        assertThat(classStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClassStatus() throws Exception {
        // Initialize the database
        classStatusService.save(classStatus);

        int databaseSizeBeforeDelete = classStatusRepository.findAll().size();

        // Get the classStatus
        restClassStatusMockMvc.perform(delete("/api/class-statuses/{id}", classStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClassStatus> classStatusList = classStatusRepository.findAll();
        assertThat(classStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassStatus.class);
        ClassStatus classStatus1 = new ClassStatus();
        classStatus1.setId(1L);
        ClassStatus classStatus2 = new ClassStatus();
        classStatus2.setId(classStatus1.getId());
        assertThat(classStatus1).isEqualTo(classStatus2);
        classStatus2.setId(2L);
        assertThat(classStatus1).isNotEqualTo(classStatus2);
        classStatus1.setId(null);
        assertThat(classStatus1).isNotEqualTo(classStatus2);
    }
}
