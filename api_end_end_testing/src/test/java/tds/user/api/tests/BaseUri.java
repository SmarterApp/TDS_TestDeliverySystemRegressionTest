package tds.user.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;

/**
 * Created by mjbarger on 9/6/16.
 */
public abstract class BaseUri {
    String authenticateURI = null;
    String userUri = null;

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
            System.out.println("auth prop " + prop.getProperty("authenticateURI"));

            authenticateURI = prop.getProperty("authenticateURI");
            userUri = prop.getProperty("user.URI");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
