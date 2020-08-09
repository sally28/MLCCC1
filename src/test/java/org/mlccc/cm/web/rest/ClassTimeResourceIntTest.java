package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.ClassTime;
import org.mlccc.cm.repository.ClassTimeRepository;
import org.mlccc.cm.service.ClassTimeService;
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
 * Test class for the ClassTimeResource REST controller.
 *
 * @see ClassTimeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class ClassTimeResourceIntTest {

    private static final String DEFAULT_CLASS_TIME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_TIME = "BBBBBBBBBB";

    @Autowired
    private ClassTimeRepository classTimeRepository;

    @Autowired
    private ClassTimeService classTimeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClassTimeMockMvc;

    private ClassTime classTime;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassTimeResource classTimeResource = new ClassTimeResource(classTimeService);
        this.restClassTimeMockMvc = MockMvcBuilders.standaloneSetup(classTimeResource)
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
    public static ClassTime createEntity(EntityManager em) {
        ClassTime classTime = new ClassTime()
            .classTime(DEFAULT_CLASS_TIME);
        return classTime;
    }

    @Before
    public void initTest() {
        classTime = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassTime() throws Exception {
        int databaseSizeBeforeCreate = classTimeRepository.findAll().size();

        // Create the ClassTime
        restClassTimeMockMvc.perform(post("/api/class-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classTime)))
            .andExpect(status().isCreated());

        // Validate the ClassTime in the database
        List<ClassTime> classTimeList = classTimeRepository.findAll();
        assertThat(classTimeList).hasSize(databaseSizeBeforeCreate + 1);
        ClassTime testClassTime = classTimeList.get(classTimeList.size() - 1);
        assertThat(testClassTime.getClassTime()).isEqualTo(DEFAULT_CLASS_TIME);
    }

    @Test
    @Transactional
    public void createClassTimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classTimeRepository.findAll().size();

        // Create the ClassTime with an existing ID
        classTime.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassTimeMockMvc.perform(post("/api/class-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classTime)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ClassTime> classTimeList = classTimeRepository.findAll();
        assertThat(classTimeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClassTimes() throws Exception {
        // Initialize the database
        classTimeRepository.saveAndFlush(classTime);

        // Get all the classTimeList
        restClassTimeMockMvc.perform(get("/api/class-times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].classTime").value(hasItem(DEFAULT_CLASS_TIME.toString())));
    }

    @Test
    @Transactional
    public void getClassTime() throws Exception {
        // Initialize the database
        classTimeRepository.saveAndFlush(classTime);

        // Get the classTime
        restClassTimeMockMvc.perform(get("/api/class-times/{id}", classTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classTime.getId().intValue()))
            .andExpect(jsonPath("$.classTime").value(DEFAULT_CLASS_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClassTime() throws Exception {
        // Get the classTime
        restClassTimeMockMvc.perform(get("/api/class-times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassTime() throws Exception {
        // Initialize the database
        classTimeService.save(classTime);

        int databaseSizeBeforeUpdate = classTimeRepository.findAll().size();

        // Update the classTime
        ClassTime updatedClassTime = classTimeRepository.findOne(classTime.getId());
        updatedClassTime
            .classTime(UPDATED_CLASS_TIME);

        restClassTimeMockMvc.perform(put("/api/class-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassTime)))
            .andExpect(status().isOk());

        // Validate the ClassTime in the database
        List<ClassTime> classTimeList = classTimeRepository.findAll();
        assertThat(classTimeList).hasSize(databaseSizeBeforeUpdate);
        ClassTime testClassTime = classTimeList.get(classTimeList.size() - 1);
        assertThat(testClassTime.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingClassTime() throws Exception {
        int databaseSizeBeforeUpdate = classTimeRepository.findAll().size();

        // Create the ClassTime

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClassTimeMockMvc.perform(put("/api/class-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classTime)))
            .andExpect(status().isCreated());

        // Validate the ClassTime in the database
        List<ClassTime> classTimeList = classTimeRepository.findAll();
        assertThat(classTimeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClassTime() throws Exception {
        // Initialize the database
        classTimeService.save(classTime);

        int databaseSizeBeforeDelete = classTimeRepository.findAll().size();

        // Get the classTime
        restClassTimeMockMvc.perform(delete("/api/class-times/{id}", classTime.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClassTime> classTimeList = classTimeRepository.findAll();
        assertThat(classTimeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassTime.class);
        ClassTime classTime1 = new ClassTime();
        classTime1.setId(1L);
        ClassTime classTime2 = new ClassTime();
        classTime2.setId(classTime1.getId());
        assertThat(classTime1).isEqualTo(classTime2);
        classTime2.setId(2L);
        assertThat(classTime1).isNotEqualTo(classTime2);
        classTime1.setId(null);
        assertThat(classTime1).isNotEqualTo(classTime2);
    }
}
