package com.raitamitra.app.repository;

import com.raitamitra.app.domain.Village;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Village entity.
 */
@SuppressWarnings("unused")
public interface VillageRepository extends JpaRepository<Village,Long> {

}
