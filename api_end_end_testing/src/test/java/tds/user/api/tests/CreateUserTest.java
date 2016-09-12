package tds.user.api.tests;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;
import tds.user.api.model.UserInfo;
import tds.user.api.model.RoleAssociation;

/*
    Authenticate user
    Testing HTTP POST of https://sso-deployment.sbtds.org/auth/oauth2/access_token?realm=/sbac
    and success status 200
 */
public class CreateUserTest extends BaseUri{
    String accessToken = null;
    String email = null;

    @Test
    public void getAuthenticationCode() {
        RestAssured.baseURI = authenticateURI;

        Header header = new Header("Content-Type", "application/x-www-form-urlencoded");

        accessToken = given()
            .contentType("application/x-www-form-urlencoded")
            .header(header)
            .queryParam("realm", "/sbac")
            .formParam("client_id", "pm")
            .formParam("client_secret", "sbac12345")
            .formParam("grant_type", "password")
            .formParam("password", "password")
            .formParam("username", "prime.user@example.com")
            .when()
            .post("/auth/oauth2/access_token")
            .then()
            .statusCode(200)
            .extract()
            .path("access_token");
    }

    /*
        Create user
        Testing HTTP POST of /rest/external/user, 201 success item created
    */
    @Test(dependsOnMethods = "getAuthenticationCode")
    public void createUser() {
        email = "betsy.ross@example.com";

        RestAssured.baseURI = userUri;

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));

        Header authHeader = new Header("Authorization", "Bearer " + accessToken);

        UserInfo userInfo = new UserInfo(email, "Betsy", "Ross", "800-332-4747", roleAssociations);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;

        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo);
            System.out.println("jsonInString: " + jsonInString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
            .when()
            .post("/rest/external/user")
            .then()
            .statusCode(201);
    }

    /*
        Update user
        Testing HTTP POST of /rest/external/user, 204 success item updated
    */
    @Test(dependsOnMethods = "createUser")
    public void updateUser() {

        RestAssured.baseURI = userUri;

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));

        Header authHeader = new Header("Authorization", "Bearer " + accessToken);

        UserInfo userInfo = new UserInfo(email, "Betsy", "Carson", "800-332-4747", roleAssociations);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;

        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo);
            System.out.println("jsonInString: " + jsonInString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
            .when()
            .post("/rest/external/user")
            .then()
            .statusCode(204);
    }

    /*
        Create invalid user
        Testing HTTP POST of /rest/external/user, 400 bad request
    */
    @Test(dependsOnMethods = "updateUser")
    public void invalidUser() {

        RestAssured.baseURI = userUri;

        List<RoleAssociation> roleAssociations = new ArrayList<RoleAssociation>();
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));
        roleAssociations.add(new RoleAssociation("Administrator", "CLIENT", "98765"));

        Header authHeader = new Header("Authorization", "Bearer " + accessToken);

        UserInfo userInfo = new UserInfo(email, "Betsy", "", null, roleAssociations);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;

        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo);
            System.out.println("jsonInString: " + jsonInString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(userInfo)
            .when()
            .post("/rest/external/user")
            .then()
            .statusCode(400);
    }

    /*
        Get user by email
        Testing HTTP GET of /rest/external/user/{email}/details, 200 success return student info
    */
    @Test(dependsOnMethods = "invalidUser")
    public void getUserByEmail() {
        RestAssured.baseURI = userUri;

        Header authHeader = new Header("Authorization", "Bearer " + accessToken);

        System.out.println("endpoint: " + "/rest/external/user/" + email + "/details");

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
            .when()
            .get("/rest/external/user/" + email + "/details")
            .then()
            .statusCode(200);
    }

    /*
        Get user by Email that is non-existent
        Testing HTTP GET of /rest/external/user/{email}/details, 404 not found
    */
    @Test(dependsOnMethods = "invalidUser")
    public void getUserByInvalidEmail() {
        RestAssured.baseURI = userUri;

        String invalidEmail = "invalidEmail@example.com";

        Header authHeader = new Header("Authorization", "Bearer " + accessToken);

        System.out.println("endpoint: " + "/rest/external/user/" + email + "/details");

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(authHeader)
            .when()
            .get("/rest/external/user/" + invalidEmail + "/details")
            .then()
            .statusCode(404);
    }

    /*
        Delete user by Email
        Testing HTTP DELETE of /rest/external/user, 204 success item found and deleted
    */
    @Test(dependsOnMethods = "getUserByInvalidEmail")
    public void deleteUserByEmail() {

        RestAssured.baseURI = userUri;

        Header authHeader = new Header("Authorization", "Bearer " + accessToken);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(email)
            .when()
            .delete("/rest/external/user")
            .then()
            .statusCode(204);
    }

    /*
        Delete user by Email that is non-existent
        Testing HTTP DELETE of /rest/external/user, 404 not found
    */
    @Test(dependsOnMethods = "deleteUserByEmail")
    public void deleteUserByInvalidEmail() {

        RestAssured.baseURI = userUri;

        Header authHeader = new Header("Authorization", "Bearer " + accessToken);

        given()
            .contentType(ContentType.JSON)
            .header(authHeader)
            .body(email)
            .when()
            .delete("/rest/external/user")
            .then()
            .statusCode(404);
    }
}
