package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.entity.SubProcurement;
import com.ictasl.java.projectinventory.Web.dto.ReviewRequestDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;

import java.util.List;

public interface SubProcurementService {
    List<SubProcurement> getSubProcurementByExistingProjectId(Long projectId);
    List<SubProcurement> getSubProcurementByNewProjectId(Long projectId);
    SubProcurement reviewSubProcurement(ReviewRequestDto reviewRequestDto)throws ResourceNotFoundException;
}
