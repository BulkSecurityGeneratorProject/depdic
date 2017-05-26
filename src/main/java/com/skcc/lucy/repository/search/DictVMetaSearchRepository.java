package com.skcc.lucy.repository.search;

import com.skcc.lucy.domain.DictVMeta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DictVMeta entity.
 */
public interface DictVMetaSearchRepository extends ElasticsearchRepository<DictVMeta, Long> {
}
