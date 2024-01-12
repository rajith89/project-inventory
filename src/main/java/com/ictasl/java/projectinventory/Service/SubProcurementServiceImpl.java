package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.SubProcurementRepository;
import com.ictasl.java.projectinventory.Persistence.entity.SubProcurement;
import com.ictasl.java.projectinventory.Web.dto.ReviewRequestDto;
import com.ictasl.java.projectinventory.Web.dto.Status;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("subProcurementService")
public class SubProcurementServiceImpl implements SubProcurementService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private SubProcurementRepository subProcurementRepository;

    @Override
    public List<SubProcurement> getSubProcurementByExistingProjectId(Long projectId){
        return subProcurementRepository.getSubProcurementByExistingProjectId(projectId);
    }

    @Override
    public List<SubProcurement> getSubProcurementByNewProjectId(Long projectId){
        return subProcurementRepository.getSubProcurementByNewProjectId(projectId);
    }

    @Override
    public SubProcurement reviewSubProcurement(ReviewRequestDto reviewRequestDto)throws ResourceNotFoundException {
        Optional<SubProcurement> SubProcurementOptional = subProcurementRepository.findById(reviewRequestDto.getId());
        if (!SubProcurementOptional.isPresent()) {
            throw new ResourceNotFoundException("SubProcurement not found.");
        }
        SubProcurement SubProcurementObj = SubProcurementOptional.get();
        SubProcurementObj.setComment(reviewRequestDto.getUserComment());
        if(reviewRequestDto.getUserAction() == (Status.Recommended.getStatusVal())){
            SubProcurementObj.setStatus(Status.Recommended);
        }else if(reviewRequestDto.getUserAction() == Status.Reviewing.getStatusVal()){
            SubProcurementObj.setStatus(Status.Reviewing);
        }
        return subProcurementRepository.save(SubProcurementObj);
    }



}
