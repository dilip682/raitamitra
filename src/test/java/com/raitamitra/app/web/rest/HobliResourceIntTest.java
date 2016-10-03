package com.raitamitra.app.web.rest;

import com.raitamitra.app.RaitamitraApp;

import com.raitamitra.app.domain.Hobli;
import com.raitamitra.app.repository.HobliRepository;
import com.raitamitra.app.repository.search.HobliSearchRepository;

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
 * Test class for the HobliResource REST controller.
 *
 * @see HobliResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaitamitraApp.class)
public class HobliResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private HobliRepository hobliRepository;

    @Inject
    private HobliSearchRepository hobliSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHobliMockMvc;

    private Hobli hobli;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HobliResource hobliResource = new HobliResource();
        ReflectionTestUtils.setField(hobliResource, "hobliSearchRepository", hobliSearchRepository);
        ReflectionTestUtils.setField(hobliResource, "hobliRepository", hobliRepository);
        this.restHobliMockMvc = MockMvcBuilders.standaloneSetup(hobliResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hobli createEntity(EntityManager em) {
        Hobli hobli = new Hobli()
                .name(DEFAULT_NAME);
        return hobli;
    }

    @Before
    public void initTest() {
        hobliSearchRepository.deleteAll();
        hobli = createEntity(em);
    }

    @Test
    @Transactional
    public void createHobli() throws Exception {
        int databaseSizeBeforeCreate = hobliRepository.findAll().size();

        // Create the Hobli

        restHobliMockMvc.perform(post("/api/hoblis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hobli)))
                .andExpect(status().isCreated());

        // Validate the Hobli in the database
        List<Hobli> hoblis = hobliRepository.findAll();
        assertThat(hoblis).hasSize(databaseSizeBeforeCreate + 1);
        Hobli testHobli = hoblis.get(hoblis.size() - 1);
        assertThat(testHobli.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Hobli in ElasticSearch
        Hobli hobliEs = hobliSearchRepository.findOne(testHobli.getId());
        assertThat(hobliEs).isEqualToComparingFieldByField(testHobli);
    }

    @Test
    @Transactional
    public void getAllHoblis() throws Exception {
        // Initialize the database
        hobliRepository.saveAndFlush(hobli);

        // Get all the hoblis
        restHobliMockMvc.perform(get("/api/hoblis?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hobli.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getHobli() throws Exception {
        // Initialize the database
        hobliRepository.saveAndFlush(hobli);

        // Get the hobli
        restHobliMockMvc.perform(get("/api/hoblis/{id}", hobli.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hobli.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHobli() throws Exception {
        // Get the hobli
        restHobliMockMvc.perform(get("/api/hoblis/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHobli() throws Exception {
        // Initialize the database
        hobliRepository.saveAndFlush(hobli);
        hobliSearchRepository.save(hobli);
        int databaseSizeBeforeUpdate = hobliRepository.findAll().size();

        // Update the hobli
        Hobli updatedHobli = hobliRepository.findOne(hobli.getId());
        updatedHobli
                .name(UPDATED_NAME);

        restHobliMockMvc.perform(put("/api/hoblis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHobli)))
                .andExpect(status().isOk());

        // Validate the Hobli in the database
        List<Hobli> hoblis = hobliRepository.findAll();
        assertThat(hoblis).hasSize(databaseSizeBeforeUpdate);
        Hobli testHobli = hoblis.get(hoblis.size() - 1);
        assertThat(testHobli.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Hobli in ElasticSearch
        Hobli hobliEs = hobliSearchRepository.findOne(testHobli.getId());
        assertThat(hobliEs).isEqualToComparingFieldByField(testHobli);
    }

    @Test
    @Transactional
    public void deleteHobli() throws Exception {
        // Initialize the database
        hobliRepository.saveAndFlush(hobli);
        hobliSearchRepository.save(hobli);
        int databaseSizeBeforeDelete = hobliRepository.findAll().size();

        // Get the hobli
        restHobliMockMvc.perform(delete("/api/hoblis/{id}", hobli.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean hobliExistsInEs = hobliSearchRepository.exists(hobli.getId());
        assertThat(hobliExistsInEs).isFalse();

        // Validate the database is empty
        List<Hobli> hoblis = hobliRepository.findAll();
        assertThat(hoblis).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHobli() throws Exception {
        // Initialize the database
        hobliRepository.saveAndFlush(hobli);
        hobliSearchRepository.save(hobli);

        // Search the hobli
        restHobliMockMvc.perform(get("/api/_search/hoblis?query=id:" + hobli.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hobli.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
