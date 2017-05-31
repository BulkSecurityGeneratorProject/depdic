package com.skcc.lucy.web.rest;

import com.skcc.lucy.DepdicApp;

import com.skcc.lucy.domain.About;
import com.skcc.lucy.repository.AboutRepository;
import com.skcc.lucy.repository.search.AboutSearchRepository;
import com.skcc.lucy.web.rest.errors.ExceptionTranslator;

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
 * Test class for the AboutResource REST controller.
 *
 * @see AboutResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DepdicApp.class)
public class AboutResourceIntTest {

    @Autowired
    private AboutRepository aboutRepository;

    @Autowired
    private AboutSearchRepository aboutSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAboutMockMvc;

    private About about;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AboutResource aboutResource = new AboutResource(aboutRepository, aboutSearchRepository);
        this.restAboutMockMvc = MockMvcBuilders.standaloneSetup(aboutResource)
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
    public static About createEntity(EntityManager em) {
        About about = new About();
        return about;
    }

    @Before
    public void initTest() {
        aboutSearchRepository.deleteAll();
        about = createEntity(em);
    }

    @Test
    @Transactional
    public void createAbout() throws Exception {
        int databaseSizeBeforeCreate = aboutRepository.findAll().size();

        // Create the About
        restAboutMockMvc.perform(post("/api/abouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(about)))
            .andExpect(status().isCreated());

        // Validate the About in the database
        List<About> aboutList = aboutRepository.findAll();
        assertThat(aboutList).hasSize(databaseSizeBeforeCreate + 1);
        About testAbout = aboutList.get(aboutList.size() - 1);

        // Validate the About in Elasticsearch
        About aboutEs = aboutSearchRepository.findOne(testAbout.getId());
        assertThat(aboutEs).isEqualToComparingFieldByField(testAbout);
    }

    @Test
    @Transactional
    public void createAboutWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aboutRepository.findAll().size();

        // Create the About with an existing ID
        about.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAboutMockMvc.perform(post("/api/abouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(about)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<About> aboutList = aboutRepository.findAll();
        assertThat(aboutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAbouts() throws Exception {
        // Initialize the database
        aboutRepository.saveAndFlush(about);

        // Get all the aboutList
        restAboutMockMvc.perform(get("/api/abouts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(about.getId().intValue())));
    }

    @Test
    @Transactional
    public void getAbout() throws Exception {
        // Initialize the database
        aboutRepository.saveAndFlush(about);

        // Get the about
        restAboutMockMvc.perform(get("/api/abouts/{id}", about.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(about.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAbout() throws Exception {
        // Get the about
        restAboutMockMvc.perform(get("/api/abouts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbout() throws Exception {
        // Initialize the database
        aboutRepository.saveAndFlush(about);
        aboutSearchRepository.save(about);
        int databaseSizeBeforeUpdate = aboutRepository.findAll().size();

        // Update the about
        About updatedAbout = aboutRepository.findOne(about.getId());

        restAboutMockMvc.perform(put("/api/abouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAbout)))
            .andExpect(status().isOk());

        // Validate the About in the database
        List<About> aboutList = aboutRepository.findAll();
        assertThat(aboutList).hasSize(databaseSizeBeforeUpdate);
        About testAbout = aboutList.get(aboutList.size() - 1);

        // Validate the About in Elasticsearch
        About aboutEs = aboutSearchRepository.findOne(testAbout.getId());
        assertThat(aboutEs).isEqualToComparingFieldByField(testAbout);
    }

    @Test
    @Transactional
    public void updateNonExistingAbout() throws Exception {
        int databaseSizeBeforeUpdate = aboutRepository.findAll().size();

        // Create the About

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAboutMockMvc.perform(put("/api/abouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(about)))
            .andExpect(status().isCreated());

        // Validate the About in the database
        List<About> aboutList = aboutRepository.findAll();
        assertThat(aboutList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAbout() throws Exception {
        // Initialize the database
        aboutRepository.saveAndFlush(about);
        aboutSearchRepository.save(about);
        int databaseSizeBeforeDelete = aboutRepository.findAll().size();

        // Get the about
        restAboutMockMvc.perform(delete("/api/abouts/{id}", about.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean aboutExistsInEs = aboutSearchRepository.exists(about.getId());
        assertThat(aboutExistsInEs).isFalse();

        // Validate the database is empty
        List<About> aboutList = aboutRepository.findAll();
        assertThat(aboutList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAbout() throws Exception {
        // Initialize the database
        aboutRepository.saveAndFlush(about);
        aboutSearchRepository.save(about);

        // Search the about
        restAboutMockMvc.perform(get("/api/_search/abouts?query=id:" + about.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(about.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(About.class);
        About about1 = new About();
        about1.setId(1L);
        About about2 = new About();
        about2.setId(about1.getId());
        assertThat(about1).isEqualTo(about2);
        about2.setId(2L);
        assertThat(about1).isNotEqualTo(about2);
        about1.setId(null);
        assertThat(about1).isNotEqualTo(about2);
    }
}
