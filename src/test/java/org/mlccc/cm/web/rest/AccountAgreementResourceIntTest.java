package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.AccountAgreement;
import org.mlccc.cm.repository.AccountAgreementRepository;
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
 * Test class for the AccountAgreementResource REST controller.
 *
 * @see AccountAgreementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class AccountAgreementResourceIntTest {

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private AccountAgreementRepository accountAgreementRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountAgreementMockMvc;

    private AccountAgreement accountAgreement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountAgreementResource accountAgreementResource = new AccountAgreementResource(accountAgreementRepository);
        this.restAccountAgreementMockMvc = MockMvcBuilders.standaloneSetup(accountAgreementResource)
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
    public static AccountAgreement createEntity(EntityManager em) {
        AccountAgreement accountAgreement = new AccountAgreement()
            .createDate(DEFAULT_CREATE_DATE);
        return accountAgreement;
    }

    @Before
    public void initTest() {
        accountAgreement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountAgreement() throws Exception {
        int databaseSizeBeforeCreate = accountAgreementRepository.findAll().size();

        // Create the AccountAgreement
        restAccountAgreementMockMvc.perform(post("/api/account-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountAgreement)))
            .andExpect(status().isCreated());

        // Validate the AccountAgreement in the database
        List<AccountAgreement> accountAgreementList = accountAgreementRepository.findAll();
        assertThat(accountAgreementList).hasSize(databaseSizeBeforeCreate + 1);
        AccountAgreement testAccountAgreement = accountAgreementList.get(accountAgreementList.size() - 1);
        assertThat(testAccountAgreement.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createAccountAgreementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountAgreementRepository.findAll().size();

        // Create the AccountAgreement with an existing ID
        accountAgreement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountAgreementMockMvc.perform(post("/api/account-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountAgreement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountAgreement> accountAgreementList = accountAgreementRepository.findAll();
        assertThat(accountAgreementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountAgreements() throws Exception {
        // Initialize the database
        accountAgreementRepository.saveAndFlush(accountAgreement);

        // Get all the accountAgreementList
        restAccountAgreementMockMvc.perform(get("/api/account-agreements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountAgreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getAccountAgreement() throws Exception {
        // Initialize the database
        accountAgreementRepository.saveAndFlush(accountAgreement);

        // Get the accountAgreement
        restAccountAgreementMockMvc.perform(get("/api/account-agreements/{id}", accountAgreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountAgreement.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountAgreement() throws Exception {
        // Get the accountAgreement
        restAccountAgreementMockMvc.perform(get("/api/account-agreements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountAgreement() throws Exception {
        // Initialize the database
        accountAgreementRepository.saveAndFlush(accountAgreement);
        int databaseSizeBeforeUpdate = accountAgreementRepository.findAll().size();

        // Update the accountAgreement
        AccountAgreement updatedAccountAgreement = accountAgreementRepository.findOne(accountAgreement.getId());
        updatedAccountAgreement
            .createDate(UPDATED_CREATE_DATE);

        restAccountAgreementMockMvc.perform(put("/api/account-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountAgreement)))
            .andExpect(status().isOk());

        // Validate the AccountAgreement in the database
        List<AccountAgreement> accountAgreementList = accountAgreementRepository.findAll();
        assertThat(accountAgreementList).hasSize(databaseSizeBeforeUpdate);
        AccountAgreement testAccountAgreement = accountAgreementList.get(accountAgreementList.size() - 1);
        assertThat(testAccountAgreement.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountAgreement() throws Exception {
        int databaseSizeBeforeUpdate = accountAgreementRepository.findAll().size();

        // Create the AccountAgreement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountAgreementMockMvc.perform(put("/api/account-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountAgreement)))
            .andExpect(status().isCreated());

        // Validate the AccountAgreement in the database
        List<AccountAgreement> accountAgreementList = accountAgreementRepository.findAll();
        assertThat(accountAgreementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountAgreement() throws Exception {
        // Initialize the database
        accountAgreementRepository.saveAndFlush(accountAgreement);
        int databaseSizeBeforeDelete = accountAgreementRepository.findAll().size();

        // Get the accountAgreement
        restAccountAgreementMockMvc.perform(delete("/api/account-agreements/{id}", accountAgreement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountAgreement> accountAgreementList = accountAgreementRepository.findAll();
        assertThat(accountAgreementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountAgreement.class);
        AccountAgreement accountAgreement1 = new AccountAgreement();
        accountAgreement1.setId(1L);
        AccountAgreement accountAgreement2 = new AccountAgreement();
        accountAgreement2.setId(accountAgreement1.getId());
        assertThat(accountAgreement1).isEqualTo(accountAgreement2);
        accountAgreement2.setId(2L);
        assertThat(accountAgreement1).isNotEqualTo(accountAgreement2);
        accountAgreement1.setId(null);
        assertThat(accountAgreement1).isNotEqualTo(accountAgreement2);
    }
}
