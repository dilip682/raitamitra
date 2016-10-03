package com.raitamitra.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raitamitra.app.domain.Hobli;

import com.raitamitra.app.repository.HobliRepository;
import com.raitamitra.app.repository.search.HobliSearchRepository;
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
 * REST controller for managing Hobli.
 */
@RestController
@RequestMapping("/api")
public class HobliResource {

    private final Logger log = LoggerFactory.getLogger(HobliResource.class);
        
    @Inject
    private HobliRepository hobliRepository;

    @Inject
    private HobliSearchRepository hobliSearchRepository;

    /**
     * POST  /hoblis : Create a new hobli.
     *
     * @param hobli the hobli to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hobli, or with status 400 (Bad Request) if the hobli has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hoblis",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hobli> createHobli(@RequestBody Hobli hobli) throws URISyntaxException {
        log.debug("REST request to save Hobli : {}", hobli);
        if (hobli.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hobli", "idexists", "A new hobli cannot already have an ID")).body(null);
        }
        Hobli result = hobliRepository.save(hobli);
        hobliSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hoblis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hobli", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hoblis : Updates an existing hobli.
     *
     * @param hobli the hobli to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hobli,
     * or with status 400 (Bad Request) if the hobli is not valid,
     * or with status 500 (Internal Server Error) if the hobli couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hoblis",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hobli> updateHobli(@RequestBody Hobli hobli) throws URISyntaxException {
        log.debug("REST request to update Hobli : {}", hobli);
        if (hobli.getId() == null) {
            return createHobli(hobli);
        }
        Hobli result = hobliRepository.save(hobli);
        hobliSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hobli", hobli.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hoblis : get all the hoblis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hoblis in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/hoblis",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Hobli>> getAllHoblis(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Hoblis");
        Page<Hobli> page = hobliRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hoblis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hoblis/:id : get the "id" hobli.
     *
     * @param id the id of the hobli to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hobli, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/hoblis/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hobli> getHobli(@PathVariable Long id) {
        log.debug("REST request to get Hobli : {}", id);
        Hobli hobli = hobliRepository.findOne(id);
        return Optional.ofNullable(hobli)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hoblis/:id : delete the "id" hobli.
     *
     * @param id the id of the hobli to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/hoblis/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHobli(@PathVariable Long id) {
        log.debug("REST request to delete Hobli : {}", id);
        hobliRepository.delete(id);
        hobliSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hobli", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hoblis?query=:query : search for the hobli corresponding
     * to the query.
     *
     * @param query the query of the hobli search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/hoblis",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Hobli>> searchHoblis(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Hoblis for query {}", query);
        Page<Hobli> page = hobliSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/hoblis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
