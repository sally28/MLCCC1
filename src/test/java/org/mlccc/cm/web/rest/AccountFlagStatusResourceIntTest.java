package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.AccountFlagStatus;
import org.mlccc.cm.repository.AccountFlagStatusRepository;
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
 * Test class for the AccountFlagStatusResource REST controller.
 *
 * @see AccountFlagStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class AccountFlagStatusResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AccountFlagStatusRepository accountFlagStatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountFlagStatusMockMvc;

    private AccountFlagStatus accountFlagStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountFlagStatusResource accountFlagStatusResource = new AccountFlagStatusResource(accountFlagStatusRepository);
        this.restAccountFlagStatusMockMvc = MockMvcBuilders.standaloneSetup(accountFlagStatusResource)
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
    public static AccountFlagStatus createEntity(EntityManager em) {
        AccountFlagStatus accountFlagStatus = new AccountFlagStatus()
            .description(DEFAULT_DESCRIPTION);
        return accountFlagStatus;
    }

    @Before
    public void initTest() {
        accountFlagStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountFlagStatus() throws Exception {
        int databaseSizeBeforeCreate = accountFlagStatusRepository.findAll().size();

        // Create the AccountFlagStatus
        restAccountFlagStatusMockMvc.perform(post("/api/account-flag-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountFlagStatus)))
            .andExpect(status().isCreated());

        // Validate the AccountFlagStatus in the database
        List<AccountFlagStatus> accountFlagStatusList = accountFlagStatusRepository.findAll();
        assertThat(accountFlagStatusList).hasSize(databaseSizeBeforeCreate + 1);
        AccountFlagStatus testAccountFlagStatus = accountFlagStatusList.get(accountFlagStatusList.size() - 1);
        assertThat(testAccountFlagStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAccountFlagStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountFlagStatusRepository.findAll().size();

        // Create the AccountFlagStatus with an existing ID
        accountFlagStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountFlagStatusMockMvc.perform(post("/api/account-flag-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountFlagStatus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountFlagStatus> accountFlagStatusList = accountFlagStatusRepository.findAll();
        assertThat(accountFlagStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountFlagStatuses() throws Exception {
        // Initialize the database
        accountFlagStatusRepository.saveAndFlush(accountFlagStatus);

        // Get all the accountFlagStatusList
        restAccountFlagStatusMockMvc.perform(get("/api/account-flag-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountFlagStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAccountFlagStatus() throws Exception {
        // Initialize the database
        accountFlagStatusRepository.saveAndFlush(accountFlagStatus);

        // Get the accountFlagStatus
        restAccountFlagStatusMockMvc.perform(get("/api/account-flag-statuses/{id}", accountFlagStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountFlagStatus.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountFlagStatus() throws Exception {
        // Get the accountFlagStatus
        restAccountFlagStatusMockMvc.perform(get("/api/account-flag-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountFlagStatus() throws Exception {
        // Initialize the database
        accountFlagStatusRepository.saveAndFlush(accountFlagStatus);
        int databaseSizeBeforeUpdate = accountFlagStatusRepository.findAll().size();

        // Update the accountFlagStatus
        AccountFlagStatus updatedAccountFlagStatus = accountFlagStatusRepository.findOne(accountFlagStatus.getId());
        updatedAccountFlagStatus
            .description(UPDATED_DESCRIPTION);

        restAccountFlagStatusMockMvc.perform(put("/api/account-flag-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountFlagStatus)))
            .andExpect(status().isOk());

        // Validate the AccountFlagStatus in the database
        List<AccountFlagStatus> accountFlagStatusList = accountFlagStatusRepository.findAll();
        assertThat(accountFlagStatusList).hasSize(databaseSizeBeforeUpdate);
        AccountFlagStatus testAccountFlagStatus = accountFlagStatusList.get(accountFlagStatusList.size() - 1);
        assertThat(testAccountFlagStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountFlagStatus() throws Exception {
        int databaseSizeBeforeUpdate = accountFlagStatusRepository.findAll().size();

        // Create the AccountFlagStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountFlagStatusMockMvc.perform(put("/api/account-flag-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountFlagStatus)))
            .andExpect(status().isCreated());

        // Validate the AccountFlagStatus in the database
        List<AccountFlagStatus> accountFlagStatusList = accountFlagStatusRepository.findAll();
        assertThat(accountFlagStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountFlagStatus() throws Exception {
        // Initialize the database
        accountFlagStatusRepository.saveAndFlush(accountFlagStatus);
        int databaseSizeBeforeDelete = accountFlagStatusRepository.findAll().size();

        // Get the accountFlagStatus
        restAccountFlagStatusMockMvc.perform(delete("/api/account-flag-statuses/{id}", accountFlagStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountFlagStatus> accountFlagStatusList = accountFlagStatusRepository.findAll();
        assertThat(accountFlagStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountFlagStatus.class);
        AccountFlagStatus accountFlagStatus1 = new AccountFlagStatus();
        accountFlagStatus1.setId(1L);
        AccountFlagStatus accountFlagStatus2 = new AccountFlagStatus();
        accountFlagStatus2.setId(accountFlagStatus1.getId());
        assertThat(accountFlagStatus1).isEqualTo(accountFlagStatus2);
        accountFlagStatus2.setId(2L);
        assertThat(accountFlagStatus1).isNotEqualTo(accountFlagStatus2);
        accountFlagStatus1.setId(null);
        assertThat(accountFlagStatus1).isNotEqualTo(accountFlagStatus2);
    }
}
