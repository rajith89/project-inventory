package com.ictasl.java.projectinventory.Web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExistingSolutionDto implements Serializable {
    private long id;
    private String solutionName;
    private String components;
    private String state;
    private String operability;
    private String improvements;
    private Date createdDate;
    private Integer status;
    private String comment;

}
