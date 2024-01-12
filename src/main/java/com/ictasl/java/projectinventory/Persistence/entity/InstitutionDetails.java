package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "institution_details")
@Data
public class InstitutionDetails extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "institute_id", referencedColumnName = "id")
    //this has to be forignkey
    @Column(name = "ins_id")
    private long instituteId;

    @Column(name = "ins_name_head")
    private String nameHeadOfTheDept;

    @Column(name = "ins_title_head")
    private String titleHeadOfTheDept;

    @Column(name = "ins_address_head")
    private String addressHeadOfTheDept;

    @Column(name = "ins_email_head")
    private String emailHeadOfTheDept;

    @Column(name = "ins_phone_head")
    private String phoneHeadOfTheDept;

    @Column(name = "ins_name_director")
    private String nameDirector;

    @Column(name = "ins_title_director")
    private String titleDirector;

    @Column(name = "ins_address_director")
    private String addressDirector;

    @Column(name = "ins_email_director")
    private String emailDirector;

    @Column(name = "ins_phone_director")
    private String phoneDirector;

    @Column(name = "ins_name_pcp")
    private String namePcp;

    @Column(name = "ins_title_pcp")
    private String titlePcp;

    @Column(name = "ins_address_pcp")
    private String addressPcp;

    @Column(name = "ins_email_pcp")
    private String emailPcp;

    @Column(name = "ins_phone_pcp")
    private String phonePcp;
}