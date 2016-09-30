package tds.student.api.tests;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.http.ContentType;
import org.testng.annotations.Test;
import tds.base.BaseUri;
import tds.student.api.model.StudentDeleteBatchInfo;
import tds.student.api.model.StudentInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * This class tests Student API endpoints that creates, updates and deletes a student
 */
public class StudentApiTests extends BaseUri {
    private String uriLocation = "/rest/external/student/";

    /*
     * Test of creating a student, HTTP POST of /api/external/student/{stateCode}, 201 success item created
     * Test of getting a student, HTTP GET of /api/external/student/{stateCode}/{ssid}, 200 success item found
     */
    private StudentInfo createStudent(String ssid, String stateAbbrev) {
        StudentInfo studentInfo = new StudentInfo(
            ssid,
            stateAbbrev,
            "DS9001",
            "DISTRICT9",
            "Amy",
            "Martin",
            "Leslie",
            "1999-01-01",
            "e" + ssid,
            "04",
            "Male",
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            false,
            false,
            false,
            false,
            null,
            false,
            "2000-02-01",
            null,
            null,
            null,
            null,
            0
        );

        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(studentInfo)
            .when()
                .post(uriLocation + stateAbbrev)
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

    /*
     * Test of creating batch of students, HTTP POST of /api/external/student/{stateCode}/batch,
     *      202 success item created
     * Test of getting batch of students, HTTP GET of /api/external/student/batch/{batchId},
     *      200 success item found
     * Test of getting single student, HTTP GET of /api/external/student/{stateCode}/{ssid},
     *      200 success item found
     */
    private List<StudentInfo> createBatchOfStudents(int numStudents, String stateAbbrev) {
        List<String> ssidList = new ArrayList<String>();
        List<StudentInfo> students = new ArrayList<StudentInfo>();

        for (int i = 0; i < numStudents; i++) {
            ssidList.add(createRandomStudentSsid());

            students.add(new StudentInfo(
                ssidList.get(i),
                stateAbbrev,
                "DS9002",
                "DISTRICT8",
                "Meredith",
                "Gray",
                "Leslie",
                createRandomBirthDate(LocalDate.of(2000, 1, 1), LocalDate.of(2005, 1, 1)),
                "e" + ssidList.get(i),
                "05",
                "Male",
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false,
                null,
                false,
                createRandomBirthDate(LocalDate.of(2005, 6, 1), LocalDate.of(2011, 8, 1)),
                null,
                null,
                null,
                null,
                0
            ));
        }

        // Execute a batch POST and extract the location header to validate that the
        //    students were created
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(uriLocation + stateAbbrev + "/" + "batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

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
                .get(uriLocation + stateAbbrev + "/"  + ssidList.get(i))
            .then()
                .statusCode(200)
                .body("ssid", is(students.get(i).getSsid()))
                .body("firstName", is(students.get(i).getFirstName()))
                .body("middleName", is(students.get(i).getMiddleName()))
                .body("lastName", is(students.get(i).getLastName()));
        }

        return students;
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
            .delete(uriLocation + stateAbbrev + "/" + ssid)
        .then()
            .statusCode(204);

        // Execute a GET by ssid to validate student is deleted
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + stateAbbrev + "/" + ssid)
        .then()
            .statusCode(404);
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
                .delete(uriLocation + stateCode + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        int pos = location.lastIndexOf("/") + 1;
        String batchId = location.substring(pos);

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
                .get(uriLocation + stateCode + "/" + students.get(i).getSsid())
            .then()
                .statusCode(404);
        }
    }

    @Test
    public void shouldCreateDeleteStudent() {
        String stateAbbrev = "CA";
        String ssid = createRandomStudentSsid();

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Execute a DELETE to delete the student
        deleteSingleStudent(ssid,stateAbbrev);
    }

    @Test
    public void shouldCreateUpdateDeleteStudent() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Change some of the data for the student just created
        studentInfo.setStateAbbreviation(stateAbbrev);
        studentInfo.setFirstName("William");
        studentInfo.setLastName("Peters");
        studentInfo.setGradeLevelWhenAssessed("02");
        studentInfo.setHispanicOrLatino(true);
        studentInfo.setElpLevel(2);

