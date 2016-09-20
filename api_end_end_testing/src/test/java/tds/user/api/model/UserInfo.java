package tds.user.api.model;

import java.util.List;

/**
 * Class to contain the user information
 */
public class UserInfo {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<RoleAssociation> roleAssociations;

    public UserInfo() {}

    public UserInfo(String email, String firstName, String lastName, String phoneNumber,
                    List<RoleAssociation> roleAssociations) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.roleAssociations = roleAssociations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<RoleAssociation> getRoleAssociations() {
        return roleAssociations;
    }

    public void setRoleAssociations(List<RoleAssociation> roleAssociationList) {
        this.roleAssociations = roleAssociationList;
    }
}
