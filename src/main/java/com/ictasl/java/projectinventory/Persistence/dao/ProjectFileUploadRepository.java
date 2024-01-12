package com.ictasl.java.projectinventory.Persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ictasl.java.projectinventory.Persistence.entity.ProjectFileEntity;


@Repository
public interface ProjectFileUploadRepository extends JpaRepository<ProjectFileEntity, Long> {

	List<ProjectFileEntity> findAllByOrgIdAndUserId(long organizationId, long userId);

	List<ProjectFileEntity> findAllByOrgId(long organizationId);
}
