package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Audited
@Entity
@Table(name = "feedback")
@Data
public class FeedBack extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "content", columnDefinition="TEXT")
    private String content;

    @Column(name = "date_time")
    private String dateTime;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name="ins_id")
    private ManageInstituteEntity institution;

}
