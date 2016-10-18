package tds.student.api.tests;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.testng.annotations.Test;
import tds.base.BaseUri;
import tds.student.api.model.StudentDeleteBatchInfo;
import tds.student.api.model.StudentInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/*
 * This class tests Student API endpoints that creates, updates and deletes a student
 */
public class StudentApiTests extends BaseUri {
    private final static String BASE_URI = "/rest/external/student/";
    private final static int DEFAULT_BATCH_SIZE = 4;
    private final static String DEFAULT_STATE_ABBREVIATION = "CA";

    /*
     * Test of creating a student, HTTP POST of /api/external/student/{stateCode}, 201 success item created
     * Test of getting a student, HTTP GET of /api/external/student/{stateCode}/{ssid}, 200 success item found
     */
    private StudentInfo createStudent(String stateAbbrev) {
        String ssid = createRandomStudentSsid();

        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setSsid(ssid);
        studentInfo.setStateAbbreviation(stateAbbrev);
        studentInfo.setInstitutionIdentifier("DS9001");
        studentInfo.setDistrictIdentifier("DISTRICT9");
        studentInfo.setFirstName("Amy");
        studentInfo.setLastName("Martin");
        studentInfo.setMiddleName("Leslie");
        studentInfo.setBirthDate("1999-01-01");
        studentInfo.setExternalSsid("e" + ssid);
        studentInfo.setGradeLevelWhenAssessed("04");
        studentInfo.setSex("Male");
        studentInfo.setHispanicOrLatino(false);
        studentInfo.setAmericanIndianOrAlaskaNative(false);
        studentInfo.setAsian(false);
        studentInfo.setBlackOrAfricanAmerican(false);
        studentInfo.setWhite(false);
        studentInfo.setNativeHawaiianOrPacificIsland(false);
        studentInfo.setTwoOrMoreRaces(true);
        studentInfo.setiDEAIndicator(false);
        studentInfo.setLepStatus(false);
        studentInfo.setSection504Status(false);
        studentInfo.setDisadvantageStatus(false);
        studentInfo.setLanguageCode(null);
        studentInfo.setMigrantStatus(false);
        studentInfo.setFirstEntryDateIntoUsSchool("2000-02-01");
        studentInfo.setLepEntryDate(null);
        studentInfo.setLepExitDate(null);
        studentInfo.setTitle3ProgramType(null);
        studentInfo.setPrimaryDisabilityType(null);
        studentInfo.setElpLevel(0);

        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(studentInfo)
            .when()
                .post(BASE_URI + stateAbbrev)
            .then()
                .statusCode(201)
            .extract()
                .header("location");

        // Execute a GET by ssid to validate that the student was created
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(location)
        .then()
            .statusCode(200)
            .body("institutionIdentifier", is(studentInfo.getInstitutionIdentifier()))
            .body("firstName", is(studentInfo.getFirstName()))
            .body("middleName", is(studentInfo.getMiddleName()))
            .body("lastName", is(studentInfo.getLastName()));

        return studentInfo;
    }

    private StudentInfo createStudent() {
        return createStudent(DEFAULT_STATE_ABBREVIATION);
    }

