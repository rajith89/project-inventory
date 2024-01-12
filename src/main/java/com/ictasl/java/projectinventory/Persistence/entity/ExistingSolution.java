package com.ictasl.java.projectinventory.Persistence.entity;

import com.ictasl.java.projectinventory.Web.dto.Status;
import lombok.Data;
import org.hibernate.envers.Audited;
import javax.persistence.*;


@Audited
@Entity
@Table(name = "existing_solution")
@Data
public class ExistingSolution extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exsol_id")
    private long id;

    @Column(name = "solution_name")
    private String solutionName;

    @Column(name = "components")
    private String components;

    @Column(name = "state")
    private String state;

    @Column(name = "operability")
    private String operability;

    @Column(name = "improvements")
    private String improvements;

    @ManyToOne
    @JoinColumn(name="ins_id")
    private ManageInstituteEntity institution;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "comment")
    private String comment;

}
