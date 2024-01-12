package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import com.ictasl.java.projectinventory.Web.dto.ExistingSolutionDto;
import com.ictasl.java.projectinventory.Web.dto.ReviewRequestDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;

import java.util.List;

public interface ExistingSolutionService {
    List<ExistingSolutionDto> getAllSolutions(long id);

    List<ExistingSolution> getAllExistingSolutions(long id);

    ExistingSolutionDto createExistingSolution(ExistingSolutionDto existingSolutionDto);

    ExistingSolutionDto updateExistingSolution(Long id, ExistingSolutionDto existingSolutionDto) throws ResourceNotFoundException;

    ExistingSolutionDto getExistingSolution(Long id) throws ResourceNotFoundException;

    void deleteExistingSolution(Long id);

    ExistingSolutionDto reviewExistingSolutoin(ReviewRequestDto reviewRequestDto)throws ResourceNotFoundException;
}
