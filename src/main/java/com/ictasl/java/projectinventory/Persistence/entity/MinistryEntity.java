package com.ictasl.java.projectinventory.Persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;

@Audited
@Entity
@Table(name = "ministry")
@Data
public class MinistryEntity extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5227044187658730301L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "status")
    private String status;
}
