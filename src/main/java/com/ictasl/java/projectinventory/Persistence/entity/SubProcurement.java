package com.ictasl.java.projectinventory.Persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ictasl.java.projectinventory.Web.dto.Status;
import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

@Audited
@Entity
@Table(name = "sub_procurement")
@Data
public class SubProcurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sub_procu_id")
    private Long id;

    @Column(name = "solution_name")
    private String solutionName;

    @Column(name = "description")
    private String description;

    @Column(name = "budget")
    private String budget;

    @Column(name = "state")
    private String state;

    @Column(name = "proc_type")
    private String type;

    @Column(name = "proc_entity")
    private String entity;



    /**
     * To Do
     *  use datetime
     */

//    @Column(name = "impl_completion_date")
//    @JsonFormat(pattern="yyyy-MM-dd")
//    private Date implCompletionDate;

//    @Column(name = "pro_completion_date")
//    @JsonFormat(pattern="yyyy-MM-dd")
//    private Date proCompletionDate;

    @Column(name = "pro_completion_date")
    private String proCompletionDate;

    @Column(name = "impl_completion_date")
    private String implCompletionDate;

    @Column(name = "supplier_name")
    private String supplierName;

    private Integer sequenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "comment")
    private String comment;

//    @ManyToOne
//    @JoinColumn(name="existing_proj_id")
//    private ExistingProject existingProject;

}
