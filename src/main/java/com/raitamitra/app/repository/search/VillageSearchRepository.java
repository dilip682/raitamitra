package com.raitamitra.app.repository.search;

import com.raitamitra.app.domain.Village;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Village entity.
 */
public interface VillageSearchRepository extends ElasticsearchRepository<Village, Long> {
}
