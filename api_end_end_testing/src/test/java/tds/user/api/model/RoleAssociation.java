package tds.user.api.model;

/**
 * This class contains the values used by the UserInfo class.
 * UserInfo creates a list of RoleAssociations
 */
public class RoleAssociation {
    private String role;
    private String level;
    private String entityId;
    private String stateAbbreviation;

    public RoleAssociation() {}

    public RoleAssociation(String role, String level, String entityId, String stateAbbreviation) {
        this.role = role;
        this.level = level;
        this.entityId = entityId;
        this .stateAbbreviation = stateAbbreviation;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
