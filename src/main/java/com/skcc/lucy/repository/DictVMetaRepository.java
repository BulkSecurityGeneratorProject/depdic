package com.skcc.lucy.repository;

import com.skcc.lucy.domain.DictVMeta;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DictVMeta entity.
 */
@SuppressWarnings("unused")
public interface DictVMetaRepository extends JpaRepository<DictVMeta,Long> {

}
