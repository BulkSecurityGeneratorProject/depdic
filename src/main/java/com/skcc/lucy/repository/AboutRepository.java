package com.skcc.lucy.repository;

import com.skcc.lucy.domain.About;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the About entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutRepository extends JpaRepository<About,Long> {

}
