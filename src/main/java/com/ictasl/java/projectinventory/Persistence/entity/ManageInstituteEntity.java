package com.ictasl.java.projectinventory.Persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Entity
@Table(name = "institution")
@Data
public class ManageInstituteEntity extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "ministry_id")
    private MinistryEntity ministryId;
    
    /*@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
    private User user;*/

//    @OneToOne(mappedBy = "instituteId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
//    private InstitutionDetails detailId;

    @Column(name = "status")
    private String status;
    
}

