package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.ExistingProject;
import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("existingProjectRepository")
public interface ExistingProjectRepository extends JpaRepository<ExistingProject, Long> {

    @Query(value="SELECT * FROM existing_project WHERE ins_id=:insId ORDER BY proj_id ASC", nativeQuery=true)
    List<ExistingProject> findAllById(@Param("insId") Long insId);
}
