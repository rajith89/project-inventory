package com.ictasl.java.projectinventory.Web.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinistryDto implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	@NotNull
	@NotEmpty(message = "*Please provide a Valid Ministry Name")
	//@NotBlank
	//@ValidField
	private String name;
	
	@Builder.Default
	private String status = "Y";
}
