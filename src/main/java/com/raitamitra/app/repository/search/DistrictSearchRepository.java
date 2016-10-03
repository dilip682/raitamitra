package com.raitamitra.app.repository.search;

import com.raitamitra.app.domain.District;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the District entity.
 */
public interface DistrictSearchRepository extends ElasticsearchRepository<District, Long> {
}
