package com.ictasl.java.projectinventory.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Web.dto.InstituteManageDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;

public interface InstituteManageService {

	List<HashMap<String, String>> getMinistryNameList() throws Exception;

	void createInstitute(@Valid InstituteManageDto institueManageDto,Principal principal) throws Exception;

	InstituteManageDto editInstitute(long id) throws Exception;

	void update(long id, @Valid InstituteManageDto institueManageDto,Principal principal) throws Exception;

	List<InstituteManageDto> getAllSavedList() throws Exception;

	void delete(long id) throws Exception;

	List<HashMap<String, String>> getInsNameList();

	InstituteManageDto getInstitution(Long id) throws ResourceNotFoundException, ResourceNotFoundException;

	List<HashMap<String, String>> getAllInsByMinId(long minId) throws Exception;

	Page<ManageInstituteEntity> findPaginated(int pageNo, int pageSize);

}