        // Execute a POST to update the student with new information
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(204)
            .header("location", endsWith(uriLocation + stateAbbrev + "/" + ssid));

        // Execute a GET by ssid to verify that the student information has changed
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + stateAbbrev + "/" + ssid)
        .then()
            .statusCode(200)
            .body("firstName", is(studentInfo.getFirstName()))
            .body("lastName", is(studentInfo.getLastName()));

        // Execute a DELETE to delete the student
        deleteSingleStudent(ssid,stateAbbrev);
    }

    @Test
    public void shouldCreateUpdateDeleteBatchOfStudents() {
        int numStudents = 3;

        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(numStudents, stateAbbrev);

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
        String location =
            given()
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(students)
            .when()
                .post(uriLocation + stateAbbrev + "/batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        int pos = location.lastIndexOf("/") + 1;
        String batchId = location.substring(pos);

        // Execute a GET by ssid to verify that the student information has changed
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + "batch/" + batchId)
        .then()
            .statusCode(200)
            .body("id", is(batchId));

        // Execute a DELETE to delete the batch of students
        deleteBatchOfStudents(students);
    }

    @Test
    public void shouldNotDeleteStudentWithInvalidSsid() {
        String stateAbbrev = "CA";

        // Execute a DELETE to verify that a student with an invalid ssid can't be deleted
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
        .when()
            .delete(uriLocation + stateAbbrev + "/" + "ABCDEF")
        .then()
            .statusCode(404);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidStateAbbreviation() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid state abbreviation that should only be 2 characters
        stateAbbrev = "ABC";
        studentInfo.setStateAbbreviation(stateAbbrev);

        // Execute POST to verify that a student can't be created with an invalid state abbreviation.
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithNullStateAbbreviation() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid state abbreviation set to null
        stateAbbrev = null;
        studentInfo.setStateAbbreviation(stateAbbrev);

        // Execute POST to verify that a student can't be created with a null state abbreviation.
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidInstitutionId() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid institution Id set to a blank
        studentInfo.setInstitutionIdentifier("");

        // Execute a POST to verify that a student can't be created with an invalid institution id
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidDistrictId() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid district Id
        studentInfo.setDistrictIdentifier("DistrictAADEFFJJIDDDDWERDEDEFG00293838192922");

        // Execute a POST to verify that a student can't be created with an invalid district id
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.districtIdentifier[0]", equalTo("District Identifier size must be between 1 and 40"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidFirstName() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid first name
        studentInfo.setFirstName("MynameisJoeSmithAndMyMiddleNameisSteven");

        // Execute a POST to verify that a student can't be created with an invalid first name
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.firstName[0]", equalTo("The max size of the FirstName is 35"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLastName() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid last name
        studentInfo.setLastName("MylastnameisBiggalow-Johnson-Richardson");

        // Execute a POST to verify that a student can't be created with an invalid last name
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.lastName[0]", equalTo("The max size of the LastName is 35"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidMiddleName() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid middle name
        studentInfo.setMiddleName("MymiddlenameisWilliam-Peterson-Henderson");

        // Execute a POST to verify that a student can't be created with an invalid middle name
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.middleName[0]", equalTo("The max size of the MiddleName is 35"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidExternalSsid() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid external ssid
        studentInfo.setExternalSsid("");

        // Execute a POST to verify that a student can't be created with an invalid external ssid
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidSex() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid sex
        studentInfo.setSex("");

        // Execute a POST to verify that a student can't be created with an invalid sex
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidGradeLevel() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid grade level
        studentInfo.setGradeLevelWhenAssessed("123456");

        // Execute a POST to verify that a student can't be created with an invalid grade level
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.gradeLevelWhenAssessed[0]", startsWith("Grade level when assessed should be"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLanguageCode() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid language code
        studentInfo.setLanguageCode("ENGL");

        // Execute a POST to verify that a student can't be created with an invalid language code
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.languageCode[0]", startsWith("Language Codes are invalid"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidFirstEntryDate() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid entry date
        studentInfo.setFirstEntryDateIntoUsSchool("1930-01-01");

        // Execute a POST to verify that a student can't be created with an invalid first entry date
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.firstEntryDateIntoUsSchool[0]", startsWith("First Entry Date Into US School should always be after the year of birth date"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLepEntryDate() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid LEP Entry Date
        studentInfo.setLepEntryDate("214-1999-12-01");

        // Execute a POST to verify that a student can't be created with an invalid LEP entry date
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidLepExitDate() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid LEP exit date
        studentInfo.setLepExitDate("2099-12-01-99");

        // Execute a POST to verify that a student can't be created with an invalid LEP exit date
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidTitle3ProgType() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid title 3 program type
        studentInfo.setTitle3ProgramType("500");

        // Execute a POST to verify that a student can't be created with an invalid
        //    title 3 program type
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.title3ProgramType[0]", startsWith("TitleIIILanguage Instruction ProgramType in invalid Types"));
    }

    @Test
    public void shouldNotCreateUpdateStudentWithInvalidPrimDisabilityType() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test an invalid primary disability type
        studentInfo.setPrimaryDisabilityType("3333");

        // Execute a POST to verify that a student can't be created with an invalid
        //    primary disability type
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(400)
            .body("messages.primaryDisabilityType[0]", startsWith("PrimaryDisabilityType is invalid Types"));
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidStateAbbrev() {
        int numStudents = 3;
        int invalidStudentItem = numStudents-1;

        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(numStudents, stateAbbrev);

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
                .post(uriLocation + stateAbbrev + "/" + "batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        int pos = location.lastIndexOf("/") + 1;
        String batchId = location.substring(pos);

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
            .body("id", equalTo(batchId))
            .body("numProcessed", is(students.size()))
            .body("numSubmitted", is(students.size()))
            .body("numSuccess", is(students.size()-1))
            .body("exceptions[0].errorMessage", startsWith("Attempted to modify student in state"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidInstitutionId() {
        int numStudents = 3;
        int invalidStudentItem = numStudents-1;

        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(numStudents, stateAbbrev);

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
                .post(uriLocation + stateAbbrev + "/" + "batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        int pos = location.lastIndexOf("/") + 1;
        String batchId = location.substring(pos);

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
            .body("id", equalTo(batchId))
            .body("numProcessed", is(students.size()))
            .body("numSubmitted", is(students.size()))
            .body("numSuccess", is(students.size()-1))
            .body("exceptions[0].errorMessage", startsWith("Valid Institution Identifier is required"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidBirthDate() {
        int numStudents = 3;

        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(numStudents, stateAbbrev);

        // Test an invalid birth date
        students.get(0).setBirthDate("882292-12-983-55");

        // Execute a batch POST to validate that this student returned an error
        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(students)
        .when()
            .post(uriLocation + stateAbbrev + "/" + "batch")
        .then()
            .statusCode(400);
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidGradeLevel() {
        int numStudents = 3;
        int invalidStudentItem = numStudents-1;

        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(numStudents, stateAbbrev);

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
                .post(uriLocation + stateAbbrev + "/" + "batch")
            .then()
                .statusCode(202)
            .extract()
                .header("location");

        int pos = location.lastIndexOf("/") + 1;
        String batchId = location.substring(pos);

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
            .body("id", equalTo(batchId))
            .body("numProcessed", is(students.size()))
            .body("numSubmitted", is(students.size()))
            .body("numSuccess", is(students.size()-1))
            .body("exceptions[0].errorMessage", startsWith("Grade level when assessed should be in"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }

    @Test
    public void shouldNotCreateBatchOfStudentsWithInvalidSex() {
        int numStudents = 3;
        int invalidStudentItem = numStudents-1;

        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(numStudents, stateAbbrev);

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
                .post(uriLocation + stateAbbrev + "/" + "batch")
                .then()
                .statusCode(202)
                .extract()
                .header("location");

        int pos = location.lastIndexOf("/") + 1;
        String batchId = location.substring(pos);

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
            .body("id", equalTo(batchId))
            .body("numProcessed", is(students.size()))
            .body("numSubmitted", is(students.size()))
            .body("numSuccess", is(students.size()-1))
            .body("exceptions[0].errorMessage", startsWith("Sex is required"))
            .body("exceptions[0].ssid", equalTo(students.get(invalidStudentItem).getSsid()));
    }
}
