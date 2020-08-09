package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.RegistrationStatus;
import org.mlccc.cm.repository.RegistrationStatusRepository;
import org.mlccc.cm.service.RegistrationStatusService;
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
 * Test class for the RegistrationStatusResource REST controller.
 *
 * @see RegistrationStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class RegistrationStatusResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private RegistrationStatusRepository registrationStatusRepository;

    @Autowired
    private RegistrationStatusService registrationStatusService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistrationStatusMockMvc;

    private RegistrationStatus registrationStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegistrationStatusResource registrationStatusResource = new RegistrationStatusResource(registrationStatusService);
        this.restRegistrationStatusMockMvc = MockMvcBuilders.standaloneSetup(registrationStatusResource)
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
    public static RegistrationStatus createEntity(EntityManager em) {
        RegistrationStatus registrationStatus = new RegistrationStatus()
            .status(DEFAULT_STATUS);
        return registrationStatus;
    }

    @Before
    public void initTest() {
        registrationStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistrationStatus() throws Exception {
        int databaseSizeBeforeCreate = registrationStatusRepository.findAll().size();

        // Create the RegistrationStatus
        restRegistrationStatusMockMvc.perform(post("/api/registration-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registrationStatus)))
            .andExpect(status().isCreated());

        // Validate the RegistrationStatus in the database
        List<RegistrationStatus> registrationStatusList = registrationStatusRepository.findAll();
        assertThat(registrationStatusList).hasSize(databaseSizeBeforeCreate + 1);
        RegistrationStatus testRegistrationStatus = registrationStatusList.get(registrationStatusList.size() - 1);
        assertThat(testRegistrationStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRegistrationStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registrationStatusRepository.findAll().size();

        // Create the RegistrationStatus with an existing ID
        registrationStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistrationStatusMockMvc.perform(post("/api/registration-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registrationStatus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RegistrationStatus> registrationStatusList = registrationStatusRepository.findAll();
        assertThat(registrationStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegistrationStatuses() throws Exception {
        // Initialize the database
        registrationStatusRepository.saveAndFlush(registrationStatus);

        // Get all the registrationStatusList
        restRegistrationStatusMockMvc.perform(get("/api/registration-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registrationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getRegistrationStatus() throws Exception {
        // Initialize the database
        registrationStatusRepository.saveAndFlush(registrationStatus);

        // Get the registrationStatus
        restRegistrationStatusMockMvc.perform(get("/api/registration-statuses/{id}", registrationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registrationStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistrationStatus() throws Exception {
        // Get the registrationStatus
        restRegistrationStatusMockMvc.perform(get("/api/registration-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistrationStatus() throws Exception {
        // Initialize the database
        registrationStatusService.save(registrationStatus);

        int databaseSizeBeforeUpdate = registrationStatusRepository.findAll().size();

        // Update the registrationStatus
        RegistrationStatus updatedRegistrationStatus = registrationStatusRepository.findOne(registrationStatus.getId());
        updatedRegistrationStatus
            .status(UPDATED_STATUS);

        restRegistrationStatusMockMvc.perform(put("/api/registration-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistrationStatus)))
            .andExpect(status().isOk());

        // Validate the RegistrationStatus in the database
        List<RegistrationStatus> registrationStatusList = registrationStatusRepository.findAll();
        assertThat(registrationStatusList).hasSize(databaseSizeBeforeUpdate);
        RegistrationStatus testRegistrationStatus = registrationStatusList.get(registrationStatusList.size() - 1);
        assertThat(testRegistrationStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistrationStatus() throws Exception {
        int databaseSizeBeforeUpdate = registrationStatusRepository.findAll().size();

        // Create the RegistrationStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegistrationStatusMockMvc.perform(put("/api/registration-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registrationStatus)))
            .andExpect(status().isCreated());

        // Validate the RegistrationStatus in the database
        List<RegistrationStatus> registrationStatusList = registrationStatusRepository.findAll();
        assertThat(registrationStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegistrationStatus() throws Exception {
        // Initialize the database
        registrationStatusService.save(registrationStatus);

        int databaseSizeBeforeDelete = registrationStatusRepository.findAll().size();

        // Get the registrationStatus
        restRegistrationStatusMockMvc.perform(delete("/api/registration-statuses/{id}", registrationStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RegistrationStatus> registrationStatusList = registrationStatusRepository.findAll();
        assertThat(registrationStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistrationStatus.class);
        RegistrationStatus registrationStatus1 = new RegistrationStatus();
        registrationStatus1.setId(1L);
        RegistrationStatus registrationStatus2 = new RegistrationStatus();
        registrationStatus2.setId(registrationStatus1.getId());
        assertThat(registrationStatus1).isEqualTo(registrationStatus2);
        registrationStatus2.setId(2L);
        assertThat(registrationStatus1).isNotEqualTo(registrationStatus2);
        registrationStatus1.setId(null);
        assertThat(registrationStatus1).isNotEqualTo(registrationStatus2);
    }
}
