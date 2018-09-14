package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.PhoneType;
import org.mlccc.cm.repository.PhoneTypeRepository;
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
 * Test class for the PhoneTypeResource REST controller.
 *
 * @see PhoneTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class PhoneTypeResourceIntTest {

    private static final String DEFAULT_PHONE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_TYPE = "BBBBBBBBBB";

    @Autowired
    private PhoneTypeRepository phoneTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPhoneTypeMockMvc;

    private PhoneType phoneType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhoneTypeResource phoneTypeResource = new PhoneTypeResource(phoneTypeRepository);
        this.restPhoneTypeMockMvc = MockMvcBuilders.standaloneSetup(phoneTypeResource)
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
    public static PhoneType createEntity(EntityManager em) {
        PhoneType phoneType = new PhoneType()
            .phoneType(DEFAULT_PHONE_TYPE);
        return phoneType;
    }

    @Before
    public void initTest() {
        phoneType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhoneType() throws Exception {
        int databaseSizeBeforeCreate = phoneTypeRepository.findAll().size();

        // Create the PhoneType
        restPhoneTypeMockMvc.perform(post("/api/phone-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phoneType)))
            .andExpect(status().isCreated());

        // Validate the PhoneType in the database
        List<PhoneType> phoneTypeList = phoneTypeRepository.findAll();
        assertThat(phoneTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PhoneType testPhoneType = phoneTypeList.get(phoneTypeList.size() - 1);
        assertThat(testPhoneType.getPhoneType()).isEqualTo(DEFAULT_PHONE_TYPE);
    }

    @Test
    @Transactional
    public void createPhoneTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = phoneTypeRepository.findAll().size();

        // Create the PhoneType with an existing ID
        phoneType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneTypeMockMvc.perform(post("/api/phone-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phoneType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PhoneType> phoneTypeList = phoneTypeRepository.findAll();
        assertThat(phoneTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPhoneTypes() throws Exception {
        // Initialize the database
        phoneTypeRepository.saveAndFlush(phoneType);

        // Get all the phoneTypeList
        restPhoneTypeMockMvc.perform(get("/api/phone-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phoneType.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneType").value(hasItem(DEFAULT_PHONE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPhoneType() throws Exception {
        // Initialize the database
        phoneTypeRepository.saveAndFlush(phoneType);

        // Get the phoneType
        restPhoneTypeMockMvc.perform(get("/api/phone-types/{id}", phoneType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(phoneType.getId().intValue()))
            .andExpect(jsonPath("$.phoneType").value(DEFAULT_PHONE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhoneType() throws Exception {
        // Get the phoneType
        restPhoneTypeMockMvc.perform(get("/api/phone-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhoneType() throws Exception {
        // Initialize the database
        phoneTypeRepository.saveAndFlush(phoneType);
        int databaseSizeBeforeUpdate = phoneTypeRepository.findAll().size();

        // Update the phoneType
        PhoneType updatedPhoneType = phoneTypeRepository.findOne(phoneType.getId());
        updatedPhoneType
            .phoneType(UPDATED_PHONE_TYPE);

        restPhoneTypeMockMvc.perform(put("/api/phone-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPhoneType)))
            .andExpect(status().isOk());

        // Validate the PhoneType in the database
        List<PhoneType> phoneTypeList = phoneTypeRepository.findAll();
        assertThat(phoneTypeList).hasSize(databaseSizeBeforeUpdate);
        PhoneType testPhoneType = phoneTypeList.get(phoneTypeList.size() - 1);
        assertThat(testPhoneType.getPhoneType()).isEqualTo(UPDATED_PHONE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPhoneType() throws Exception {
        int databaseSizeBeforeUpdate = phoneTypeRepository.findAll().size();

        // Create the PhoneType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPhoneTypeMockMvc.perform(put("/api/phone-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phoneType)))
            .andExpect(status().isCreated());

        // Validate the PhoneType in the database
        List<PhoneType> phoneTypeList = phoneTypeRepository.findAll();
        assertThat(phoneTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePhoneType() throws Exception {
        // Initialize the database
        phoneTypeRepository.saveAndFlush(phoneType);
        int databaseSizeBeforeDelete = phoneTypeRepository.findAll().size();

        // Get the phoneType
        restPhoneTypeMockMvc.perform(delete("/api/phone-types/{id}", phoneType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PhoneType> phoneTypeList = phoneTypeRepository.findAll();
        assertThat(phoneTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneType.class);
        PhoneType phoneType1 = new PhoneType();
        phoneType1.setId(1L);
        PhoneType phoneType2 = new PhoneType();
        phoneType2.setId(phoneType1.getId());
        assertThat(phoneType1).isEqualTo(phoneType2);
        phoneType2.setId(2L);
        assertThat(phoneType1).isNotEqualTo(phoneType2);
        phoneType1.setId(null);
        assertThat(phoneType1).isNotEqualTo(phoneType2);
    }
}
