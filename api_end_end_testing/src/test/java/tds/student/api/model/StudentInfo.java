package tds.student.api.model;

/**
 * This class contains the student information needed to create, update and delete a new student
 */
public class StudentInfo {
    private String ssid;
    private String stateAbbreviation;
    private String institutionIdentifier;
    private String districtIdentifier;
    private String firstName;
    private String lastName;
    private String middleName;
    private String birthDate;
    private String externalSsid;
    private String gradeLevelWhenAssessed;
    private String sex;
    private boolean hispanicOrLatino;
    private boolean americanIndianOrAlaskaNative;
    private boolean asian;
    private boolean blackOrAfricanAmerican;
    private boolean white;
    private boolean nativeHawaiianOrPacificIsland;
    private boolean twoOrMoreRaces;
    private boolean iDEAIndicator;
    private boolean lepStatus;
    private boolean section504Status;
    private boolean disadvantageStatus;
    private String languageCode;
    private boolean migrantStatus;
    private String firstEntryDateIntoUsSchool;
    private String lepEntryDate;
    private String lepExitDate;
    private String title3ProgramType;
    private String primaryDisabilityType;
    private int elpLevel;

    public StudentInfo() {}

    public StudentInfo(String ssid,
                       String stateAbbreviation,
                       String institutionIdentifier,
                       String districtIdentifier,
                       String firstName,
                       String lastName,
                       String middleName,
                       String birthDate,
                       String externalSsid,
                       String gradeLevelWhenAssessed,
                       String sex,
                       boolean hispanicOrLatino,
                       boolean americanIndianOrAlaskaNative,
                       boolean asian,
                       boolean blackOrAfricanAmerican,
                       boolean white,
                       boolean nativeHawaiianOrPacificIsland,
                       boolean twoOrMoreRaces,
                       boolean iDEAIndicator,
                       boolean lepStatus,
                       boolean section504Status,
                       boolean disadvantageStatus,
                       String languageCode,
                       boolean migrantStatus,
                       String firstEntryDateIntoUsSchool,
                       String lepEntryDate,
                       String lepExitDate,
                       String title3ProgramType,
                       String primaryDisabilityType,
                       int elpLevel) {

        this.ssid = ssid;
        this.stateAbbreviation = stateAbbreviation;
        this.institutionIdentifier = institutionIdentifier;
        this.districtIdentifier = districtIdentifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.externalSsid = externalSsid;
        this.gradeLevelWhenAssessed = gradeLevelWhenAssessed;
        this.sex = sex;
        this.hispanicOrLatino = hispanicOrLatino;
        this.americanIndianOrAlaskaNative = americanIndianOrAlaskaNative;
        this.asian = asian;
        this.blackOrAfricanAmerican = blackOrAfricanAmerican;
        this.white = white;
        this.nativeHawaiianOrPacificIsland = nativeHawaiianOrPacificIsland;
        this.twoOrMoreRaces = twoOrMoreRaces;
        this.iDEAIndicator = iDEAIndicator;
        this.lepStatus = lepStatus;
        this.section504Status = section504Status;
        this.disadvantageStatus = disadvantageStatus;
        this.languageCode = languageCode;
        this.migrantStatus = migrantStatus;
        this.firstEntryDateIntoUsSchool = firstEntryDateIntoUsSchool;
        this.lepEntryDate = lepEntryDate;
        this.lepExitDate = lepExitDate;
        this.title3ProgramType = title3ProgramType;
        this.primaryDisabilityType = primaryDisabilityType;
        this.elpLevel = elpLevel;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getInstitutionIdentifier() {
        return institutionIdentifier;
    }

    public void setInstitutionIdentifier(String institutionIdentifier) {
        this.institutionIdentifier = institutionIdentifier;
    }

    public String getDistrictIdentifier() {
        return districtIdentifier;
    }

    public void setDistrictIdentifier(String districtIdentifier) {
        this.districtIdentifier = districtIdentifier;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getExternalSsid() {
        return externalSsid;
    }

    public void setExternalSsid(String externalSsid) {
        this.externalSsid = externalSsid;
    }

    public String getGradeLevelWhenAssessed() {
        return gradeLevelWhenAssessed;
    }

    public void setGradeLevelWhenAssessed(String gradeLevelWhenAssessed) {
        this.gradeLevelWhenAssessed = gradeLevelWhenAssessed;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isHispanicOrLatino() {
        return hispanicOrLatino;
    }

    public void setHispanicOrLatino(boolean hispanicOrLatino) {
        this.hispanicOrLatino = hispanicOrLatino;
    }

    public boolean isAmericanIndianOrAlaskaNative() {
        return americanIndianOrAlaskaNative;
    }

    public void setAmericanIndianOrAlaskaNative(boolean americanIndianOrAlaskaNative) {
        this.americanIndianOrAlaskaNative = americanIndianOrAlaskaNative;
    }

    public boolean isAsian() {
        return asian;
    }

    public void setAsian(boolean asian) {
        this.asian = asian;
    }

    public boolean isBlackOrAfricanAmerican() {
        return blackOrAfricanAmerican;
    }

    public void setBlackOrAfricanAmerican(boolean blackOrAfricanAmerican) {
        this.blackOrAfricanAmerican = blackOrAfricanAmerican;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean isNativeHawaiianOrPacificIsland() {
        return nativeHawaiianOrPacificIsland;
    }

    public void setNativeHawaiianOrPacificIsland(boolean nativeHawaiianOrPacificIsland) {
        this.nativeHawaiianOrPacificIsland = nativeHawaiianOrPacificIsland;
    }

    public boolean isTwoOrMoreRaces() {
        return twoOrMoreRaces;
    }

    public void setTwoOrMoreRaces(boolean twoOrMoreRaces) {
        this.twoOrMoreRaces = twoOrMoreRaces;
    }

    public boolean isiDEAIndicator() {
        return iDEAIndicator;
    }

    public void setiDEAIndicator(boolean iDEAIndicator) {
        this.iDEAIndicator = iDEAIndicator;
    }

    public boolean isLepStatus() {
        return lepStatus;
    }

    public void setLepStatus(boolean lepStatus) {
        this.lepStatus = lepStatus;
    }

    public boolean isSection504Status() {
        return section504Status;
    }

    public void setSection504Status(boolean section504Status) {
        this.section504Status = section504Status;
    }

    public boolean isDisadvantageStatus() {
        return disadvantageStatus;
    }

    public void setDisadvantageStatus(boolean disadvantageStatus) {
        this.disadvantageStatus = disadvantageStatus;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isMigrantStatus() {
        return migrantStatus;
    }

    public void setMigrantStatus(boolean migrantStatus) {
        this.migrantStatus = migrantStatus;
    }

    public String getFirstEntryDateIntoUsSchool() {
        return firstEntryDateIntoUsSchool;
    }

    public void setFirstEntryDateIntoUsSchool(String firstEntryDateIntoUsSchool) {
        this.firstEntryDateIntoUsSchool = firstEntryDateIntoUsSchool;
    }

    public String getLepEntryDate() {
        return lepEntryDate;
    }

    public void setLepEntryDate(String lepEntryDate) {
        this.lepEntryDate = lepEntryDate;
    }

    public String getLepExitDate() {
        return lepExitDate;
    }

    public void setLepExitDate(String lepExitDate) {
        this.lepExitDate = lepExitDate;
    }

    public String getTitle3ProgramType() {
        return title3ProgramType;
    }

    public void setTitle3ProgramType(String title3ProgramType) {
        this.title3ProgramType = title3ProgramType;
    }

    public String getPrimaryDisabilityType() {
        return primaryDisabilityType;
    }

    public void setPrimaryDisabilityType(String primaryDisabilityType) {
        this.primaryDisabilityType = primaryDisabilityType;
    }

    public int getElpLevel() {
        return elpLevel;
    }

    public void setElpLevel(int elpLevel) {
        this.elpLevel = elpLevel;
    }

    public String toString() {
        return (
            "ssid: " + ssid
            + "\nstateAbbreviation: " + stateAbbreviation
            + "\ninstitutionIdentiier: " + institutionIdentifier
            + "\ndistrictIdentifier: " + districtIdentifier
            + "\nfirstName: " + firstName
            + "\nlastName: " + lastName
            + "\nmiddleName: " + middleName
            + "\nbirthDate: " + birthDate
            + "\nexternalSsid: " + externalSsid
            + "\ngradeLevelWhenAssessed: " + gradeLevelWhenAssessed
            + "\nsex: " + sex
            + "\nhispanicOrLatino: " + hispanicOrLatino
            + "\namericanIndianOrAlaskaNative: " +  americanIndianOrAlaskaNative
            + "\nasian: " + asian
            + "\nblackOrAfricanAmerican: " + blackOrAfricanAmerican
            + "\nwhite: " + white
            + "\nnativeHawaiianOrPacificIsland: " + nativeHawaiianOrPacificIsland
            + "\ntwoOrMoreRaces: " + twoOrMoreRaces
            + "\niDEAIndicator: " + iDEAIndicator
            + "\nlepStatus: " + lepStatus
            + "\nsection504Status: " + section504Status
            + "\ndisadvantageStatus: " + disadvantageStatus
            + "\nlanguageCode: " + languageCode
            + "\nmigrantStatus: " + migrantStatus
            + "\nfirstEntryDateIntoUsSchool: " + firstEntryDateIntoUsSchool
            + "\nlepEntryDate: " + lepEntryDate
            + "\nlepExitDate: " + lepExitDate
            + "\ntitle3ProgramType: " + title3ProgramType
            + "\nprimaryDisabilityType: " + primaryDisabilityType
            + "\nelpLevel: " + elpLevel
        );
    }
}
