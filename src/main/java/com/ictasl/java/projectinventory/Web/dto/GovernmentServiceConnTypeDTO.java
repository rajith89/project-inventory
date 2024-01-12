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
public class GovernmentServiceConnTypeDTO {

    private long id;
    private String LGN;
    private String ADSL;
    private String dongle;

}