    /*
     * Test of creating batch of students, HTTP POST of /api/external/student/{stateCode}/batch,
     *      202 success item created
     * Test of getting batch of students, HTTP GET of /api/external/student/batch/{batchId},
     *      200 success item found
     * Test of getting single student, HTTP GET of /api/external/student/{stateCode}/{ssid},
     *      200 success item found
     */
    private List<StudentInfo> createBatchOfStudents(int numStudents, String stateAbbrev) {
        List<StudentInfo> students = new ArrayList<StudentInfo>();

        for (int i = 0; i < numStudents; i++) {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setStateAbbreviation(stateAbbrev);
            studentInfo.setInstitutionIdentifier("DS9002");
            studentInfo.setDistrictIdentifier("DISTRICT8");
            studentInfo.setFirstName("Meredith");
            studentInfo.setLastName("Grey");
            studentInfo.setMiddleName("Leslie");
            studentInfo.setGradeLevelWhenAssessed("05");
            studentInfo.setSex("Male");
            studentInfo.setHispanicOrLatino(false);
            studentInfo.setAmericanIndianOrAlaskaNative(false);
            studentInfo.setAsian(false);
            studentInfo.setBlackOrAfricanAmerican(false);
            studentInfo.setWhite(false);
            studentInfo.setNativeHawaiianOrPacificIsland(false);
            studentInfo.setTwoOrMoreRaces(true);
            studentInfo.setiDEAIndicator(false);
            studentInfo.setLepStatus(false);
            studentInfo.setSection504Status(false);
            studentInfo.setDisadvantageStatus(false);
            studentInfo.setLanguageCode(null);
            studentInfo.setMigrantStatus(false);
            studentInfo.setLepEntryDate(null);
            studentInfo.setLepExitDate(null);
            studentInfo.setTitle3ProgramType(null);
            studentInfo.setPrimaryDisabilityType(null);
            studentInfo.setElpLevel(0);

            studentInfo.setSsid(createRandomStudentSsid());
            studentInfo.setExternalSsid("e" + studentInfo.getSsid());
            studentInfo.setBirthDate(createRandomDate(LocalDate.of(2000, 1, 1), LocalDate.of(2005, 1, 1)));
            studentInfo.setFirstEntryDateIntoUsSchool(createRandomDate(LocalDate.of(2005, 6, 1), LocalDate.of(2011, 8, 1)));

            students.add(studentInfo);
        }

        // Execute a batch POST and extract the location header to validate that the
        //    students were created
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(BASE_URI + stateAbbrev + "/" + "batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        waitForBatchToComplete(location);

        // Execute a GET by batch id to validate that all students were created
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(location)
        .then()
            .statusCode(200)
            .body("numSubmitted", is(students.size()))
            .body("numSuccess", is(students.size()));

        // Execute a GET by ssid to validate that each student was created
        for (int i = 0; i < students.size(); i++) {
            given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(authHeader)
            .when()
                .get(BASE_URI + stateAbbrev + "/"  + students.get(i).getSsid())
            .then()
                .statusCode(200)
                .body("ssid", is(students.get(i).getSsid()))
                .body("firstName", is(students.get(i).getFirstName()))
                .body("middleName", is(students.get(i).getMiddleName()))
                .body("lastName", is(students.get(i).getLastName()));
        }

        return students;
    }

    private List<StudentInfo> createBatchOfStudents() {
        return createBatchOfStudents(DEFAULT_BATCH_SIZE, DEFAULT_STATE_ABBREVIATION);
    }

    /*
     *  Test of deleting a student, HTTP DELETE of /api/external/student/{stateCode}/{ssid},
     *      204 success item found and deleted
     *  Test of getting a student, HTTP GET of /api/external/external/student/{stateCode}/{ssid},
     *      404 item not found
     */
    private void deleteSingleStudent(String ssid, String stateAbbrev) {
        // Execute a Delete to remove the student
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
        .when()
            .delete(BASE_URI + stateAbbrev + "/" + ssid)
        .then()
            .statusCode(204);

        // Execute a GET by ssid to validate student is deleted
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(BASE_URI + stateAbbrev + "/" + ssid)
        .then()
            .statusCode(404);
    }

    private void deleteSingleStudent(String ssid) {
        deleteSingleStudent(ssid, DEFAULT_STATE_ABBREVIATION);
    }

    /*
     *  Test of deleting batch of students, HTTP DELETE of /api/external/student/{stateCode}/{ssid},
     *      204 success item found and deleted
     *  Test of getting batch of students, HTTP GET of /api/external/external/student/batch/{batchId},
     *      200 item not found
     *  Test of getting a student, HTTP GET of /api/external/student/{stateCode}/{ssid},
     *      404 not found
     */
    private void deleteBatchOfStudents(List<StudentInfo> students) {

        List<StudentDeleteBatchInfo> deleteStudents = new ArrayList<StudentDeleteBatchInfo>();

        for (int i = 0; i < students.size(); i++) {
            deleteStudents.add(new StudentDeleteBatchInfo(students.get(i).getSsid(), students.get(i).getStateAbbreviation()));
        }

        String stateCode = deleteStudents.get(0).getStateCode();

        // Execute a Delete to remove the batch of students
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(deleteStudents)
            .when()
                .delete(BASE_URI + stateCode + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        waitForBatchToComplete(location);

        // Execute a GET by batch id to validate that all students were deleted
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(location)
        .then()
            .statusCode(200)
            .body("numSubmitted", is(students.size()))
            .body("numSuccess", is(students.size()));

        // Execute a GET by ssid to validate that each student was deleted
        for (int i = 0; i < students.size(); i++) {
            given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(authHeader)
            .when()
                .get(BASE_URI + stateCode + "/" + students.get(i).getSsid())
            .then()
                .statusCode(404);
        }
    }

    /**
     * Queries the batch progress URL and waits for the numProcessed to equal the batch size that was submitted.
     * Control is returned to the test and it will query for the exact details
     * @param batchDetailsUrl Url of the batch progress endpoint, which is returned in the Location header of the batch endpoint
     */
    private void waitForBatchToComplete(String batchDetailsUrl) {
        int attempts = 1;

        while (attempts < 20) {
            Response response =
                given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .header(authHeader)
                .when()
                    .get(batchDetailsUrl)
                .then()
                    .statusCode(200)
                .extract()
                    .response();

            if (response.path("numSubmitted") == response.path("numProcessed")) {
                return;
            }

            attempts++;

            // wait to allow the batch processing to happen
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                // swallow and move on
            }
        }

        fail(String.format("Batch did not complete after %s attempts for %s", attempts, batchDetailsUrl));
    }

    /**
     * Posts a batch of students and waits for it to complete by polling the batch progress
     * @param stateAbbrev
     * @param students
     */
    private void postBatchOfStudents(String stateAbbrev, List<StudentInfo> students) {
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(BASE_URI + stateAbbrev + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        waitForBatchToComplete(location);
    }

    private void validateStudentInfo(StudentInfo studentInfo) {
        // Execute a GET by ssid to verify that the student information has changed
        JsonPath jsonPath = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(BASE_URI + studentInfo.getStateAbbreviation() + "/" + studentInfo.getSsid())
        .then()
            .statusCode(200)
        .extract()
            .jsonPath();

        assertThat(jsonPath.getString("ssid")).isEqualTo(studentInfo.getSsid());
        assertThat(jsonPath.getString("stateAbbreviation")).isEqualTo(studentInfo.getStateAbbreviation());
        assertThat(jsonPath.getString("institutionIdentifier")).isEqualTo(studentInfo.getInstitutionIdentifier());
        assertThat(jsonPath.getString("districtIdentifier")).isEqualTo(studentInfo.getDistrictIdentifier());
        assertThat(jsonPath.getString("firstName")).isEqualTo(studentInfo.getFirstName());
        assertThat(jsonPath.getString("lastName")).isEqualTo(studentInfo.getLastName());
        assertThat(jsonPath.getString("middleName")).isEqualTo(studentInfo.getMiddleName());
        assertThat(jsonPath.getString("birthDate")).isEqualTo(studentInfo.getBirthDate());
        assertThat(jsonPath.getString("externalSsid")).isEqualTo(studentInfo.getExternalSsid());
        assertThat(jsonPath.getString("gradeLevelWhenAssessed")).isEqualTo(studentInfo.getGradeLevelWhenAssessed());
        assertThat(jsonPath.getString("sex")).isEqualTo(studentInfo.getSex());
        assertThat(jsonPath.getBoolean("hispanicOrLatino")).isEqualTo(studentInfo.isHispanicOrLatino());
        assertThat(jsonPath.getBoolean("americanIndianOrAlaskaNative")).isEqualTo(studentInfo.isAmericanIndianOrAlaskaNative());
        assertThat(jsonPath.getBoolean("asian")).isEqualTo(studentInfo.isAsian());
        assertThat(jsonPath.getBoolean("blackOrAfricanAmerican")).isEqualTo(studentInfo.isBlackOrAfricanAmerican());
        assertThat(jsonPath.getBoolean("white")).isEqualTo(studentInfo.isWhite());
        assertThat(jsonPath.getBoolean("nativeHawaiianOrPacificIsland")).isEqualTo(studentInfo.isNativeHawaiianOrPacificIsland());
        assertThat(jsonPath.getBoolean("twoOrMoreRaces")).isEqualTo(studentInfo.isTwoOrMoreRaces());
        assertThat(jsonPath.getBoolean("iDEAIndicator")).isEqualTo(studentInfo.isiDEAIndicator());
        assertThat(jsonPath.getBoolean("lepStatus")).isEqualTo(studentInfo.isLepStatus());
        assertThat(jsonPath.getBoolean("section504Status")).isEqualTo(studentInfo.isSection504Status());
        assertThat(jsonPath.getBoolean("disadvantageStatus")).isEqualTo(studentInfo.isDisadvantageStatus());
        assertThat(jsonPath.getString("languageCode")).isEqualTo(studentInfo.getLanguageCode());
        assertThat(jsonPath.getBoolean("migrantStatus")).isEqualTo(studentInfo.isMigrantStatus());
        assertThat(jsonPath.getBoolean("firstEntryDateIntoUsSchool")).isEqualTo(studentInfo.getFirstEntryDateIntoUsSchool());
        assertThat(jsonPath.getBoolean("lepEntryDate")).isEqualTo(studentInfo.getLepEntryDate());
        assertThat(jsonPath.getBoolean("lepExitDate")).isEqualTo(studentInfo.getLepExitDate());
        assertThat(jsonPath.getBoolean("title3ProgramType")).isEqualTo(studentInfo.getTitle3ProgramType());
        assertThat(jsonPath.getBoolean("primaryDisabilityType")).isEqualTo(studentInfo.getPrimaryDisabilityType());
        assertThat(jsonPath.getInt("elpLevel")).isEqualTo(studentInfo.getElpLevel());
    }

    @Test
    public void shouldCreateDeleteStudent() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Execute a DELETE to delete the student
        deleteSingleStudent(studentInfo.getSsid());
    }

    @Test
    public void shouldCreateUpdateDeleteStudent() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Change some of the data for the student just created
        studentInfo.setStateAbbreviation(DEFAULT_STATE_ABBREVIATION);
        studentInfo.setFirstName("William");
        studentInfo.setLastName("Peters");
        studentInfo.setGradeLevelWhenAssessed("02");
        studentInfo.setHispanicOrLatino(true);
        studentInfo.setTwoOrMoreRaces(false);
        studentInfo.setElpLevel(2);

        // Execute a POST to update the student with new information
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(204)
            .header("location", endsWith(BASE_URI + DEFAULT_STATE_ABBREVIATION + "/" + studentInfo.getSsid()));

        validateStudentInfo(studentInfo);

        // Execute a DELETE to delete the student
        deleteSingleStudent(studentInfo.getSsid());
    }

