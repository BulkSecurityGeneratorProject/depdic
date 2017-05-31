package com.skcc.lucy.repository;

import com.skcc.lucy.domain.DictVMeta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DictVMeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DictVMetaRepository extends JpaRepository<DictVMeta,Long> {

}
