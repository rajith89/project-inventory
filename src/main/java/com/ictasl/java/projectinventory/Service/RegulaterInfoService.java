package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Web.dto.RegulaterInfoDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;

import java.util.List;

public interface RegulaterInfoService {
    List<RegulaterInfoDto> getRegulationByInsId(long id);

    RegulaterInfoDto createRegulaterInfo(RegulaterInfoDto existingSolutionDto);

    RegulaterInfoDto updateRegulaterInfo(Long id, RegulaterInfoDto existingSolutionDto) throws ResourceNotFoundException;

    RegulaterInfoDto getRegulaterInfo(Long id) throws ResourceNotFoundException;

    void deleteRegulaterInfo(Long id);
}
