package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.SchoolDistrict;
import org.mlccc.cm.repository.SchoolDistrictRepository;
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
 * Test class for the SchoolDistrictResource REST controller.
 *
 * @see SchoolDistrictResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class SchoolDistrictResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY = "BBBBBBBBBB";

    @Autowired
    private SchoolDistrictRepository schoolDistrictRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchoolDistrictMockMvc;

    private SchoolDistrict schoolDistrict;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SchoolDistrictResource schoolDistrictResource = new SchoolDistrictResource(schoolDistrictRepository);
        this.restSchoolDistrictMockMvc = MockMvcBuilders.standaloneSetup(schoolDistrictResource)
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
    public static SchoolDistrict createEntity(EntityManager em) {
        SchoolDistrict schoolDistrict = new SchoolDistrict()
            .name(DEFAULT_NAME)
            .county(DEFAULT_COUNTY);
        return schoolDistrict;
    }

    @Before
    public void initTest() {
        schoolDistrict = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchoolDistrict() throws Exception {
        int databaseSizeBeforeCreate = schoolDistrictRepository.findAll().size();

        // Create the SchoolDistrict
        restSchoolDistrictMockMvc.perform(post("/api/school-districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDistrict)))
            .andExpect(status().isCreated());

        // Validate the SchoolDistrict in the database
        List<SchoolDistrict> schoolDistrictList = schoolDistrictRepository.findAll();
        assertThat(schoolDistrictList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolDistrict testSchoolDistrict = schoolDistrictList.get(schoolDistrictList.size() - 1);
        assertThat(testSchoolDistrict.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSchoolDistrict.getCounty()).isEqualTo(DEFAULT_COUNTY);
    }

    @Test
    @Transactional
    public void createSchoolDistrictWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schoolDistrictRepository.findAll().size();

        // Create the SchoolDistrict with an existing ID
        schoolDistrict.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolDistrictMockMvc.perform(post("/api/school-districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDistrict)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SchoolDistrict> schoolDistrictList = schoolDistrictRepository.findAll();
        assertThat(schoolDistrictList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSchoolDistricts() throws Exception {
        // Initialize the database
        schoolDistrictRepository.saveAndFlush(schoolDistrict);

        // Get all the schoolDistrictList
        restSchoolDistrictMockMvc.perform(get("/api/school-districts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolDistrict.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY.toString())));
    }

    @Test
    @Transactional
    public void getSchoolDistrict() throws Exception {
        // Initialize the database
        schoolDistrictRepository.saveAndFlush(schoolDistrict);

        // Get the schoolDistrict
        restSchoolDistrictMockMvc.perform(get("/api/school-districts/{id}", schoolDistrict.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schoolDistrict.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.county").value(DEFAULT_COUNTY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSchoolDistrict() throws Exception {
        // Get the schoolDistrict
        restSchoolDistrictMockMvc.perform(get("/api/school-districts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchoolDistrict() throws Exception {
        // Initialize the database
        schoolDistrictRepository.saveAndFlush(schoolDistrict);
        int databaseSizeBeforeUpdate = schoolDistrictRepository.findAll().size();

        // Update the schoolDistrict
        SchoolDistrict updatedSchoolDistrict = schoolDistrictRepository.findOne(schoolDistrict.getId());
        updatedSchoolDistrict
            .name(UPDATED_NAME)
            .county(UPDATED_COUNTY);

        restSchoolDistrictMockMvc.perform(put("/api/school-districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchoolDistrict)))
            .andExpect(status().isOk());

        // Validate the SchoolDistrict in the database
        List<SchoolDistrict> schoolDistrictList = schoolDistrictRepository.findAll();
        assertThat(schoolDistrictList).hasSize(databaseSizeBeforeUpdate);
        SchoolDistrict testSchoolDistrict = schoolDistrictList.get(schoolDistrictList.size() - 1);
        assertThat(testSchoolDistrict.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSchoolDistrict.getCounty()).isEqualTo(UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void updateNonExistingSchoolDistrict() throws Exception {
        int databaseSizeBeforeUpdate = schoolDistrictRepository.findAll().size();

        // Create the SchoolDistrict

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchoolDistrictMockMvc.perform(put("/api/school-districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolDistrict)))
            .andExpect(status().isCreated());

        // Validate the SchoolDistrict in the database
        List<SchoolDistrict> schoolDistrictList = schoolDistrictRepository.findAll();
        assertThat(schoolDistrictList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSchoolDistrict() throws Exception {
        // Initialize the database
        schoolDistrictRepository.saveAndFlush(schoolDistrict);
        int databaseSizeBeforeDelete = schoolDistrictRepository.findAll().size();

        // Get the schoolDistrict
        restSchoolDistrictMockMvc.perform(delete("/api/school-districts/{id}", schoolDistrict.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SchoolDistrict> schoolDistrictList = schoolDistrictRepository.findAll();
        assertThat(schoolDistrictList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolDistrict.class);
        SchoolDistrict schoolDistrict1 = new SchoolDistrict();
        schoolDistrict1.setId(1L);
        SchoolDistrict schoolDistrict2 = new SchoolDistrict();
        schoolDistrict2.setId(schoolDistrict1.getId());
        assertThat(schoolDistrict1).isEqualTo(schoolDistrict2);
        schoolDistrict2.setId(2L);
        assertThat(schoolDistrict1).isNotEqualTo(schoolDistrict2);
        schoolDistrict1.setId(null);
        assertThat(schoolDistrict1).isNotEqualTo(schoolDistrict2);
    }
}
