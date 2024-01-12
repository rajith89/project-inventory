package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Audited
@Entity
@Table(name = "new_project")
@Data
public class NewProject extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "new_proj_id")
    private Long id;

    @Column(name = "solution_name")
    private String solutionName;

    @Column(name = "description")
    private String description;

    @Column(name = "npdApproval")
    private String npdApproval;

    @Column(name = "budget")
    private String budget;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "new_proj_id")
    private List<SubProcurement> subProcurements = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="ins_id")
    private ManageInstituteEntity institution;

}
