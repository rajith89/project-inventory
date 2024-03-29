package com.ictasl.java.projectinventory.Web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProjectDto {

    private long id;
    private String solutionName;
    private String description;
    private String budget;
    private List<SubProcurementDto> subProcurementDtos;
    private Boolean isDelete = Boolean.FALSE;
    private Date createdDate;

}
