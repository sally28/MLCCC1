package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.MlcAccount;
import org.mlccc.cm.repository.MlcAccountRepository;
import org.mlccc.cm.service.MlcAccountService;
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
 * Test class for the MlcAccountResource REST controller.
 *
 * @see MlcAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class MlcAccountResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MlcAccountRepository mlcAccountRepository;

    @Autowired
    private MlcAccountService mlcAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMlcAccountMockMvc;

    private MlcAccount mlcAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MlcAccountResource mlcAccountResource = new MlcAccountResource(mlcAccountService);
        this.restMlcAccountMockMvc = MockMvcBuilders.standaloneSetup(mlcAccountResource)
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
    public static MlcAccount createEntity(EntityManager em) {
        MlcAccount mlcAccount = new MlcAccount()
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .createDate(DEFAULT_CREATE_DATE);
        return mlcAccount;
    }

    @Before
    public void initTest() {
        mlcAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createMlcAccount() throws Exception {
        int databaseSizeBeforeCreate = mlcAccountRepository.findAll().size();

        // Create the MlcAccount
        restMlcAccountMockMvc.perform(post("/api/mlc-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mlcAccount)))
            .andExpect(status().isCreated());

        // Validate the MlcAccount in the database
        List<MlcAccount> mlcAccountList = mlcAccountRepository.findAll();
        assertThat(mlcAccountList).hasSize(databaseSizeBeforeCreate + 1);
        MlcAccount testMlcAccount = mlcAccountList.get(mlcAccountList.size() - 1);
        assertThat(testMlcAccount.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMlcAccount.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testMlcAccount.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createMlcAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mlcAccountRepository.findAll().size();

        // Create the MlcAccount with an existing ID
        mlcAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMlcAccountMockMvc.perform(post("/api/mlc-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mlcAccount)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MlcAccount> mlcAccountList = mlcAccountRepository.findAll();
        assertThat(mlcAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMlcAccounts() throws Exception {
        // Initialize the database
        mlcAccountRepository.saveAndFlush(mlcAccount);

        // Get all the mlcAccountList
        restMlcAccountMockMvc.perform(get("/api/mlc-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mlcAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getMlcAccount() throws Exception {
        // Initialize the database
        mlcAccountRepository.saveAndFlush(mlcAccount);

        // Get the mlcAccount
        restMlcAccountMockMvc.perform(get("/api/mlc-accounts/{id}", mlcAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mlcAccount.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMlcAccount() throws Exception {
        // Get the mlcAccount
        restMlcAccountMockMvc.perform(get("/api/mlc-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMlcAccount() throws Exception {
        // Initialize the database
        mlcAccountService.save(mlcAccount);

        int databaseSizeBeforeUpdate = mlcAccountRepository.findAll().size();

        // Update the mlcAccount
        MlcAccount updatedMlcAccount = mlcAccountRepository.findOne(mlcAccount.getId());
        updatedMlcAccount
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createDate(UPDATED_CREATE_DATE);

        restMlcAccountMockMvc.perform(put("/api/mlc-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMlcAccount)))
            .andExpect(status().isOk());

        // Validate the MlcAccount in the database
        List<MlcAccount> mlcAccountList = mlcAccountRepository.findAll();
        assertThat(mlcAccountList).hasSize(databaseSizeBeforeUpdate);
        MlcAccount testMlcAccount = mlcAccountList.get(mlcAccountList.size() - 1);
        assertThat(testMlcAccount.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMlcAccount.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testMlcAccount.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMlcAccount() throws Exception {
        int databaseSizeBeforeUpdate = mlcAccountRepository.findAll().size();

        // Create the MlcAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMlcAccountMockMvc.perform(put("/api/mlc-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mlcAccount)))
            .andExpect(status().isCreated());

        // Validate the MlcAccount in the database
        List<MlcAccount> mlcAccountList = mlcAccountRepository.findAll();
        assertThat(mlcAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMlcAccount() throws Exception {
        // Initialize the database
        mlcAccountService.save(mlcAccount);

        int databaseSizeBeforeDelete = mlcAccountRepository.findAll().size();

        // Get the mlcAccount
        restMlcAccountMockMvc.perform(delete("/api/mlc-accounts/{id}", mlcAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MlcAccount> mlcAccountList = mlcAccountRepository.findAll();
        assertThat(mlcAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MlcAccount.class);
        MlcAccount mlcAccount1 = new MlcAccount();
        mlcAccount1.setId(1L);
        MlcAccount mlcAccount2 = new MlcAccount();
        mlcAccount2.setId(mlcAccount1.getId());
        assertThat(mlcAccount1).isEqualTo(mlcAccount2);
        mlcAccount2.setId(2L);
        assertThat(mlcAccount1).isNotEqualTo(mlcAccount2);
        mlcAccount1.setId(null);
        assertThat(mlcAccount1).isNotEqualTo(mlcAccount2);
    }
}
