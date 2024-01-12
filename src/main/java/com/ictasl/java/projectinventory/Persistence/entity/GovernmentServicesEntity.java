package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Audited
@Entity
@Table(name = "government_services")
@Data
public class GovernmentServicesEntity extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Valid
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "connection_type", referencedColumnName = "id")
    private GovernmentServiceConnectivityTypes connectionTypes;

    @Column(name = "lgn")
    private String lgn;

    @Column(name = "c_ni")
    private String Cni;

    @Column(name = "hosting")
    private String hosting;

    @Column(name = "lgc")
    private String lgc;

    @Column(name = "h_ni")
    private String Hni;

    @Column(name = "opsname")
    private String OpsName;

    @Column(name = "opsurl")
    private String OpsUrl;

    @Column(name = "opsstatus")
    private String OpsStatus;

    @Column(name = "smsprovider")
    private String smsProvider;

    @Column(name = "smsprovidername")
    private String smsProviderName;

    @Column(name = "smsStatus")
    private String smsStatus;

    @ManyToOne
    @JoinColumn(name = "ins_id")
    private ManageInstituteEntity institute;
    
}
