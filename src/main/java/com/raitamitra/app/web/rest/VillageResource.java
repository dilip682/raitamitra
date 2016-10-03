package com.raitamitra.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raitamitra.app.domain.Village;

import com.raitamitra.app.repository.VillageRepository;
import com.raitamitra.app.repository.search.VillageSearchRepository;
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
 * REST controller for managing Village.
 */
@RestController
@RequestMapping("/api")
public class VillageResource {

    private final Logger log = LoggerFactory.getLogger(VillageResource.class);
        
    @Inject
    private VillageRepository villageRepository;

    @Inject
    private VillageSearchRepository villageSearchRepository;

    /**
     * POST  /villages : Create a new village.
     *
     * @param village the village to create
     * @return the ResponseEntity with status 201 (Created) and with body the new village, or with status 400 (Bad Request) if the village has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/villages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Village> createVillage(@RequestBody Village village) throws URISyntaxException {
        log.debug("REST request to save Village : {}", village);
        if (village.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("village", "idexists", "A new village cannot already have an ID")).body(null);
        }
        Village result = villageRepository.save(village);
        villageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/villages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("village", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /villages : Updates an existing village.
     *
     * @param village the village to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated village,
     * or with status 400 (Bad Request) if the village is not valid,
     * or with status 500 (Internal Server Error) if the village couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/villages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Village> updateVillage(@RequestBody Village village) throws URISyntaxException {
        log.debug("REST request to update Village : {}", village);
        if (village.getId() == null) {
            return createVillage(village);
        }
        Village result = villageRepository.save(village);
        villageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("village", village.getId().toString()))
            .body(result);
    }

    /**
     * GET  /villages : get all the villages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of villages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/villages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Village>> getAllVillages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Villages");
        Page<Village> page = villageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/villages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /villages/:id : get the "id" village.
     *
     * @param id the id of the village to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the village, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/villages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Village> getVillage(@PathVariable Long id) {
        log.debug("REST request to get Village : {}", id);
        Village village = villageRepository.findOne(id);
        return Optional.ofNullable(village)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /villages/:id : delete the "id" village.
     *
     * @param id the id of the village to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/villages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVillage(@PathVariable Long id) {
        log.debug("REST request to delete Village : {}", id);
        villageRepository.delete(id);
        villageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("village", id.toString())).build();
    }

    /**
     * SEARCH  /_search/villages?query=:query : search for the village corresponding
     * to the query.
     *
     * @param query the query of the village search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/villages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Village>> searchVillages(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Villages for query {}", query);
        Page<Village> page = villageSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/villages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