    @Test
    public void shouldCreateUpdateDeleteBatchOfStudents() {
        List<StudentInfo> students = createBatchOfStudents();

        // Change some of the data for the batch of students just created

        students.get(0).setInstitutionIdentifier("DS9222");
        students.get(0).setMiddleName("Ray");
        students.get(0).setAmericanIndianOrAlaskaNative(true);
        students.get(1).setLastName("Jones");
        students.get(1).setBlackOrAfricanAmerican(true);
        students.get(1).setElpLevel(3);
        students.get(2).setLastName("Nelson");
        students.get(2).setAsian(true);
        students.get(2).setMigrantStatus(true);

        // Execute a POST to update the students with new information
        postBatchOfStudents(DEFAULT_STATE_ABBREVIATION, students);

        // validate that the students have been changed by calling the GET endpoint
        students.forEach(this::validateStudentInfo);

        // Execute a DELETE to delete the batch of students
        deleteBatchOfStudents(students);
    }

    @Test
    public void shouldNotDeleteStudentWithInvalidSsid() {
        // Execute a DELETE to verify that a student with an invalid ssid can't be deleted
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
        .when()
            .delete(BASE_URI + DEFAULT_STATE_ABBREVIATION + "/" + "ABCDEF")
        .then()
            .statusCode(404);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidStateAbbreviation() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid state abbreviation that should only be 2 characters
        studentInfo.setStateAbbreviation("ABC");

        // Execute POST to verify that a student can't be created with an invalid state abbreviation.
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + "ABC")
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithNullStateAbbreviation() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid state abbreviation set to null
        studentInfo.setStateAbbreviation(null);

