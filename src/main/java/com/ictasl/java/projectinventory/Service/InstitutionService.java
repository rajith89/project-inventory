package com.ictasl.java.projectinventory.Service;


import com.ictasl.java.projectinventory.Persistence.entity.MinistryEntity;
import com.ictasl.java.projectinventory.Web.dto.InstitutionDetailsDTO;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InstitutionService {

    InstitutionDetailsDTO createInstitution(InstitutionDetailsDTO institutionDTO);
    InstitutionDetailsDTO updateInstitution (Long id, InstitutionDetailsDTO institutionDTO) throws ResourceNotFoundException;
    InstitutionDetailsDTO getByInstitution(Long id) throws ResourceNotFoundException;
    void deleteInstitution(Long id);
    boolean isDetailsAvailable(Long id);
    List<InstitutionDetailsDTO> getAllInstitutions();
}