package com.raitamitra.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raitamitra.app.domain.Taluka;

import com.raitamitra.app.repository.TalukaRepository;
import com.raitamitra.app.repository.search.TalukaSearchRepository;
import com.raitamitra.app.web.rest.util.HeaderUtil;
import com.raitamitra.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Taluka.
 */
@RestController
@RequestMapping("/api")
public class TalukaResource {

    private final Logger log = LoggerFactory.getLogger(TalukaResource.class);
        
    @Inject
    private TalukaRepository talukaRepository;

    @Inject
    private TalukaSearchRepository talukaSearchRepository;

    /**
     * POST  /talukas : Create a new taluka.
     *
     * @param taluka the taluka to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taluka, or with status 400 (Bad Request) if the taluka has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/talukas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Taluka> createTaluka(@RequestBody Taluka taluka) throws URISyntaxException {
        log.debug("REST request to save Taluka : {}", taluka);
        if (taluka.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("taluka", "idexists", "A new taluka cannot already have an ID")).body(null);
        }
        Taluka result = talukaRepository.save(taluka);
        talukaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/talukas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taluka", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /talukas : Updates an existing taluka.
     *
     * @param taluka the taluka to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taluka,
     * or with status 400 (Bad Request) if the taluka is not valid,
     * or with status 500 (Internal Server Error) if the taluka couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/talukas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Taluka> updateTaluka(@RequestBody Taluka taluka) throws URISyntaxException {
        log.debug("REST request to update Taluka : {}", taluka);
        if (taluka.getId() == null) {
            return createTaluka(taluka);
        }
        Taluka result = talukaRepository.save(taluka);
        talukaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taluka", taluka.getId().toString()))
            .body(result);
    }

    /**
     * GET  /talukas : get all the talukas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of talukas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/talukas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Taluka>> getAllTalukas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Talukas");
        Page<Taluka> page = talukaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/talukas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /talukas/:id : get the "id" taluka.
     *
     * @param id the id of the taluka to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taluka, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/talukas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Taluka> getTaluka(@PathVariable Long id) {
        log.debug("REST request to get Taluka : {}", id);
        Taluka taluka = talukaRepository.findOne(id);
        return Optional.ofNullable(taluka)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /talukas/:id : delete the "id" taluka.
     *
     * @param id the id of the taluka to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/talukas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaluka(@PathVariable Long id) {
        log.debug("REST request to delete Taluka : {}", id);
        talukaRepository.delete(id);
        talukaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taluka", id.toString())).build();
    }

    /**
     * SEARCH  /_search/talukas?query=:query : search for the taluka corresponding
     * to the query.
     *
     * @param query the query of the taluka search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/talukas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Taluka>> searchTalukas(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Talukas for query {}", query);
        Page<Taluka> page = talukaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/talukas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
