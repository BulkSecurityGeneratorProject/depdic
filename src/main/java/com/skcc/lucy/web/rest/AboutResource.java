package com.skcc.lucy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skcc.lucy.domain.About;

import com.skcc.lucy.repository.AboutRepository;
import com.skcc.lucy.repository.search.AboutSearchRepository;
import com.skcc.lucy.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing About.
 */
@RestController
@RequestMapping("/api")
public class AboutResource {

    private final Logger log = LoggerFactory.getLogger(AboutResource.class);

    private static final String ENTITY_NAME = "about";
        
    private final AboutRepository aboutRepository;

    private final AboutSearchRepository aboutSearchRepository;

    public AboutResource(AboutRepository aboutRepository, AboutSearchRepository aboutSearchRepository) {
        this.aboutRepository = aboutRepository;
        this.aboutSearchRepository = aboutSearchRepository;
    }

    /**
     * POST  /abouts : Create a new about.
     *
     * @param about the about to create
     * @return the ResponseEntity with status 201 (Created) and with body the new about, or with status 400 (Bad Request) if the about has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/abouts")
    @Timed
    public ResponseEntity<About> createAbout(@RequestBody About about) throws URISyntaxException {
        log.debug("REST request to save About : {}", about);
        if (about.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new about cannot already have an ID")).body(null);
        }
        About result = aboutRepository.save(about);
        aboutSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/abouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /abouts : Updates an existing about.
     *
     * @param about the about to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated about,
     * or with status 400 (Bad Request) if the about is not valid,
     * or with status 500 (Internal Server Error) if the about couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/abouts")
    @Timed
    public ResponseEntity<About> updateAbout(@RequestBody About about) throws URISyntaxException {
        log.debug("REST request to update About : {}", about);
        if (about.getId() == null) {
            return createAbout(about);
        }
        About result = aboutRepository.save(about);
        aboutSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, about.getId().toString()))
            .body(result);
    }

    /**
     * GET  /abouts : get all the abouts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of abouts in body
     */
    @GetMapping("/abouts")
    @Timed
    public List<About> getAllAbouts() {
        log.debug("REST request to get all Abouts");
        List<About> abouts = aboutRepository.findAll();
        return abouts;
    }

    /**
     * GET  /abouts/:id : get the "id" about.
     *
     * @param id the id of the about to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the about, or with status 404 (Not Found)
     */
    @GetMapping("/abouts/{id}")
    @Timed
    public ResponseEntity<About> getAbout(@PathVariable Long id) {
        log.debug("REST request to get About : {}", id);
        About about = aboutRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(about));
    }

    /**
     * DELETE  /abouts/:id : delete the "id" about.
     *
     * @param id the id of the about to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/abouts/{id}")
    @Timed
    public ResponseEntity<Void> deleteAbout(@PathVariable Long id) {
        log.debug("REST request to delete About : {}", id);
        aboutRepository.delete(id);
        aboutSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/abouts?query=:query : search for the about corresponding
     * to the query.
     *
     * @param query the query of the about search 
     * @return the result of the search
     */
    @GetMapping("/_search/abouts")
    @Timed
    public List<About> searchAbouts(@RequestParam String query) {
        log.debug("REST request to search Abouts for query {}", query);
        return StreamSupport
            .stream(aboutSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
