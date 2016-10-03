package com.raitamitra.app.repository.search;

import com.raitamitra.app.domain.Hobli;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Hobli entity.
 */
public interface HobliSearchRepository extends ElasticsearchRepository<Hobli, Long> {
}
