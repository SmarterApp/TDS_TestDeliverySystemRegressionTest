package tds.user.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import org.testng.annotations.BeforeClass;

import static com.jayway.restassured.RestAssured.given;

/**
 * Class to read properites file containing URIs, and authentication specific
 *    information.
 */
public abstract class BaseUri {
    String authenticateURI = null;
    String userURI = null;
    String userEmail = null;
    Header authHeader = null;

    @BeforeClass
    public void init() throws IOException {
        String configFileLocation = System.getProperty("config");

        if (configFileLocation == null || configFileLocation.isEmpty()) {
            throw new IllegalArgumentException("config file location system property is required (mvn test -Dconfig=<fileLocation>");
        }

        Properties prop = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(configFileLocation);
            prop.load(inputStream);

            authenticateURI = prop.getProperty("authenticateURI");
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
    }
}