        // Execute POST to verify that a student can't be created with a null state abbreviation.
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidInstitutionId() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid institution Id set to a blank
        studentInfo.setInstitutionIdentifier("");

        // Execute a POST to verify that a student can't be created with an invalid institution id
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidDistrictId() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid district Id
        studentInfo.setDistrictIdentifier("DistrictAADEFFJJIDDDDWERDEDEFG00293838192922");

        // Execute a POST to verify that a student can't be created with an invalid district id
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.districtIdentifier[0]", equalTo("District Identifier size must be between 1 and 40"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidFirstName() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid first name
        studentInfo.setFirstName("MynameisJoeSmithAndMyMiddleNameisSteven");

        // Execute a POST to verify that a student can't be created with an invalid first name
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.firstName[0]", equalTo("The max size of the FirstName is 35"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLastName() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid last name
        studentInfo.setLastName("MylastnameisBiggalow-Johnson-Richardson");

        // Execute a POST to verify that a student can't be created with an invalid last name
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.lastName[0]", equalTo("The max size of the LastName is 35"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidMiddleName() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid middle name
        studentInfo.setMiddleName("MymiddlenameisWilliam-Peterson-Henderson");

        // Execute a POST to verify that a student can't be created with an invalid middle name
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.middleName[0]", equalTo("The max size of the MiddleName is 35"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidExternalSsid() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid external ssid
        studentInfo.setExternalSsid("");

