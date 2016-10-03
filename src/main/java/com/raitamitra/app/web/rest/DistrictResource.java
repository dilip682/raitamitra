package com.raitamitra.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raitamitra.app.domain.District;

import com.raitamitra.app.repository.DistrictRepository;
import com.raitamitra.app.repository.search.DistrictSearchRepository;
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
 * REST controller for managing District.
 */
@RestController
@RequestMapping("/api")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);
        
    @Inject
    private DistrictRepository districtRepository;

    @Inject
    private DistrictSearchRepository districtSearchRepository;

    /**
     * POST  /districts : Create a new district.
     *
     * @param district the district to create
     * @return the ResponseEntity with status 201 (Created) and with body the new district, or with status 400 (Bad Request) if the district has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/districts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> createDistrict(@RequestBody District district) throws URISyntaxException {
        log.debug("REST request to save District : {}", district);
        if (district.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("district", "idexists", "A new district cannot already have an ID")).body(null);
        }
        District result = districtRepository.save(district);
        districtSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("district", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /districts : Updates an existing district.
     *
     * @param district the district to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated district,
     * or with status 400 (Bad Request) if the district is not valid,
     * or with status 500 (Internal Server Error) if the district couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/districts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> updateDistrict(@RequestBody District district) throws URISyntaxException {
        log.debug("REST request to update District : {}", district);
        if (district.getId() == null) {
            return createDistrict(district);
        }
        District result = districtRepository.save(district);
        districtSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("district", district.getId().toString()))
            .body(result);
    }

    /**
     * GET  /districts : get all the districts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of districts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/districts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<District>> getAllDistricts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Districts");
        Page<District> page = districtRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /districts/:id : get the "id" district.
     *
     * @param id the id of the district to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the district, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/districts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> getDistrict(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        District district = districtRepository.findOne(id);
        return Optional.ofNullable(district)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /districts/:id : delete the "id" district.
     *
     * @param id the id of the district to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/districts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        districtRepository.delete(id);
        districtSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("district", id.toString())).build();
    }

    /**
     * SEARCH  /_search/districts?query=:query : search for the district corresponding
     * to the query.
     *
     * @param query the query of the district search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/districts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<District>> searchDistricts(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Districts for query {}", query);
        Page<District> page = districtSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
