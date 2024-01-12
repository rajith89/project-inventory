package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.NewProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("newProjectRepository")
public interface NewProjectRepository extends JpaRepository<NewProject, Long> {

    @Query(value="SELECT * FROM new_project WHERE ins_id=:insId ORDER BY new_proj_id ASC", nativeQuery=true)
    List<NewProject> findAllById(@Param("insId") Long insId);
}
