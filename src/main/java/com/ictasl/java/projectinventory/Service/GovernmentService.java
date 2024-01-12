package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Web.dto.GovernmentServicesDTO;
import com.ictasl.java.projectinventory.Web.dto.InstituteManageDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

public interface GovernmentService {

	void createServiceDetails(@Valid GovernmentServicesDTO governmentServicesDTO);

	GovernmentServicesDTO editGovernmentService(long id);

	void updateGovernmentService(long id, @Valid GovernmentServicesDTO governmentServicesDTO);

	List<GovernmentServicesDTO> getAllServicesList();

	void deleteGovernmentService(long id);

	GovernmentServicesDTO getServices(Long id) throws ResourceNotFoundException, ResourceNotFoundException;

	boolean isDetailsAvailable(Long id);
}
