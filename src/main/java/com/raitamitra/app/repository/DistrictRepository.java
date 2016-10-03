package com.raitamitra.app.repository;

import com.raitamitra.app.domain.District;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the District entity.
 */
@SuppressWarnings("unused")
public interface DistrictRepository extends JpaRepository<District,Long> {

}
