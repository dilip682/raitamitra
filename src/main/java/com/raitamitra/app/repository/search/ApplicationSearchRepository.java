package com.raitamitra.app.repository.search;

import com.raitamitra.app.domain.Application;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Application entity.
 */
public interface ApplicationSearchRepository extends ElasticsearchRepository<Application, Long> {
}
