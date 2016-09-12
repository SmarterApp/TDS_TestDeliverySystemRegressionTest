package tds.user.api.model;

/**
 * Created by mjbarger on 9/6/16.
 */
public class RoleAssociation {
    private String role;
    private String level;
    private String entityId;

    public RoleAssociation() {}

    public RoleAssociation(String role, String level, String entityId) {
        this.role = role;
        this.level = level;
        this.entityId = entityId;
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
