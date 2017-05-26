package com.skcc.lucy.web.rest;

import com.skcc.lucy.DepdicApp;

import com.skcc.lucy.domain.DictVMeta;
import com.skcc.lucy.repository.DictVMetaRepository;
import com.skcc.lucy.repository.search.DictVMetaSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skcc.lucy.domain.enumeration.ColTyp;
/**
 * Test class for the DictVMetaResource REST controller.
 *
 * @see DictVMetaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DepdicApp.class)
public class DictVMetaResourceIntTest {

    private static final ColTyp DEFAULT_COL_TYP = ColTyp.WORD;
    private static final ColTyp UPDATED_COL_TYP = ColTyp.COL;

    private static final String DEFAULT_COL_KR = "AAAAAAAAAA";
    private static final String UPDATED_COL_KR = "BBBBBBBBBB";

    private static final String DEFAULT_COL_EN = "AAAAAAAAAA";
    private static final String UPDATED_COL_EN = "BBBBBBBBBB";

    private static final String DEFAULT_JSON_DESC = "AAAAAAAAAA";
    private static final String UPDATED_JSON_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DTM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DTM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DictVMetaRepository dictVMetaRepository;

    @Autowired
    private DictVMetaSearchRepository dictVMetaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDictVMetaMockMvc;

    private DictVMeta dictVMeta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DictVMetaResource dictVMetaResource = new DictVMetaResource(dictVMetaRepository, dictVMetaSearchRepository);
        this.restDictVMetaMockMvc = MockMvcBuilders.standaloneSetup(dictVMetaResource)
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
    public static DictVMeta createEntity(EntityManager em) {
        DictVMeta dictVMeta = new DictVMeta()
            .colTyp(DEFAULT_COL_TYP)
            .colKr(DEFAULT_COL_KR)
            .colEn(DEFAULT_COL_EN)
            .jsonDesc(DEFAULT_JSON_DESC)
            .memo(DEFAULT_MEMO)
            .dtm(DEFAULT_DTM);
        return dictVMeta;
    }

    @Before
    public void initTest() {
        dictVMetaSearchRepository.deleteAll();
        dictVMeta = createEntity(em);
    }

    @Test
    @Transactional
    public void createDictVMeta() throws Exception {
        int databaseSizeBeforeCreate = dictVMetaRepository.findAll().size();

        // Create the DictVMeta
        restDictVMetaMockMvc.perform(post("/api/dict-v-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictVMeta)))
            .andExpect(status().isCreated());

        // Validate the DictVMeta in the database
        List<DictVMeta> dictVMetaList = dictVMetaRepository.findAll();
        assertThat(dictVMetaList).hasSize(databaseSizeBeforeCreate + 1);
        DictVMeta testDictVMeta = dictVMetaList.get(dictVMetaList.size() - 1);
        assertThat(testDictVMeta.getColTyp()).isEqualTo(DEFAULT_COL_TYP);
        assertThat(testDictVMeta.getColKr()).isEqualTo(DEFAULT_COL_KR);
        assertThat(testDictVMeta.getColEn()).isEqualTo(DEFAULT_COL_EN);
        assertThat(testDictVMeta.getJsonDesc()).isEqualTo(DEFAULT_JSON_DESC);
        assertThat(testDictVMeta.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testDictVMeta.getDtm()).isEqualTo(DEFAULT_DTM);

        // Validate the DictVMeta in Elasticsearch
        DictVMeta dictVMetaEs = dictVMetaSearchRepository.findOne(testDictVMeta.getId());
        assertThat(dictVMetaEs).isEqualToComparingFieldByField(testDictVMeta);
    }

    @Test
    @Transactional
    public void createDictVMetaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dictVMetaRepository.findAll().size();

        // Create the DictVMeta with an existing ID
        dictVMeta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDictVMetaMockMvc.perform(post("/api/dict-v-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictVMeta)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DictVMeta> dictVMetaList = dictVMetaRepository.findAll();
        assertThat(dictVMetaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDictVMetas() throws Exception {
        // Initialize the database
        dictVMetaRepository.saveAndFlush(dictVMeta);

        // Get all the dictVMetaList
        restDictVMetaMockMvc.perform(get("/api/dict-v-metas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dictVMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].colTyp").value(hasItem(DEFAULT_COL_TYP.toString())))
            .andExpect(jsonPath("$.[*].colKr").value(hasItem(DEFAULT_COL_KR.toString())))
            .andExpect(jsonPath("$.[*].colEn").value(hasItem(DEFAULT_COL_EN.toString())))
            .andExpect(jsonPath("$.[*].jsonDesc").value(hasItem(DEFAULT_JSON_DESC.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].dtm").value(hasItem(DEFAULT_DTM.toString())));
    }

    @Test
    @Transactional
    public void getDictVMeta() throws Exception {
        // Initialize the database
        dictVMetaRepository.saveAndFlush(dictVMeta);

        // Get the dictVMeta
        restDictVMetaMockMvc.perform(get("/api/dict-v-metas/{id}", dictVMeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dictVMeta.getId().intValue()))
            .andExpect(jsonPath("$.colTyp").value(DEFAULT_COL_TYP.toString()))
            .andExpect(jsonPath("$.colKr").value(DEFAULT_COL_KR.toString()))
            .andExpect(jsonPath("$.colEn").value(DEFAULT_COL_EN.toString()))
            .andExpect(jsonPath("$.jsonDesc").value(DEFAULT_JSON_DESC.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.dtm").value(DEFAULT_DTM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDictVMeta() throws Exception {
        // Get the dictVMeta
        restDictVMetaMockMvc.perform(get("/api/dict-v-metas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDictVMeta() throws Exception {
        // Initialize the database
        dictVMetaRepository.saveAndFlush(dictVMeta);
        dictVMetaSearchRepository.save(dictVMeta);
        int databaseSizeBeforeUpdate = dictVMetaRepository.findAll().size();

        // Update the dictVMeta
        DictVMeta updatedDictVMeta = dictVMetaRepository.findOne(dictVMeta.getId());
        updatedDictVMeta
            .colTyp(UPDATED_COL_TYP)
            .colKr(UPDATED_COL_KR)
            .colEn(UPDATED_COL_EN)
            .jsonDesc(UPDATED_JSON_DESC)
            .memo(UPDATED_MEMO)
            .dtm(UPDATED_DTM);

        restDictVMetaMockMvc.perform(put("/api/dict-v-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDictVMeta)))
            .andExpect(status().isOk());

        // Validate the DictVMeta in the database
        List<DictVMeta> dictVMetaList = dictVMetaRepository.findAll();
        assertThat(dictVMetaList).hasSize(databaseSizeBeforeUpdate);
        DictVMeta testDictVMeta = dictVMetaList.get(dictVMetaList.size() - 1);
        assertThat(testDictVMeta.getColTyp()).isEqualTo(UPDATED_COL_TYP);
        assertThat(testDictVMeta.getColKr()).isEqualTo(UPDATED_COL_KR);
        assertThat(testDictVMeta.getColEn()).isEqualTo(UPDATED_COL_EN);
        assertThat(testDictVMeta.getJsonDesc()).isEqualTo(UPDATED_JSON_DESC);
        assertThat(testDictVMeta.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testDictVMeta.getDtm()).isEqualTo(UPDATED_DTM);

        // Validate the DictVMeta in Elasticsearch
        DictVMeta dictVMetaEs = dictVMetaSearchRepository.findOne(testDictVMeta.getId());
        assertThat(dictVMetaEs).isEqualToComparingFieldByField(testDictVMeta);
    }

    @Test
    @Transactional
    public void updateNonExistingDictVMeta() throws Exception {
        int databaseSizeBeforeUpdate = dictVMetaRepository.findAll().size();

        // Create the DictVMeta

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDictVMetaMockMvc.perform(put("/api/dict-v-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictVMeta)))
            .andExpect(status().isCreated());

        // Validate the DictVMeta in the database
        List<DictVMeta> dictVMetaList = dictVMetaRepository.findAll();
        assertThat(dictVMetaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDictVMeta() throws Exception {
        // Initialize the database
        dictVMetaRepository.saveAndFlush(dictVMeta);
        dictVMetaSearchRepository.save(dictVMeta);
        int databaseSizeBeforeDelete = dictVMetaRepository.findAll().size();

        // Get the dictVMeta
        restDictVMetaMockMvc.perform(delete("/api/dict-v-metas/{id}", dictVMeta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dictVMetaExistsInEs = dictVMetaSearchRepository.exists(dictVMeta.getId());
        assertThat(dictVMetaExistsInEs).isFalse();

        // Validate the database is empty
        List<DictVMeta> dictVMetaList = dictVMetaRepository.findAll();
        assertThat(dictVMetaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDictVMeta() throws Exception {
        // Initialize the database
        dictVMetaRepository.saveAndFlush(dictVMeta);
        dictVMetaSearchRepository.save(dictVMeta);

        // Search the dictVMeta
        restDictVMetaMockMvc.perform(get("/api/_search/dict-v-metas?query=id:" + dictVMeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dictVMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].colTyp").value(hasItem(DEFAULT_COL_TYP.toString())))
            .andExpect(jsonPath("$.[*].colKr").value(hasItem(DEFAULT_COL_KR.toString())))
            .andExpect(jsonPath("$.[*].colEn").value(hasItem(DEFAULT_COL_EN.toString())))
            .andExpect(jsonPath("$.[*].jsonDesc").value(hasItem(DEFAULT_JSON_DESC.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].dtm").value(hasItem(DEFAULT_DTM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DictVMeta.class);
        DictVMeta dictVMeta1 = new DictVMeta();
        dictVMeta1.setId(1L);
        DictVMeta dictVMeta2 = new DictVMeta();
        dictVMeta2.setId(dictVMeta1.getId());
        assertThat(dictVMeta1).isEqualTo(dictVMeta2);
        dictVMeta2.setId(2L);
        assertThat(dictVMeta1).isNotEqualTo(dictVMeta2);
        dictVMeta1.setId(null);
        assertThat(dictVMeta1).isNotEqualTo(dictVMeta2);
    }
}
