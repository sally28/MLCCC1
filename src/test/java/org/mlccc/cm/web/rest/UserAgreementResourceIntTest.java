package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.UserAgreement;
import org.mlccc.cm.repository.UserAgreementRepository;
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
 * Test class for the UserAgreementResource REST controller.
 *
 * @see UserAgreementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class UserAgreementResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private UserAgreementRepository userAgreementRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserAgreementMockMvc;

    private UserAgreement userAgreement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserAgreementResource userAgreementResource = new UserAgreementResource(userAgreementRepository);
        this.restUserAgreementMockMvc = MockMvcBuilders.standaloneSetup(userAgreementResource)
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
    public static UserAgreement createEntity(EntityManager em) {
        UserAgreement userAgreement = new UserAgreement()
            .name(DEFAULT_NAME);
        return userAgreement;
    }

    @Before
    public void initTest() {
        userAgreement = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserAgreement() throws Exception {
        int databaseSizeBeforeCreate = userAgreementRepository.findAll().size();

        // Create the UserAgreement
        restUserAgreementMockMvc.perform(post("/api/user-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAgreement)))
            .andExpect(status().isCreated());

        // Validate the UserAgreement in the database
        List<UserAgreement> userAgreementList = userAgreementRepository.findAll();
        assertThat(userAgreementList).hasSize(databaseSizeBeforeCreate + 1);
        UserAgreement testUserAgreement = userAgreementList.get(userAgreementList.size() - 1);
        assertThat(testUserAgreement.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createUserAgreementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userAgreementRepository.findAll().size();

        // Create the UserAgreement with an existing ID
        userAgreement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAgreementMockMvc.perform(post("/api/user-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAgreement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserAgreement> userAgreementList = userAgreementRepository.findAll();
        assertThat(userAgreementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserAgreements() throws Exception {
        // Initialize the database
        userAgreementRepository.saveAndFlush(userAgreement);

        // Get all the userAgreementList
        restUserAgreementMockMvc.perform(get("/api/user-agreements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAgreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUserAgreement() throws Exception {
        // Initialize the database
        userAgreementRepository.saveAndFlush(userAgreement);

        // Get the userAgreement
        restUserAgreementMockMvc.perform(get("/api/user-agreements/{id}", userAgreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userAgreement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserAgreement() throws Exception {
        // Get the userAgreement
        restUserAgreementMockMvc.perform(get("/api/user-agreements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserAgreement() throws Exception {
        // Initialize the database
        userAgreementRepository.saveAndFlush(userAgreement);
        int databaseSizeBeforeUpdate = userAgreementRepository.findAll().size();

        // Update the userAgreement
        UserAgreement updatedUserAgreement = userAgreementRepository.findOne(userAgreement.getId());
        updatedUserAgreement
            .name(UPDATED_NAME);

        restUserAgreementMockMvc.perform(put("/api/user-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserAgreement)))
            .andExpect(status().isOk());

        // Validate the UserAgreement in the database
        List<UserAgreement> userAgreementList = userAgreementRepository.findAll();
        assertThat(userAgreementList).hasSize(databaseSizeBeforeUpdate);
        UserAgreement testUserAgreement = userAgreementList.get(userAgreementList.size() - 1);
        assertThat(testUserAgreement.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingUserAgreement() throws Exception {
        int databaseSizeBeforeUpdate = userAgreementRepository.findAll().size();

        // Create the UserAgreement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserAgreementMockMvc.perform(put("/api/user-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAgreement)))
            .andExpect(status().isCreated());

        // Validate the UserAgreement in the database
        List<UserAgreement> userAgreementList = userAgreementRepository.findAll();
        assertThat(userAgreementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserAgreement() throws Exception {
        // Initialize the database
        userAgreementRepository.saveAndFlush(userAgreement);
        int databaseSizeBeforeDelete = userAgreementRepository.findAll().size();

        // Get the userAgreement
        restUserAgreementMockMvc.perform(delete("/api/user-agreements/{id}", userAgreement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserAgreement> userAgreementList = userAgreementRepository.findAll();
        assertThat(userAgreementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAgreement.class);
        UserAgreement userAgreement1 = new UserAgreement();
        userAgreement1.setId(1L);
        UserAgreement userAgreement2 = new UserAgreement();
        userAgreement2.setId(userAgreement1.getId());
        assertThat(userAgreement1).isEqualTo(userAgreement2);
        userAgreement2.setId(2L);
        assertThat(userAgreement1).isNotEqualTo(userAgreement2);
        userAgreement1.setId(null);
        assertThat(userAgreement1).isNotEqualTo(userAgreement2);
    }
}
