package com.ictasl.java.projectinventory.Web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegulaterInfoDto implements Serializable {
    private long id;
    private String regulation;
    private String description;


}
