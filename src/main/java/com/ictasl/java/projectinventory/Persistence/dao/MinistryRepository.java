package com.ictasl.java.projectinventory.Persistence.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.ictasl.java.projectinventory.Persistence.entity.MinistryEntity;

public interface MinistryRepository extends JpaRepository<MinistryEntity, Long> {

	List<MinistryEntity> findAllByStatus(String status);

	Optional<MinistryEntity> findByName(String name); 

}
