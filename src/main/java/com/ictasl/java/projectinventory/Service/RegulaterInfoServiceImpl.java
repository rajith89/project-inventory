package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.InstituteManagementRepository;
import com.ictasl.java.projectinventory.Persistence.dao.RegulaterInfoRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Persistence.entity.RegulaterInfo;
import com.ictasl.java.projectinventory.Security.MyUserDetails;
import com.ictasl.java.projectinventory.Web.dto.ExistingSolutionDto;
import com.ictasl.java.projectinventory.Web.dto.RegulaterInfoDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("regulaterInfoService")
public class RegulaterInfoServiceImpl implements RegulaterInfoService {

    @Autowired
    private RegulaterInfoRepository regulaterInfoRepository;

    @Autowired
    private InstituteManagementRepository institutionRepository;


    @Override
    public List<RegulaterInfoDto> getRegulationByInsId(long id){
        List<RegulaterInfo> dtoList = regulaterInfoRepository.findAllById(id);
        List<RegulaterInfoDto> regDtos = new ArrayList<>();
        for (RegulaterInfo dto : dtoList) {
        if (!dtoList.isEmpty()){
            RegulaterInfoDto regulaterInfoDto = RegulaterInfoDto
                    .builder()
                    .id(dto.getId())
                   .description(dto.getDescription())
                    .regulation(dto.getRegulation())
                    .build();
            regDtos.add(regulaterInfoDto);
            }
        }
        return regDtos;
    }

    @Override
    public RegulaterInfoDto createRegulaterInfo(RegulaterInfoDto existingSolutionDto){

        RegulaterInfo existingSolution = new RegulaterInfo();
        RegulaterInfoDto existingSolutionDtosaved = new RegulaterInfoDto();
        BeanUtils.copyProperties(existingSolutionDto,existingSolution);

        /**
         * TO DO
         */

        MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ManageInstituteEntity> obj = institutionRepository.findById(user.getInstuteId());
        existingSolution.setInstitution(obj.get());

        existingSolution = regulaterInfoRepository.save(existingSolution);
        BeanUtils.copyProperties(existingSolution,existingSolutionDtosaved);
        return existingSolutionDtosaved;
    }

    @Override
    public RegulaterInfoDto updateRegulaterInfo(Long id, RegulaterInfoDto existingSolutionDto) throws ResourceNotFoundException{
        Optional<RegulaterInfo> existingSolutionOptional = regulaterInfoRepository.findById(id);
        if (!existingSolutionOptional.isPresent()) {
            throw new ResourceNotFoundException("Regulater Solution not found.");
        }

        RegulaterInfo existing = existingSolutionOptional.get();
        existing.setDescription(existingSolutionDto.getDescription());
        existing.setRegulation(existingSolutionDto.getRegulation());
        RegulaterInfoDto existingSolutionDtosaved = new RegulaterInfoDto();
        BeanUtils.copyProperties(regulaterInfoRepository.save(existing),existingSolutionDtosaved);
        return existingSolutionDtosaved;
    }

    @Override
    public RegulaterInfoDto getRegulaterInfo(Long id) throws ResourceNotFoundException{
        Optional<RegulaterInfo> existingSolutionOptional = regulaterInfoRepository.findById(id);
        if (!existingSolutionOptional.isPresent()) {
            throw new ResourceNotFoundException("Exisitng Solution not found.");
        }
        RegulaterInfo existing = existingSolutionOptional.get();
        RegulaterInfoDto existingSolutionDto = new RegulaterInfoDto();
        BeanUtils.copyProperties(existing,existingSolutionDto);
        return existingSolutionDto;
    }

    @Override
    public void deleteRegulaterInfo(Long id){
        regulaterInfoRepository.deleteById(id);
    }

}
