package tds.student.api.tests;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.http.ContentType;
import org.testng.annotations.Test;
import tds.base.BaseUri;
import tds.student.api.model.StudentDeleteBatchInfo;
import tds.student.api.model.StudentInfo;

import java.util.ArrayList;
import java.util.List;

/*
 * This class tests Student API endpoints that creates, updates and deletes a student
 */
public class StudentApiTests extends BaseUri {
    private String uriLocation = "/rest/external/student/";

    /*
     * Test of Create Student, HTTP POST of /api/external/student/{stateCode}, 201 success item created
     * Test of Get Student, HTTP GET of /api/external/student/{stateCode}/{ssid}, 200 success item found
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

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(studentInfo)
        .when()
            .post(uriLocation + stateAbbrev)
        .then()
            .statusCode(201)
            .header("location", endsWith(uriLocation + stateAbbrev + "/" + ssid));

        // Execute a GET by ssid to validate that the student was created
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + stateAbbrev + "/"  + ssid)
        .then()
            .statusCode(200)
            .body("institutionIdentifier", is(studentInfo.getInstitutionIdentifier()))
            .body("firstName", is(studentInfo.getFirstName()))
            .body("middleName", is(studentInfo.getMiddleName()))
            .body("lastName", is(studentInfo.getLastName()));

        return studentInfo;
    }

    /*
     * Test of Create Students, HTTP POST of /api/external/student/{stateCode}, 202 success item created
     * Test of Get Students, HTTP GET of /api/external/student/{stateCode}/{ssid}, 200 success item found
     */
    private List<StudentInfo> createBatchOfStudents(String ssid1, String ssid2, String stateAbbrev) {
        List<StudentInfo> students = new ArrayList<StudentInfo>();

        students.add(new StudentInfo(
            ssid1,
            stateAbbrev,
            "DS9001",
            "DISTRICT9",
            "Amy",
            "Martin",
            "Leslie",
            "1999-01-01",
            "e" + ssid1,
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
        ));
        students.add(new StudentInfo(
            ssid2,
            stateAbbrev,
            "DS9001",
            "DISTRICT9",
            "Alex",
            "Karev",
            "Jim",
            "1999-01-01",
            "e" + ssid2,
            "04",
            "Female",
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
            "2001-11-01",
            null,
            null,
            null,
            null,
            0
        ));

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(students)
        .when()
            .post(uriLocation + stateAbbrev + "/" + "batch")
        .then()
            .statusCode(202);

        // Execute a GET by ssid to validate that the student was created
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + stateAbbrev + "/"  + ssid1)
        .then()
            .statusCode(200)
            .body("institutionIdentifier", is(students.get(0).getInstitutionIdentifier()))
            .body("firstName", is(students.get(0).getFirstName()))
            .body("middleName", is(students.get(0).getMiddleName()))
            .body("lastName", is(students.get(0).getLastName()));

        return students;
    }

    /*
     *  Test of Delete Student, HTTP DELETE of /api/external/student/{stateCode}/{ssid}, 204 success item found and deleted
     *  Test of Get Student, HTTP GET of /api/external/external/student/{stateCode}/{ssid}, 404 item not found
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

        // Execute a GET by ssid to validate user is deleted
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + stateAbbrev + "/" + ssid)
        .then()
            .statusCode(404);
    }

    private void deleteBatchOfStudents(List<StudentDeleteBatchInfo> deleteStudents) {
        // Execute a Delete to remove the batch of students
        String stateCode = deleteStudents.get(0).getStateCode();

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
        String studentId = location.substring(pos);

        // Execute a GET by ssid to validate student is deleted
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + "batch/" + studentId)
        .then()
            .statusCode(200)
            .body("exceptions[0].errorMessage", endsWith("does not exist"));

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
        String ssid1  = createRandomStudentSsid();
        String ssid2  = createRandomStudentSsid();
        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(ssid1, ssid2, stateAbbrev);
        students.get(0).setInstitutionIdentifier("DS9222");
        students.get(0).setMiddleName("Ray");
        students.get(0).setAmericanIndianOrAlaskaNative(true);
        students.get(1).setLastName("Jones");
        students.get(1).setBlackOrAfricanAmerican(true);
        students.get(1).setElpLevel(3);

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
        String studentId = location.substring(pos);

        // Execute a GET by ssid to verify that the student information has changed
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
        .when()
            .get(uriLocation + "batch/" + studentId)
        .then()
            .statusCode(200)
            .body("id", is(studentId));

        // Execute a DELETE to delete the batch of students
        List<StudentDeleteBatchInfo> deleteStudents = new ArrayList<StudentDeleteBatchInfo>();
        deleteStudents.add(new StudentDeleteBatchInfo(ssid1, stateAbbrev));
        deleteStudents.add(new StudentDeleteBatchInfo(ssid2, stateAbbrev));

        deleteBatchOfStudents(deleteStudents);
    }

    @Test
    public void shouldNotDeleteStudentWithInvalidSsid() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

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

        // Execute POST with invalid state abbreviation
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
    public void shouldNotCreateBatchOfStudentsWithInvalidBirthDate() {
        String ssid1  = createRandomStudentSsid();
        String ssid2  = createRandomStudentSsid();
        String stateAbbrev = "CA";
        List<StudentInfo> students = createBatchOfStudents(ssid1, ssid2, stateAbbrev);

        // Test an invalid birth date
        students.get(0).setBirthDate("882292-12-983");

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
    public void shouldNotCreateUpdateStudentWithInvalidExternalSsid() {
        String ssid = createRandomStudentSsid();
        String stateAbbrev = "CA";

        // Create a student
        StudentInfo studentInfo = createStudent(ssid, stateAbbrev);

        // Test and invalid state abbreviation
        studentInfo.setExternalSsid("");

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

        // Test and invalid state abbreviation
        studentInfo.setSex("");

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

        // Test and invalid state abbreviation
        studentInfo.setGradeLevelWhenAssessed("123456");

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

        // Test and invalid state abbreviation
        studentInfo.setLanguageCode("ENGL");

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

        // Test and invalid state abbreviation
        studentInfo.setFirstEntryDateIntoUsSchool("1930-01-01");

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

        // Test and invalid state abbreviation
        studentInfo.setLepEntryDate("214-1999-12-01");

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

        // Test and invalid state abbreviation
        studentInfo.setLepExitDate("2099-12-01-99");

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

        // Test and invalid state abbreviation
        studentInfo.setTitle3ProgramType("500");

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

        // Test and invalid state abbreviation
        studentInfo.setPrimaryDisabilityType("3333");

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
}
