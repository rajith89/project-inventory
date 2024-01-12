package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.InstitutionDetailsRepository;
import com.ictasl.java.projectinventory.Persistence.entity.InstitutionDetails;
import com.ictasl.java.projectinventory.Web.dto.InstitutionDetailsDTO;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("institutionService")
public class InstitutionServiceImpl implements InstitutionService {

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;
    @Autowired
    private HttpServletRequest request;

    @Override
    public InstitutionDetailsDTO createInstitution(InstitutionDetailsDTO institutionDto) {
        InstitutionDetails institution = new InstitutionDetails();
        InstitutionDetailsDTO institutionDTO = new InstitutionDetailsDTO();
        BeanUtils.copyProperties(institutionDto,institution);
        institution = institutionDetailsRepository.save(institution);
        BeanUtils.copyProperties(institution,institutionDTO);
        return institutionDTO;
    }

    @Override
    public InstitutionDetailsDTO updateInstitution(Long id, InstitutionDetailsDTO institutionDTO) throws ResourceNotFoundException {
        Optional<InstitutionDetails> institutionOptional = institutionDetailsRepository.findById(id);
        if (!institutionOptional.isPresent()) {
            throw new ResourceNotFoundException("Exisitng Solution not found.");
        }

        InstitutionDetails insDetails = institutionOptional.get();
//        insDetails.setInstitutionName(institutionDTO.getInstitutionName());

        insDetails.setNameHeadOfTheDept(institutionDTO.getNameHeadOfTheDept());
        insDetails.setTitleHeadOfTheDept(institutionDTO.getTitleHeadOfTheDept());
        insDetails.setAddressHeadOfTheDept(institutionDTO.getAddressHeadOfTheDept());
        insDetails.setEmailHeadOfTheDept(institutionDTO.getEmailHeadOfTheDept());
        insDetails.setPhoneHeadOfTheDept(institutionDTO.getPhoneHeadOfTheDept());

        insDetails.setNameDirector(institutionDTO.getNameDirector());
        insDetails.setTitleDirector(institutionDTO.getTitleDirector());
        insDetails.setAddressDirector(institutionDTO.getAddressDirector());
        insDetails.setEmailDirector(institutionDTO.getEmailDirector());
        insDetails.setPhoneDirector(institutionDTO.getPhoneDirector());

        insDetails.setNamePcp(institutionDTO.getNamePcp());
        insDetails.setTitlePcp(institutionDTO.getTitlePcp());
        insDetails.setAddressPcp(institutionDTO.getAddressPcp());
        insDetails.setEmailPcp(institutionDTO.getEmailPcp());
        insDetails.setPhonePcp(institutionDTO.getPhonePcp());

        InstitutionDetailsDTO institutionDTOSave = new InstitutionDetailsDTO();
        BeanUtils.copyProperties(institutionDetailsRepository.save(insDetails),institutionDTOSave);
        return institutionDTOSave;
    }

    @Override
    public InstitutionDetailsDTO getByInstitution(Long id) throws ResourceNotFoundException {

        Optional<InstitutionDetails> institutionOptional = institutionDetailsRepository.findByInstituteId(id);
        if (!institutionOptional.isPresent()) {
            InstitutionDetailsDTO institutionDTO = new InstitutionDetailsDTO();
            institutionDTO.setInstituteId(id);
            return institutionDTO;
        }
        InstitutionDetails institution = institutionOptional.get();
        InstitutionDetailsDTO institutionDTO = new InstitutionDetailsDTO();
        BeanUtils.copyProperties(institution,institutionDTO);
        return institutionDTO;
    }

    @Override
    public void deleteInstitution(Long id) {

    }

    @Override
    public boolean isDetailsAvailable(Long id) {
        Optional<InstitutionDetails> institutionOptional = institutionDetailsRepository.findById(id);
        if (institutionOptional.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public List<InstitutionDetailsDTO> getAllInstitutions() {
        List<InstitutionDetails> institutions = institutionDetailsRepository.findAll();
        List<InstitutionDetailsDTO> institutionDto = new ArrayList<>();
        for (InstitutionDetails institution : institutions) {
            if (!institutions.isEmpty()){
                InstitutionDetailsDTO institutionDTO = InstitutionDetailsDTO
                        .builder()
                        .id(institution.getId())
                        .build();
                institutionDto.add(institutionDTO);
            }
        }
        return institutionDto;
    }


}