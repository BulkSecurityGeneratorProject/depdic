package com.skcc.lucy.repository.search;

import com.skcc.lucy.domain.About;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the About entity.
 */
public interface AboutSearchRepository extends ElasticsearchRepository<About, Long> {
}
