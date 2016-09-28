package tds.student.api.model;

/**
 * This class contains the student information needed to delete a batch of students
 */
public class StudentDeleteBatchInfo {
    private String ssid;
    private String stateCode;

    public StudentDeleteBatchInfo() {
    }

    public StudentDeleteBatchInfo(String ssid, String stateCode) {
        this.ssid = ssid;
        this.stateCode = stateCode;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String toString() {
        return (
            "ssid: " + ssid
            + "\nstateCode: " + stateCode
        );
    }
}
