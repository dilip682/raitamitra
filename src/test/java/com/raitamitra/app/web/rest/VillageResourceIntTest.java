package com.raitamitra.app.web.rest;

import com.raitamitra.app.RaitamitraApp;

import com.raitamitra.app.domain.Village;
import com.raitamitra.app.repository.VillageRepository;
import com.raitamitra.app.repository.search.VillageSearchRepository;

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
 * Test class for the VillageResource REST controller.
 *
 * @see VillageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaitamitraApp.class)
public class VillageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private VillageRepository villageRepository;

    @Inject
    private VillageSearchRepository villageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restVillageMockMvc;

    private Village village;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VillageResource villageResource = new VillageResource();
        ReflectionTestUtils.setField(villageResource, "villageSearchRepository", villageSearchRepository);
        ReflectionTestUtils.setField(villageResource, "villageRepository", villageRepository);
        this.restVillageMockMvc = MockMvcBuilders.standaloneSetup(villageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createEntity(EntityManager em) {
        Village village = new Village()
                .name(DEFAULT_NAME);
        return village;
    }

    @Before
    public void initTest() {
        villageSearchRepository.deleteAll();
        village = createEntity(em);
    }

    @Test
    @Transactional
    public void createVillage() throws Exception {
        int databaseSizeBeforeCreate = villageRepository.findAll().size();

        // Create the Village

        restVillageMockMvc.perform(post("/api/villages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(village)))
                .andExpect(status().isCreated());

        // Validate the Village in the database
        List<Village> villages = villageRepository.findAll();
        assertThat(villages).hasSize(databaseSizeBeforeCreate + 1);
        Village testVillage = villages.get(villages.size() - 1);
        assertThat(testVillage.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Village in ElasticSearch
        Village villageEs = villageSearchRepository.findOne(testVillage.getId());
        assertThat(villageEs).isEqualToComparingFieldByField(testVillage);
    }

    @Test
    @Transactional
    public void getAllVillages() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villages
        restVillageMockMvc.perform(get("/api/villages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get the village
        restVillageMockMvc.perform(get("/api/villages/{id}", village.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(village.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVillage() throws Exception {
        // Get the village
        restVillageMockMvc.perform(get("/api/villages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);
        villageSearchRepository.save(village);
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village
        Village updatedVillage = villageRepository.findOne(village.getId());
        updatedVillage
                .name(UPDATED_NAME);

        restVillageMockMvc.perform(put("/api/villages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVillage)))
                .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villages = villageRepository.findAll();
        assertThat(villages).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villages.get(villages.size() - 1);
        assertThat(testVillage.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Village in ElasticSearch
        Village villageEs = villageSearchRepository.findOne(testVillage.getId());
        assertThat(villageEs).isEqualToComparingFieldByField(testVillage);
    }

    @Test
    @Transactional
    public void deleteVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);
        villageSearchRepository.save(village);
        int databaseSizeBeforeDelete = villageRepository.findAll().size();

        // Get the village
        restVillageMockMvc.perform(delete("/api/villages/{id}", village.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean villageExistsInEs = villageSearchRepository.exists(village.getId());
        assertThat(villageExistsInEs).isFalse();

        // Validate the database is empty
        List<Village> villages = villageRepository.findAll();
        assertThat(villages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);
        villageSearchRepository.save(village);

        // Search the village
        restVillageMockMvc.perform(get("/api/_search/villages?query=id:" + village.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
