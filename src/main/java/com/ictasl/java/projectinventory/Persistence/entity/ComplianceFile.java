package com.ictasl.java.projectinventory.Persistence.entity;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "compliance_file")
public class ComplianceFile extends BaseModel{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "org_id")
	private long orgId;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "name")
	private String name;

	@Column(name = "path")
	private String path;

	public ComplianceFile() {
	}

	public ComplianceFile(long id, long orgId, long userId, String name, String path) {
		super();
		this.id = id;
		this.orgId = orgId;
		this.userId = userId;
		this.name = name;
		this.path = path ;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ComplianceFile [id=" + id + ", orgId=" + orgId + ", userId=" + userId + ", name=" + name + "]";
	}

}
