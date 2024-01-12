package com.ictasl.java.projectinventory.Web.dto;

import com.ictasl.java.projectinventory.Persistence.entity.SubProcurement;
import lombok.Data;

import java.util.List;

@Data
public class SubProcurementDto {

    List<SubProcurement> subProcurements;

}
