package org.mlccc.cm.web.rest;

import org.mlccc.cm.MlcccApp;

import org.mlccc.cm.domain.NewsLetter;
import org.mlccc.cm.repository.NewsLetterRepository;
import org.mlccc.cm.service.NewsLetterService;
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
 * Test class for the NewsLetterResource REST controller.
 *
 * @see NewsLetterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MlcccApp.class)
public class NewsLetterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPLOAD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPLOAD_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_UPLOADED_BY = 1;
    private static final Integer UPDATED_UPLOADED_BY = 2;

    @Autowired
    private NewsLetterRepository newsLetterRepository;

    @Autowired
    private NewsLetterService newsLetterService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNewsLetterMockMvc;

    private NewsLetter newsLetter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NewsLetterResource newsLetterResource = new NewsLetterResource(newsLetterService);
        this.restNewsLetterMockMvc = MockMvcBuilders.standaloneSetup(newsLetterResource)
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
    public static NewsLetter createEntity(EntityManager em) {
        NewsLetter newsLetter = new NewsLetter()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .fileName(DEFAULT_FILE_NAME)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .uploadedBy(DEFAULT_UPLOADED_BY);
        return newsLetter;
    }

    @Before
    public void initTest() {
        newsLetter = createEntity(em);
    }

    @Test
    @Transactional
    public void createNewsLetter() throws Exception {
        int databaseSizeBeforeCreate = newsLetterRepository.findAll().size();

        // Create the NewsLetter
        restNewsLetterMockMvc.perform(post("/api/news-letters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsLetter)))
            .andExpect(status().isCreated());

        // Validate the NewsLetter in the database
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        assertThat(newsLetterList).hasSize(databaseSizeBeforeCreate + 1);
        NewsLetter testNewsLetter = newsLetterList.get(newsLetterList.size() - 1);
        assertThat(testNewsLetter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNewsLetter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNewsLetter.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testNewsLetter.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testNewsLetter.getUploadedBy()).isEqualTo(DEFAULT_UPLOADED_BY);
    }

    @Test
    @Transactional
    public void createNewsLetterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = newsLetterRepository.findAll().size();

        // Create the NewsLetter with an existing ID
        newsLetter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsLetterMockMvc.perform(post("/api/news-letters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsLetter)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        assertThat(newsLetterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNewsLetters() throws Exception {
        // Initialize the database
        newsLetterRepository.saveAndFlush(newsLetter);

        // Get all the newsLetterList
        restNewsLetterMockMvc.perform(get("/api/news-letters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsLetter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadedBy").value(hasItem(DEFAULT_UPLOADED_BY)));
    }

    @Test
    @Transactional
    public void getNewsLetter() throws Exception {
        // Initialize the database
        newsLetterRepository.saveAndFlush(newsLetter);

        // Get the newsLetter
        restNewsLetterMockMvc.perform(get("/api/news-letters/{id}", newsLetter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(newsLetter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.uploadDate").value(DEFAULT_UPLOAD_DATE.toString()))
            .andExpect(jsonPath("$.uploadedBy").value(DEFAULT_UPLOADED_BY));
    }

    @Test
    @Transactional
    public void getNonExistingNewsLetter() throws Exception {
        // Get the newsLetter
        restNewsLetterMockMvc.perform(get("/api/news-letters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNewsLetter() throws Exception {
        // Initialize the database
        newsLetterService.save(newsLetter);

        int databaseSizeBeforeUpdate = newsLetterRepository.findAll().size();

        // Update the newsLetter
        NewsLetter updatedNewsLetter = newsLetterRepository.findOne(newsLetter.getId());
        updatedNewsLetter
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .uploadedBy(UPDATED_UPLOADED_BY);

        restNewsLetterMockMvc.perform(put("/api/news-letters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNewsLetter)))
            .andExpect(status().isOk());

        // Validate the NewsLetter in the database
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        assertThat(newsLetterList).hasSize(databaseSizeBeforeUpdate);
        NewsLetter testNewsLetter = newsLetterList.get(newsLetterList.size() - 1);
        assertThat(testNewsLetter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNewsLetter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNewsLetter.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testNewsLetter.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testNewsLetter.getUploadedBy()).isEqualTo(UPDATED_UPLOADED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingNewsLetter() throws Exception {
        int databaseSizeBeforeUpdate = newsLetterRepository.findAll().size();

        // Create the NewsLetter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNewsLetterMockMvc.perform(put("/api/news-letters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsLetter)))
            .andExpect(status().isCreated());

        // Validate the NewsLetter in the database
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        assertThat(newsLetterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNewsLetter() throws Exception {
        // Initialize the database
        newsLetterService.save(newsLetter);

        int databaseSizeBeforeDelete = newsLetterRepository.findAll().size();

        // Get the newsLetter
        restNewsLetterMockMvc.perform(delete("/api/news-letters/{id}", newsLetter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        assertThat(newsLetterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsLetter.class);
        NewsLetter newsLetter1 = new NewsLetter();
        newsLetter1.setId(1L);
        NewsLetter newsLetter2 = new NewsLetter();
        newsLetter2.setId(newsLetter1.getId());
        assertThat(newsLetter1).isEqualTo(newsLetter2);
        newsLetter2.setId(2L);
        assertThat(newsLetter1).isNotEqualTo(newsLetter2);
        newsLetter1.setId(null);
        assertThat(newsLetter1).isNotEqualTo(newsLetter2);
    }
}
