package tds.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import org.testng.annotations.BeforeClass;


/*
 * This class reads a property file containing URIs, and authentication specific
 *    information for user access
 */
public abstract class BaseUri {
    private String accessToken = null;
    private String authenticateURI = null;
    private String userURI = null;
    public Header authHeader = null;

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

        getAuthenticationCode(prop);
    }

    /*
     *  Execute POST to authenticate user and get access code
     */
    private void getAuthenticationCode(Properties prop) {

        String accessToken =
            given()
                .contentType("application/x-www-form-urlencoded")
                .queryParam("realm", prop.getProperty("realm"))
                .formParam("client_id", prop.getProperty("clientId"))
                .formParam("client_secret", prop.getProperty("clientSecret"))
                .formParam("grant_type", prop.getProperty("password"))
                .formParam("password", prop.getProperty("password"))
                .formParam("username", prop.getProperty("username"))
            .when()
                .post(authenticateURI + prop.getProperty("authenticateURIEnd"))
            .then()
                .statusCode(200)
            .extract()
                .path("access_token");

        authHeader = new Header("Authorization", "Bearer " + accessToken);
        System.out.println("authHeader: " + authHeader);
    }

    /*
     *  Create a random user email
     */
    public String createRandomUserEmail() {
        String randomStr = UUID.randomUUID().toString();

        int pos = randomStr.indexOf("-");
        String userEmail = "jennifer.smith" + randomStr.substring(0,pos) + "@example.com";
        System.out.println("**** userEmail: " + userEmail);

        return userEmail;
    }

    /*
     *  Create a random student ssid
     */
    public String createRandomStudentSsid() {
        String ssid = null;
        String randomStr = UUID.randomUUID().toString();

        int pos = randomStr.indexOf("-");
        ssid = "ssid" + randomStr.substring(0,pos);
        System.out.println("**** ssid: " + ssid);

        return ssid;
    }
}
