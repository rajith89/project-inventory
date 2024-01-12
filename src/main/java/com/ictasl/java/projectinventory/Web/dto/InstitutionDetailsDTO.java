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
public class InstitutionDetailsDTO implements Serializable {

    private long id;

    private long InstituteId;

    private String nameHeadOfTheDept;
    private String titleHeadOfTheDept;
    private String addressHeadOfTheDept;
    private String emailHeadOfTheDept;
    private String phoneHeadOfTheDept;

    private String nameDirector;
    private String titleDirector;
    private String addressDirector;
    private String emailDirector;
    private String phoneDirector;

    private String namePcp;
    private String titlePcp;
    private String addressPcp;
    private String emailPcp;
    private String phonePcp;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("InstitutionDto [InstitutionID=").append(getInstituteId()).append("]");
        return builder.toString();
    }

}
