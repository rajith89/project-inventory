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
public class GovernmentServicesDTO implements Serializable  {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private long insId;

	private GovernmentServiceConnTypeDTO connectivityType;
	private String lgn;
	private String cni;

	private String hosting;
	private String lgc;
	private String hni;

	private String opsName;
	private String opsUrl;
	private String opsStatus;

	private String smsProvider;
	private String smsProviderName;
	private String smsStatus;

	private String insName;


}
