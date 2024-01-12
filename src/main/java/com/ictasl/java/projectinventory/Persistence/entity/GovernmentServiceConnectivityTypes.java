package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Audited
@Entity
@Table(name = "gs_conn_type")
@Data
public class GovernmentServiceConnectivityTypes extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "lgn")
    private String lgn;

    @Column(name = "adsl")
    private String adsl;

    @Column(name = "dongle")
    private String dongle;

}
