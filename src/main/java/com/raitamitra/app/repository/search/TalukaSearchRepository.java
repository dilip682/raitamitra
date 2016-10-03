package com.raitamitra.app.repository.search;

import com.raitamitra.app.domain.Taluka;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Taluka entity.
 */
public interface TalukaSearchRepository extends ElasticsearchRepository<Taluka, Long> {
}