        // Execute a POST to verify that a student can't be created with an invalid external ssid
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidSex() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid sex
        studentInfo.setSex("");

        // Execute a POST to verify that a student can't be created with an invalid sex
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidGradeLevel() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid grade level
        studentInfo.setGradeLevelWhenAssessed("123456");

        // Execute a POST to verify that a student can't be created with an invalid grade level
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.gradeLevelWhenAssessed[0]", startsWith("Grade level when assessed should be"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLanguageCode() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid language code
        studentInfo.setLanguageCode("ENGL");

        // Execute a POST to verify that a student can't be created with an invalid language code
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.languageCode[0]", startsWith("Language Codes are invalid"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidFirstEntryDate() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid entry date
        studentInfo.setFirstEntryDateIntoUsSchool("1930-01-01");

        // Execute a POST to verify that a student can't be created with an invalid first entry date
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.firstEntryDateIntoUsSchool[0]", startsWith("First Entry Date Into US School should always be after the year of birth date"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLepEntryDate() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid LEP Entry Date
        studentInfo.setLepEntryDate("214-1999-12-01");

        // Execute a POST to verify that a student can't be created with an invalid LEP entry date
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLepExitDate() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid LEP exit date
        studentInfo.setLepExitDate("2099-12-01-99");

        // Execute a POST to verify that a student can't be created with an invalid LEP exit date
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidTitle3ProgType() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid title 3 program type
        studentInfo.setTitle3ProgramType("500");

        // Execute a POST to verify that a student can't be created with an invalid
        //    title 3 program type
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.title3ProgramType[0]", startsWith("TitleIIILanguage Instruction ProgramType in invalid Types"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidPrimDisabilityType() {
        // Create a student
        StudentInfo studentInfo = createStudent();

        // Test an invalid primary disability type
        studentInfo.setPrimaryDisabilityType("3333");

