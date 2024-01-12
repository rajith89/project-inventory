package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.ExistingSolutionRepository;
import com.ictasl.java.projectinventory.Persistence.dao.InstituteManagementRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Security.MyUserDetails;
import com.ictasl.java.projectinventory.Web.dto.ExistingSolutionDto;
import com.ictasl.java.projectinventory.Web.dto.ReviewRequestDto;
import com.ictasl.java.projectinventory.Web.dto.Status;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("existingSolutionService")
public class ExistingSolutionServiceImpl implements ExistingSolutionService {

    @Autowired
    private ExistingSolutionRepository existingSolutionRepository;

    @Autowired
    private InstituteManagementRepository institutionRepository;


    @Override
    public List<ExistingSolutionDto> getAllSolutions(long id){

        List<ExistingSolution> solutionList = existingSolutionRepository.findAllById(id);
        List<ExistingSolutionDto> solutionDtos = new ArrayList<>();
        for (ExistingSolution solution : solutionList) {
        if (!solutionList.isEmpty()){
            ExistingSolutionDto existingSolutionDto = ExistingSolutionDto
                    .builder()
                    .id(solution.getId())
                    .createdDate(solution.getCreatedDate())
                    .solutionName(solution.getSolutionName())
                    .components(solution.getComponents())
                    .operability(solution.getOperability())
                    .improvements(solution.getImprovements())
                    .state(solution.getState())
                    .status(solution.getStatus().getStatusVal())
                    .comment(solution.getComment())
                    .build();
            solutionDtos.add(existingSolutionDto);
            }
        }
        return solutionDtos;
    }

    @Override
    public List<ExistingSolution> getAllExistingSolutions(long id){

        return existingSolutionRepository.findAllById(id);
    }



    @Override
    public ExistingSolutionDto createExistingSolution(ExistingSolutionDto existingSolutionDto){

        ExistingSolution existingSolution = new ExistingSolution();
        ExistingSolutionDto existingSolutionDtosaved = new ExistingSolutionDto();
        BeanUtils.copyProperties(existingSolutionDto,existingSolution);

        /**
         * TO DO
         */

        MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ManageInstituteEntity> obj = institutionRepository.findById(user.getInstuteId());
        existingSolution.setInstitution(obj.get());
        existingSolution.setStatus(Status.Pending);
        existingSolution = existingSolutionRepository.save(existingSolution);
        BeanUtils.copyProperties(existingSolution,existingSolutionDtosaved);
        return existingSolutionDtosaved;
    }

    @Override
    public ExistingSolutionDto updateExistingSolution(Long id, ExistingSolutionDto existingSolutionDto) throws ResourceNotFoundException{
        Optional<ExistingSolution> existingSolutionOptional = existingSolutionRepository.findById(id);
        if (!existingSolutionOptional.isPresent()) {
            throw new ResourceNotFoundException("Exisitng Solution not found.");
        }

        ExistingSolution existing = existingSolutionOptional.get();
        existing.setSolutionName(existingSolutionDto.getSolutionName());
        existing.setComponents(existingSolutionDto.getComponents());
        existing.setImprovements(existingSolutionDto.getImprovements());
        existing.setOperability(existingSolutionDto.getOperability());
        existing.setState(existingSolutionDto.getState());
        ExistingSolutionDto existingSolutionDtosaved = new ExistingSolutionDto();
        BeanUtils.copyProperties(existingSolutionRepository.save(existing),existingSolutionDtosaved);
        return existingSolutionDtosaved;
    }

    @Override
    public ExistingSolutionDto getExistingSolution(Long id) throws ResourceNotFoundException{
        Optional<ExistingSolution> existingSolutionOptional = existingSolutionRepository.findById(id);
        if (!existingSolutionOptional.isPresent()) {
            throw new ResourceNotFoundException("Exisitng Solution not found.");
        }
        ExistingSolution existing = existingSolutionOptional.get();
        ExistingSolutionDto existingSolutionDto = new ExistingSolutionDto();
        BeanUtils.copyProperties(existing,existingSolutionDto);
        return existingSolutionDto;
    }

    @Override
    public void deleteExistingSolution(Long id){
        existingSolutionRepository.deleteById(id);
    }

    public ExistingSolutionDto reviewExistingSolutoin(ReviewRequestDto reviewRequestDto)throws ResourceNotFoundException{
        Optional<ExistingSolution> existingSolutionOptional = existingSolutionRepository.findById(reviewRequestDto.getId());
        if (!existingSolutionOptional.isPresent()) {
            throw new ResourceNotFoundException("Exisitng Solution not found.");
        }
        ExistingSolution existing = existingSolutionOptional.get();
        existing.setComment(reviewRequestDto.getUserComment());
        if(reviewRequestDto.getUserAction() == (Status.Recommended.getStatusVal())){
            existing.setStatus(Status.Recommended);
        }else if(reviewRequestDto.getUserAction() == Status.Reviewing.getStatusVal()){
            existing.setStatus(Status.Reviewing);
        }
        ExistingSolutionDto existingSolutionDto = new ExistingSolutionDto();
        BeanUtils.copyProperties(existingSolutionRepository.save(existing),existingSolutionDto);
        return existingSolutionDto;
    }

}
