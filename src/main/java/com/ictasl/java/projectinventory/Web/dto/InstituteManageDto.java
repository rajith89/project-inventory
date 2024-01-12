package com.ictasl.java.projectinventory.Web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstituteManageDto implements Serializable  {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private long ministryId;
	
	@NotNull
	@NotEmpty(message = "*Please provide a Valid Institution Name")
	//@ValidField
	private String name;
	private String ministryName;
	
	@Builder.Default
	private String status = "Y";
}
