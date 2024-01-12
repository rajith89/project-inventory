package com.ictasl.java.projectinventory.Persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Entity
@Table(name = "it_unit")
@Data
public class ItUnitInfomationEntity extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "director_name")
	private String nameOfDirector;

	private String title;

	private String address;

	private String email;

	@Column(name = "tel_no") 
	private String telNo;

	@Column(name = "staff_strength")
	private String staffStrength;
	
	/*@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="org_id")
    private ManageInstituteEntity institution;*/
	
	
	@OneToOne( orphanRemoval = true)
    @JoinColumn(name = "org_id", referencedColumnName = "id")
    private ManageInstituteEntity institution;

	@OneToMany(cascade =  CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "it_unit")
	private List<ItTeamClassificationEntity> itTeamClassificationList = new ArrayList<ItTeamClassificationEntity>();

}