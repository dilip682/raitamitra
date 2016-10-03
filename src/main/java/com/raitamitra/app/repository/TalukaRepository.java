package com.raitamitra.app.repository;

import com.raitamitra.app.domain.Taluka;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Taluka entity.
 */
@SuppressWarnings("unused")
public interface TalukaRepository extends JpaRepository<Taluka,Long> {

}
