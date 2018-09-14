package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.AppliedDiscount;
import org.mlccc.cm.repository.AppliedDiscountRepository;
import org.mlccc.cm.service.AppliedDiscountService;
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
 * Test class for the AppliedDiscountResource REST controller.
 *
 * @see AppliedDiscountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class AppliedDiscountResourceIntTest {

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private AppliedDiscountRepository appliedDiscountRepository;

    @Autowired
    private AppliedDiscountService appliedDiscountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppliedDiscountMockMvc;

    private AppliedDiscount appliedDiscount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppliedDiscountResource appliedDiscountResource = new AppliedDiscountResource(appliedDiscountService);
        this.restAppliedDiscountMockMvc = MockMvcBuilders.standaloneSetup(appliedDiscountResource)
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
    public static AppliedDiscount createEntity(EntityManager em) {
        AppliedDiscount appliedDiscount = new AppliedDiscount()
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return appliedDiscount;
    }

    @Before
    public void initTest() {
        appliedDiscount = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppliedDiscount() throws Exception {
        int databaseSizeBeforeCreate = appliedDiscountRepository.findAll().size();

        // Create the AppliedDiscount
        restAppliedDiscountMockMvc.perform(post("/api/applied-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appliedDiscount)))
            .andExpect(status().isCreated());

        // Validate the AppliedDiscount in the database
        List<AppliedDiscount> appliedDiscountList = appliedDiscountRepository.findAll();
        assertThat(appliedDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        AppliedDiscount testAppliedDiscount = appliedDiscountList.get(appliedDiscountList.size() - 1);
        assertThat(testAppliedDiscount.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAppliedDiscount.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createAppliedDiscountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appliedDiscountRepository.findAll().size();

        // Create the AppliedDiscount with an existing ID
        appliedDiscount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppliedDiscountMockMvc.perform(post("/api/applied-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appliedDiscount)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AppliedDiscount> appliedDiscountList = appliedDiscountRepository.findAll();
        assertThat(appliedDiscountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppliedDiscounts() throws Exception {
        // Initialize the database
        appliedDiscountRepository.saveAndFlush(appliedDiscount);

        // Get all the appliedDiscountList
        restAppliedDiscountMockMvc.perform(get("/api/applied-discounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appliedDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getAppliedDiscount() throws Exception {
        // Initialize the database
        appliedDiscountRepository.saveAndFlush(appliedDiscount);

        // Get the appliedDiscount
        restAppliedDiscountMockMvc.perform(get("/api/applied-discounts/{id}", appliedDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appliedDiscount.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppliedDiscount() throws Exception {
        // Get the appliedDiscount
        restAppliedDiscountMockMvc.perform(get("/api/applied-discounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppliedDiscount() throws Exception {
        // Initialize the database
        appliedDiscountService.save(appliedDiscount);

        int databaseSizeBeforeUpdate = appliedDiscountRepository.findAll().size();

        // Update the appliedDiscount
        AppliedDiscount updatedAppliedDiscount = appliedDiscountRepository.findOne(appliedDiscount.getId());
        updatedAppliedDiscount
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restAppliedDiscountMockMvc.perform(put("/api/applied-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppliedDiscount)))
            .andExpect(status().isOk());

        // Validate the AppliedDiscount in the database
        List<AppliedDiscount> appliedDiscountList = appliedDiscountRepository.findAll();
        assertThat(appliedDiscountList).hasSize(databaseSizeBeforeUpdate);
        AppliedDiscount testAppliedDiscount = appliedDiscountList.get(appliedDiscountList.size() - 1);
        assertThat(testAppliedDiscount.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppliedDiscount.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppliedDiscount() throws Exception {
        int databaseSizeBeforeUpdate = appliedDiscountRepository.findAll().size();

        // Create the AppliedDiscount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppliedDiscountMockMvc.perform(put("/api/applied-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appliedDiscount)))
            .andExpect(status().isCreated());

        // Validate the AppliedDiscount in the database
        List<AppliedDiscount> appliedDiscountList = appliedDiscountRepository.findAll();
        assertThat(appliedDiscountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppliedDiscount() throws Exception {
        // Initialize the database
        appliedDiscountService.save(appliedDiscount);

        int databaseSizeBeforeDelete = appliedDiscountRepository.findAll().size();

        // Get the appliedDiscount
        restAppliedDiscountMockMvc.perform(delete("/api/applied-discounts/{id}", appliedDiscount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AppliedDiscount> appliedDiscountList = appliedDiscountRepository.findAll();
        assertThat(appliedDiscountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppliedDiscount.class);
        AppliedDiscount appliedDiscount1 = new AppliedDiscount();
        appliedDiscount1.setId(1L);
        AppliedDiscount appliedDiscount2 = new AppliedDiscount();
        appliedDiscount2.setId(appliedDiscount1.getId());
        assertThat(appliedDiscount1).isEqualTo(appliedDiscount2);
        appliedDiscount2.setId(2L);
        assertThat(appliedDiscount1).isNotEqualTo(appliedDiscount2);
        appliedDiscount1.setId(null);
        assertThat(appliedDiscount1).isNotEqualTo(appliedDiscount2);
    }
}
