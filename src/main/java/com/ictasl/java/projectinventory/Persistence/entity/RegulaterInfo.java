package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "regulater_info")
@Data
public class RegulaterInfo extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "regulation_id")
    private Long id;

    @Column(name = "regulation")
    private String regulation;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="ins_id")
    private ManageInstituteEntity institution;
}
