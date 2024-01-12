package com.ictasl.java.projectinventory.Persistence.entity;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Audited
@Entity
@Data
@Table(name = "user" )
public class User extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    @Column(name = "user_name", unique = true)
    @Size(min = 1, max = 25)
    @NotEmpty(message = "Please enter user name")
    private String userName;

    @Column(name = "ins_id")
    @NotNull(message = "Institution required")
    private long insId;

    @Column(name = "ins_name")
    @NotNull(message = "Institution required")
    private String insName;

    @Column(name = "password")
    private String passwordEnc;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private int active;

    @ManyToMany(cascade= CascadeType.MERGE , fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public String toString() {

        return "User id :" + id + " " + userName + " "
                + firstName + " " + lastName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ((Long)id != null ? ((Long)id).hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((Long)this.id == null && ((Long)other.id != null) || ((Long)this.id != null && !((Long)this.id).equals(other.id))) {
            return false;
        }
        return true;
    }


}
