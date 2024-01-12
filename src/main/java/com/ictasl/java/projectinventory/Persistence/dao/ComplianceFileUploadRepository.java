package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.ComplianceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ComplianceFileUploadRepository extends JpaRepository<ComplianceFile, Long> {

	List<ComplianceFile> findAllByOrgIdAndUserId(long organizationId, long userId);
	List<ComplianceFile> findAllByOrgId(long organizationId);
}
