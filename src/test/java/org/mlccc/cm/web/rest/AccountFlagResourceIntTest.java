package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.AccountFlag;
import org.mlccc.cm.repository.AccountFlagRepository;
import org.mlccc.cm.service.AccountFlagService;
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
 * Test class for the AccountFlagResource REST controller.
 *
 * @see AccountFlagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class AccountFlagResourceIntTest {

    private static final String DEFAULT_FLAG_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FLAG_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RELATED_KEY = 1;
    private static final Integer UPDATED_RELATED_KEY = 2;

    @Autowired
    private AccountFlagRepository accountFlagRepository;

    @Autowired
    private AccountFlagService accountFlagService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountFlagMockMvc;

    private AccountFlag accountFlag;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountFlagResource accountFlagResource = new AccountFlagResource(accountFlagService);
        this.restAccountFlagMockMvc = MockMvcBuilders.standaloneSetup(accountFlagResource)
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
    public static AccountFlag createEntity(EntityManager em) {
        AccountFlag accountFlag = new AccountFlag()
            .flagType(DEFAULT_FLAG_TYPE)
            .relatedKey(DEFAULT_RELATED_KEY);
        return accountFlag;
    }

    @Before
    public void initTest() {
        accountFlag = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountFlag() throws Exception {
        int databaseSizeBeforeCreate = accountFlagRepository.findAll().size();

        // Create the AccountFlag
        restAccountFlagMockMvc.perform(post("/api/account-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountFlag)))
            .andExpect(status().isCreated());

        // Validate the AccountFlag in the database
        List<AccountFlag> accountFlagList = accountFlagRepository.findAll();
        assertThat(accountFlagList).hasSize(databaseSizeBeforeCreate + 1);
        AccountFlag testAccountFlag = accountFlagList.get(accountFlagList.size() - 1);
        assertThat(testAccountFlag.getFlagType()).isEqualTo(DEFAULT_FLAG_TYPE);
        assertThat(testAccountFlag.getRelatedKey()).isEqualTo(DEFAULT_RELATED_KEY);
    }

    @Test
    @Transactional
    public void createAccountFlagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountFlagRepository.findAll().size();

        // Create the AccountFlag with an existing ID
        accountFlag.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountFlagMockMvc.perform(post("/api/account-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountFlag)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountFlag> accountFlagList = accountFlagRepository.findAll();
        assertThat(accountFlagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountFlags() throws Exception {
        // Initialize the database
        accountFlagRepository.saveAndFlush(accountFlag);

        // Get all the accountFlagList
        restAccountFlagMockMvc.perform(get("/api/account-flags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].flagType").value(hasItem(DEFAULT_FLAG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].relatedKey").value(hasItem(DEFAULT_RELATED_KEY)));
    }

    @Test
    @Transactional
    public void getAccountFlag() throws Exception {
        // Initialize the database
        accountFlagRepository.saveAndFlush(accountFlag);

        // Get the accountFlag
        restAccountFlagMockMvc.perform(get("/api/account-flags/{id}", accountFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountFlag.getId().intValue()))
            .andExpect(jsonPath("$.flagType").value(DEFAULT_FLAG_TYPE.toString()))
            .andExpect(jsonPath("$.relatedKey").value(DEFAULT_RELATED_KEY));
    }

    @Test
    @Transactional
    public void getNonExistingAccountFlag() throws Exception {
        // Get the accountFlag
        restAccountFlagMockMvc.perform(get("/api/account-flags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountFlag() throws Exception {
        // Initialize the database
        accountFlagService.save(accountFlag);

        int databaseSizeBeforeUpdate = accountFlagRepository.findAll().size();

        // Update the accountFlag
        AccountFlag updatedAccountFlag = accountFlagRepository.findOne(accountFlag.getId());
        updatedAccountFlag
            .flagType(UPDATED_FLAG_TYPE)
            .relatedKey(UPDATED_RELATED_KEY);

        restAccountFlagMockMvc.perform(put("/api/account-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountFlag)))
            .andExpect(status().isOk());

        // Validate the AccountFlag in the database
        List<AccountFlag> accountFlagList = accountFlagRepository.findAll();
        assertThat(accountFlagList).hasSize(databaseSizeBeforeUpdate);
        AccountFlag testAccountFlag = accountFlagList.get(accountFlagList.size() - 1);
        assertThat(testAccountFlag.getFlagType()).isEqualTo(UPDATED_FLAG_TYPE);
        assertThat(testAccountFlag.getRelatedKey()).isEqualTo(UPDATED_RELATED_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountFlag() throws Exception {
        int databaseSizeBeforeUpdate = accountFlagRepository.findAll().size();

        // Create the AccountFlag

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountFlagMockMvc.perform(put("/api/account-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountFlag)))
            .andExpect(status().isCreated());

        // Validate the AccountFlag in the database
        List<AccountFlag> accountFlagList = accountFlagRepository.findAll();
        assertThat(accountFlagList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountFlag() throws Exception {
        // Initialize the database
        accountFlagService.save(accountFlag);

        int databaseSizeBeforeDelete = accountFlagRepository.findAll().size();

        // Get the accountFlag
        restAccountFlagMockMvc.perform(delete("/api/account-flags/{id}", accountFlag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountFlag> accountFlagList = accountFlagRepository.findAll();
        assertThat(accountFlagList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountFlag.class);
        AccountFlag accountFlag1 = new AccountFlag();
        accountFlag1.setId(1L);
        AccountFlag accountFlag2 = new AccountFlag();
        accountFlag2.setId(accountFlag1.getId());
        assertThat(accountFlag1).isEqualTo(accountFlag2);
        accountFlag2.setId(2L);
        assertThat(accountFlag1).isNotEqualTo(accountFlag2);
        accountFlag1.setId(null);
        assertThat(accountFlag1).isNotEqualTo(accountFlag2);
    }
}
