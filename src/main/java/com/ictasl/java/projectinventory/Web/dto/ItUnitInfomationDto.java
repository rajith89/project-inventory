package com.ictasl.java.projectinventory.Web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItUnitInfomationDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String nameOfDirector;
	private String title;
	private String address;
	private String email;
	private String telNo;
	private String staffStrength;
	private List<ItTeamClassificationDto> itTeamClassificationList = new ArrayList<ItTeamClassificationDto>();

}
