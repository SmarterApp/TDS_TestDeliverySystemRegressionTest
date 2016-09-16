package tds.user.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
<<<<<<< HEAD
import java.util.UUID;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
=======


import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.RestAssured;

>>>>>>> feature/ART_API_testing
import org.testng.annotations.BeforeClass;

import static com.jayway.restassured.RestAssured.given;

/**
<<<<<<< HEAD
 * Class to read properites file containing URIs, and authentication specific
 *    information.
=======
 *
>>>>>>> feature/ART_API_testing
 */
public abstract class BaseUri {
    String accessToken = null;
    String authenticateURI = null;
    String userURI = null;
    String userEmail = null;
    Header authHeader = null;

<<<<<<< HEAD
    @BeforeClass
    public void init() throws IOException {
        String configFileLocation = System.getProperty("config");

        if (configFileLocation == null || configFileLocation.isEmpty()) {
            throw new IllegalArgumentException("config file location system property is required (mvn test -Dconfig=<fileLocation>");
        }
=======
    @BeforeClass(alwaysRun = true)
    public void init() {
        // Get properties needed for authentication
/*
        String configFileLocation = System.getProperty("config");
>>>>>>> feature/ART_API_testing

        if (configFileLocation == null || configFileLocation.isEmpty()) {
            throw new IllegalArgumentException("config file location system property is required (mvn test -Dconfig=<fileLocation>");
        }
*/
        String configFileLocation = "/Users/mjbarger/config.properties";
        Properties prop = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(configFileLocation);
            prop.load(inputStream);

            authenticateURI = prop.getProperty("authenticateURI");
<<<<<<< HEAD
            userURI = prop.getProperty("userURI");
            RestAssured.baseURI = userURI;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        getAuthenticationCode();
        userEmail = createRandomUser();
    }

    private void getAuthenticationCode() {

        String accessToken = given()
            .contentType("application/x-www-form-urlencoded")
            .queryParam("realm", "/sbac")
            .formParam("client_id", "pm")
            .formParam("client_secret", "sbac12345")
            .formParam("grant_type", "password")
            .formParam("password", "password")
            .formParam("username", "prime.user@example.com")
            .when()
            .post(authenticateURI + "/auth/oauth2/access_token")
            .then()
            .statusCode(200)
            .extract()
            .path("access_token");

        authHeader = new Header("Authorization", "Bearer " + accessToken);
    }

    private String createRandomUser() {
        String randomStr = UUID.randomUUID().toString();
        int pos = randomStr.indexOf("-");
        userEmail = "jennifer.smith" + randomStr.substring(0,pos) + "@example.com";
        System.out.println("**** userEmail: " + userEmail);

        return userEmail;
    }

    public String getRandomUser() {
        return userEmail;
=======
            userUri = prop.getProperty("userURI");

            System.out.println("auth prop " + authenticateURI);
            System.out.println("user prop " + userUri);


        } catch (IOException e) {
            System.out.println("catch IOException");

            e.printStackTrace();
        } finally {
            try {
                System.out.println("finally try");

                if (inputStream != null) {
                    System.out.println("close input stream");

                    inputStream.close();
                    System.out.println("DONE closing input stream");

                }
            } catch(IOException e) {
                System.out.println("catch IOException for input stream");

                e.printStackTrace();
            }
        }
>>>>>>> feature/ART_API_testing
    }
}