        // Execute a POST to verify that a student can't be created with an invalid
        //    primary disability type
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION)
        .then()
            .statusCode(400)
            .body("messages.primaryDisabilityType[0]", startsWith("PrimaryDisabilityType is invalid Types"));
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidStateAbbrev() {
        int invalidStudentItem = DEFAULT_BATCH_SIZE-1;

        List<StudentInfo> students = createBatchOfStudents();

        // Test an invalid state abbreviation for one of the students
        students.get(invalidStudentItem).setStateAbbreviation("ABCDEF");

        // Execute a batch POST and extract the location header to validate that this
        //    student returned an error
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(BASE_URI + DEFAULT_STATE_ABBREVIATION + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        waitForBatchToComplete(location);

        // Execute a GET by batch id to validate error when trying to create student
        //   with invalid state abbreviation
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(location)
        .then()
            .statusCode(200)
            .body("numProcessed", is(DEFAULT_BATCH_SIZE))
            .body("numSubmitted", is(DEFAULT_BATCH_SIZE))
            .body("numSuccess", is(invalidStudentItem))
            .body("exceptions[0].errorMessage", startsWith("Attempted to modify student in state"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }



    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidInstitutionId() {
        int invalidStudentItem = DEFAULT_BATCH_SIZE-1;

        List<StudentInfo> students = createBatchOfStudents();

        // Test an invalid institution id for one of the students
        students.get(invalidStudentItem).setInstitutionIdentifier("");

        // Execute a batch POST and extract the location header to validate that this
        //    student returned an error
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(BASE_URI + DEFAULT_STATE_ABBREVIATION + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        waitForBatchToComplete(location);

        // Execute a GET by batch id to validate error when trying to create student
        //   with invalid institution id
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(location)
        .then()
            .statusCode(200)
            .body("numProcessed", is(DEFAULT_BATCH_SIZE))
            .body("numSubmitted", is(DEFAULT_BATCH_SIZE))
            .body("numSuccess", is(invalidStudentItem))
            .body("exceptions[0].errorMessage", startsWith("Valid Institution Identifier is required"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidBirthDate() {
        List<StudentInfo> students = createBatchOfStudents();

        // Test an invalid birth date
        students.get(0).setBirthDate("882292-12-983-55");

        // Execute a batch POST to validate that this student returned an error
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(students)
        .when()
            .post(BASE_URI + DEFAULT_STATE_ABBREVIATION + "/batch")
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidGradeLevel() {
        int invalidStudentItem = DEFAULT_BATCH_SIZE-1;

        List<StudentInfo> students = createBatchOfStudents();

        // Test an invalid birth date for one of the students
        students.get(invalidStudentItem).setGradeLevelWhenAssessed("22");

        // Execute a batch POST and extract the location header to validate that this
        //    student returned an error
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(BASE_URI + DEFAULT_STATE_ABBREVIATION + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        waitForBatchToComplete(location);

        // Execute a GET by batch id to validate error when trying to create student
        //   with invalid grade level
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(location)
        .then()
            .statusCode(200)
            .body("numProcessed", is(DEFAULT_BATCH_SIZE))
            .body("numSubmitted", is(DEFAULT_BATCH_SIZE))
            .body("numSuccess", is(invalidStudentItem))
            .body("exceptions[0].errorMessage", startsWith("Grade level when assessed should be in"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidSex() {
        int invalidStudentItem = DEFAULT_BATCH_SIZE-1;

        List<StudentInfo> students = createBatchOfStudents();

        // Test an invalid birth date for one of the students
        students.get(invalidStudentItem).setSex("");

        // Execute a batch POST and extract the location header to validate that this
        //    student returned an error
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(BASE_URI + DEFAULT_STATE_ABBREVIATION + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        waitForBatchToComplete(location);

        // Execute a GET by batch id to validate error when trying to create student
        //   with invalid sex
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(location)
        .then()
            .statusCode(200)
            .body("numProcessed", is(DEFAULT_BATCH_SIZE))
            .body("numSubmitted", is(DEFAULT_BATCH_SIZE))
            .body("numSuccess", is(invalidStudentItem))
            .body("exceptions[0].errorMessage", startsWith("Sex is required"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }
}
