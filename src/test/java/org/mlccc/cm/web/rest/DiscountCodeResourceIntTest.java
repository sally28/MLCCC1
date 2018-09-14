package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.DiscountCode;
import org.mlccc.cm.repository.DiscountCodeRepository;
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
 * Test class for the DiscountCodeResource REST controller.
 *
 * @see DiscountCodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class DiscountCodeResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDiscountCodeMockMvc;

    private DiscountCode discountCode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountCodeResource discountCodeResource = new DiscountCodeResource(discountCodeRepository);
        this.restDiscountCodeMockMvc = MockMvcBuilders.standaloneSetup(discountCodeResource)
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
    public static DiscountCode createEntity(EntityManager em) {
        DiscountCode discountCode = new DiscountCode()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION);
        return discountCode;
    }

    @Before
    public void initTest() {
        discountCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscountCode() throws Exception {
        int databaseSizeBeforeCreate = discountCodeRepository.findAll().size();

        // Create the DiscountCode
        restDiscountCodeMockMvc.perform(post("/api/discount-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountCode)))
            .andExpect(status().isCreated());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeCreate + 1);
        DiscountCode testDiscountCode = discountCodeList.get(discountCodeList.size() - 1);
        assertThat(testDiscountCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDiscountCode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDiscountCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discountCodeRepository.findAll().size();

        // Create the DiscountCode with an existing ID
        discountCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountCodeMockMvc.perform(post("/api/discount-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountCode)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountCodeRepository.findAll().size();
        // set the field null
        discountCode.setCode(null);

        // Create the DiscountCode, which fails.

        restDiscountCodeMockMvc.perform(post("/api/discount-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountCode)))
            .andExpect(status().isBadRequest());

        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscountCodes() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList
        restDiscountCodeMockMvc.perform(get("/api/discount-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getDiscountCode() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get the discountCode
        restDiscountCodeMockMvc.perform(get("/api/discount-codes/{id}", discountCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discountCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscountCode() throws Exception {
        // Get the discountCode
        restDiscountCodeMockMvc.perform(get("/api/discount-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscountCode() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();

        // Update the discountCode
        DiscountCode updatedDiscountCode = discountCodeRepository.findOne(discountCode.getId());
        updatedDiscountCode
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);

        restDiscountCodeMockMvc.perform(put("/api/discount-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscountCode)))
            .andExpect(status().isOk());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
        DiscountCode testDiscountCode = discountCodeList.get(discountCodeList.size() - 1);
        assertThat(testDiscountCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDiscountCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscountCode() throws Exception {
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();

        // Create the DiscountCode

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiscountCodeMockMvc.perform(put("/api/discount-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountCode)))
            .andExpect(status().isCreated());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDiscountCode() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);
        int databaseSizeBeforeDelete = discountCodeRepository.findAll().size();

        // Get the discountCode
        restDiscountCodeMockMvc.perform(delete("/api/discount-codes/{id}", discountCode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountCode.class);
        DiscountCode discountCode1 = new DiscountCode();
        discountCode1.setId(1L);
        DiscountCode discountCode2 = new DiscountCode();
        discountCode2.setId(discountCode1.getId());
        assertThat(discountCode1).isEqualTo(discountCode2);
        discountCode2.setId(2L);
        assertThat(discountCode1).isNotEqualTo(discountCode2);
        discountCode1.setId(null);
        assertThat(discountCode1).isNotEqualTo(discountCode2);
    }
}
