package com.raitamitra.app.web.rest;

import com.raitamitra.app.RaitamitraApp;

import com.raitamitra.app.domain.Taluka;
import com.raitamitra.app.repository.TalukaRepository;
import com.raitamitra.app.repository.search.TalukaSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TalukaResource REST controller.
 *
 * @see TalukaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaitamitraApp.class)
public class TalukaResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private TalukaRepository talukaRepository;

    @Inject
    private TalukaSearchRepository talukaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTalukaMockMvc;

    private Taluka taluka;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TalukaResource talukaResource = new TalukaResource();
        ReflectionTestUtils.setField(talukaResource, "talukaSearchRepository", talukaSearchRepository);
        ReflectionTestUtils.setField(talukaResource, "talukaRepository", talukaRepository);
        this.restTalukaMockMvc = MockMvcBuilders.standaloneSetup(talukaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taluka createEntity(EntityManager em) {
        Taluka taluka = new Taluka()
                .name(DEFAULT_NAME);
        return taluka;
    }

    @Before
    public void initTest() {
        talukaSearchRepository.deleteAll();
        taluka = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaluka() throws Exception {
        int databaseSizeBeforeCreate = talukaRepository.findAll().size();

        // Create the Taluka

        restTalukaMockMvc.perform(post("/api/talukas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taluka)))
                .andExpect(status().isCreated());

        // Validate the Taluka in the database
        List<Taluka> talukas = talukaRepository.findAll();
        assertThat(talukas).hasSize(databaseSizeBeforeCreate + 1);
        Taluka testTaluka = talukas.get(talukas.size() - 1);
        assertThat(testTaluka.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Taluka in ElasticSearch
        Taluka talukaEs = talukaSearchRepository.findOne(testTaluka.getId());
        assertThat(talukaEs).isEqualToComparingFieldByField(testTaluka);
    }

    @Test
    @Transactional
    public void getAllTalukas() throws Exception {
        // Initialize the database
        talukaRepository.saveAndFlush(taluka);

        // Get all the talukas
        restTalukaMockMvc.perform(get("/api/talukas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taluka.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTaluka() throws Exception {
        // Initialize the database
        talukaRepository.saveAndFlush(taluka);

        // Get the taluka
        restTalukaMockMvc.perform(get("/api/talukas/{id}", taluka.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taluka.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaluka() throws Exception {
        // Get the taluka
        restTalukaMockMvc.perform(get("/api/talukas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaluka() throws Exception {
        // Initialize the database
        talukaRepository.saveAndFlush(taluka);
        talukaSearchRepository.save(taluka);
        int databaseSizeBeforeUpdate = talukaRepository.findAll().size();

        // Update the taluka
        Taluka updatedTaluka = talukaRepository.findOne(taluka.getId());
        updatedTaluka
                .name(UPDATED_NAME);

        restTalukaMockMvc.perform(put("/api/talukas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTaluka)))
                .andExpect(status().isOk());

        // Validate the Taluka in the database
        List<Taluka> talukas = talukaRepository.findAll();
        assertThat(talukas).hasSize(databaseSizeBeforeUpdate);
        Taluka testTaluka = talukas.get(talukas.size() - 1);
        assertThat(testTaluka.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Taluka in ElasticSearch
        Taluka talukaEs = talukaSearchRepository.findOne(testTaluka.getId());
        assertThat(talukaEs).isEqualToComparingFieldByField(testTaluka);
    }

    @Test
    @Transactional
    public void deleteTaluka() throws Exception {
        // Initialize the database
        talukaRepository.saveAndFlush(taluka);
        talukaSearchRepository.save(taluka);
        int databaseSizeBeforeDelete = talukaRepository.findAll().size();

        // Get the taluka
        restTalukaMockMvc.perform(delete("/api/talukas/{id}", taluka.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean talukaExistsInEs = talukaSearchRepository.exists(taluka.getId());
        assertThat(talukaExistsInEs).isFalse();

        // Validate the database is empty
        List<Taluka> talukas = talukaRepository.findAll();
        assertThat(talukas).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTaluka() throws Exception {
        // Initialize the database
        talukaRepository.saveAndFlush(taluka);
        talukaSearchRepository.save(taluka);

        // Search the taluka
        restTalukaMockMvc.perform(get("/api/_search/talukas?query=id:" + taluka.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taluka.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
