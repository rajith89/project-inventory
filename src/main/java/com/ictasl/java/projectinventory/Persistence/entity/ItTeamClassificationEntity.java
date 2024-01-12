package com.ictasl.java.projectinventory.Persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Entity
@Table(name = "it_team_classification")
@Data
public class ItTeamClassificationEntity extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "staff_category")
	private String staffCategory;
	
	@Column(name = "edu_qualification")
	private String eduQualification;
	
	@Column(name = "prof_qualification")
	private String profQualification;

}
