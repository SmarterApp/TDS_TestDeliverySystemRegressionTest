package tds.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;
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

            // set the base URL for all future RestAssured usage (points to the ART base URL)
            RestAssured.baseURI = prop.getProperty("artBaseURI");
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
                .post(prop.getProperty("authenticateURI") + prop.getProperty("authenticateURIEnd"))
            .then()
                .statusCode(200)
            .extract()
                .path("access_token");

        authHeader = new Header("Authorization", "Bearer " + accessToken);
    }

    /*
     *  Create a random user email
     */
    public String createRandomUserEmail() {
        return String.format("jennifer.smith%s@example.com", UUID.randomUUID().toString().substring(0, 8));
    }

    /*
     *  Create a random student ssid
     */
    public String createRandomStudentSsid() {
        return String.format("ssid%s", UUID.randomUUID().toString().substring(0, 8));
    }

    /*
     *  Create random date
     */
    public String createRandomDate(LocalDate minDate, LocalDate maxDate) {
        final Random random = new Random();

        int minDay = (int) minDate.toEpochDay();
        int maxDay = (int) maxDate.toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        return (LocalDate.ofEpochDay(randomDay)).toString();
    }
}
