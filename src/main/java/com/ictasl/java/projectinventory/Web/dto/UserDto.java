package com.ictasl.java.projectinventory.Web.dto;


import com.ictasl.java.projectinventory.Validator.PasswordMatches;
import com.ictasl.java.projectinventory.Validator.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@PasswordMatches
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private long id;

    private String firstName;
    private String lastName;

    @ValidPassword
    @Size(min = 8)
    private String password;

    @NotNull
    @Size(min = 8)
    private String matchingPassword;

    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "*Please provide a Valid User Name")
    private String userName;

//    @NotNull
//    @Size(min = 1)
//    private String insName;

    private long insId;

    private String insName;

    private boolean isUsing2FA;
    private Integer roleId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean isUsing2FA) {
        this.isUsing2FA = isUsing2FA;
    }

//    public String getInsName() {
//        return insName;
//    }

    public long getInsId() {
        return insId;
    }

    public void setInsId(long insId) {
        this.insId = insId;
    }

//    public void setInsName(String insName) {
//        this.insName = insName;
//    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [firstName=").append(firstName).append(", lastName=").append(lastName).append(", password=").append(password).append(", matchingPassword=").append(matchingPassword).append(", userName=").append(userName).append(", isUsing2FA=")
                .append(isUsing2FA).append(", role=").append(roleId).append("]");
        return builder.toString();
    }

}
