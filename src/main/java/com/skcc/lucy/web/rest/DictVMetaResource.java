package com.skcc.lucy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skcc.lucy.domain.DictVMeta;

import com.skcc.lucy.repository.DictVMetaRepository;
import com.skcc.lucy.repository.search.DictVMetaSearchRepository;
import com.skcc.lucy.web.rest.util.HeaderUtil;
import com.skcc.lucy.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DictVMeta.
 */
@RestController
@RequestMapping("/api")
public class DictVMetaResource {

    private final Logger log = LoggerFactory.getLogger(DictVMetaResource.class);

    private static final String ENTITY_NAME = "dictVMeta";
        
    private final DictVMetaRepository dictVMetaRepository;

    private final DictVMetaSearchRepository dictVMetaSearchRepository;

    public DictVMetaResource(DictVMetaRepository dictVMetaRepository, DictVMetaSearchRepository dictVMetaSearchRepository) {
        this.dictVMetaRepository = dictVMetaRepository;
        this.dictVMetaSearchRepository = dictVMetaSearchRepository;
    }

    /**
     * POST  /dict-v-metas : Create a new dictVMeta.
     *
     * @param dictVMeta the dictVMeta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dictVMeta, or with status 400 (Bad Request) if the dictVMeta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dict-v-metas")
    @Timed
    public ResponseEntity<DictVMeta> createDictVMeta(@RequestBody DictVMeta dictVMeta) throws URISyntaxException {
        log.debug("REST request to save DictVMeta : {}", dictVMeta);
        if (dictVMeta.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dictVMeta cannot already have an ID")).body(null);
        }
        DictVMeta result = dictVMetaRepository.save(dictVMeta);
        dictVMetaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dict-v-metas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dict-v-metas : Updates an existing dictVMeta.
     *
     * @param dictVMeta the dictVMeta to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dictVMeta,
     * or with status 400 (Bad Request) if the dictVMeta is not valid,
     * or with status 500 (Internal Server Error) if the dictVMeta couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dict-v-metas")
    @Timed
    public ResponseEntity<DictVMeta> updateDictVMeta(@RequestBody DictVMeta dictVMeta) throws URISyntaxException {
        log.debug("REST request to update DictVMeta : {}", dictVMeta);
        if (dictVMeta.getId() == null) {
            return createDictVMeta(dictVMeta);
        }
        DictVMeta result = dictVMetaRepository.save(dictVMeta);
        dictVMetaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dictVMeta.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dict-v-metas : get all the dictVMetas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dictVMetas in body
     */
    @GetMapping("/dict-v-metas")
    @Timed
    public ResponseEntity<List<DictVMeta>> getAllDictVMetas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DictVMetas");
        Page<DictVMeta> page = dictVMetaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dict-v-metas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dict-v-metas/:id : get the "id" dictVMeta.
     *
     * @param id the id of the dictVMeta to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dictVMeta, or with status 404 (Not Found)
     */
    @GetMapping("/dict-v-metas/{id}")
    @Timed
    public ResponseEntity<DictVMeta> getDictVMeta(@PathVariable Long id) {
        log.debug("REST request to get DictVMeta : {}", id);
        DictVMeta dictVMeta = dictVMetaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dictVMeta));
    }

    /**
     * DELETE  /dict-v-metas/:id : delete the "id" dictVMeta.
     *
     * @param id the id of the dictVMeta to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dict-v-metas/{id}")
    @Timed
    public ResponseEntity<Void> deleteDictVMeta(@PathVariable Long id) {
        log.debug("REST request to delete DictVMeta : {}", id);
        dictVMetaRepository.delete(id);
        dictVMetaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/dict-v-metas?query=:query : search for the dictVMeta corresponding
     * to the query.
     *
     * @param query the query of the dictVMeta search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/dict-v-metas")
    @Timed
    public ResponseEntity<List<DictVMeta>> searchDictVMetas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of DictVMetas for query {}", query);
        Page<DictVMeta> page = dictVMetaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/dict-v-metas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
