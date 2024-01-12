package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Audited
@Entity
@Table(name = "existing_project")
@Data
public class ExistingProject extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "proj_id")
    private Long id;

    @Column(name = "solution_name")
    private String solutionName;

    @Column(name = "description")
    private String description;

    @Column(name = "budget")
    private String budget;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exproj_id")
    private List<SubProcurement> subProcurements = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="exsol_id")
    private ExistingSolution existingSolution;

    @ManyToOne
    @JoinColumn(name="ins_id")
    private ManageInstituteEntity institution;

    public ExistingProject() {
        this.existingSolution = new ExistingSolution();
    }
}
