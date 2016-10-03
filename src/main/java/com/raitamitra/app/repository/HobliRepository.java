package com.raitamitra.app.repository;

import com.raitamitra.app.domain.Hobli;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hobli entity.
 */
@SuppressWarnings("unused")
public interface HobliRepository extends JpaRepository<Hobli,Long> {

}
