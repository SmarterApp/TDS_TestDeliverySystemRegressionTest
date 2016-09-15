package tds.user.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.RestAssured;

import org.testng.annotations.BeforeClass;

/**
 *
 */
public abstract class BaseUri {
    String accessToken = null;
    String authenticateURI = null;
    String userUri = null;

    @BeforeClass(alwaysRun = true)
    public void init() {
        // Get properties needed for authentication
/*
        String configFileLocation = System.getProperty("config");

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
    }
}
