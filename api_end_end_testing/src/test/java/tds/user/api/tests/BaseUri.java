package tds.user.api.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.annotations.BeforeClass;


/**
 * Created by mjbarger on 9/6/16.
 */
public abstract class BaseUri {
    String authenticateURI = null;
    String userUri = null;

    @BeforeClass
    public void init() {

        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

        try {
            prop.load(inputStream);
            System.out.println("auth prop " + prop.getProperty("authenticateURI"));
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
        authenticateURI = prop.getProperty("authenticateURI");
        userUri = prop.getProperty("user.URI");
    }
}
