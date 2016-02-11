package tests;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import util.navigation.ProctorTestNavigator;

/**
 * Created by emunoz on 1/26/16.
 */
public abstract class ProctorBaseTest extends SeleniumBaseTest {
    @Value( "${tds.proctor.baseurl}" )
    protected String BASE_URL;

    @Value( "${tds.proctor.username}" )
    protected String PROCTOR_USERNAME;

    @Value( "${tds.proctor.password}" )
    protected String PROCTOR_PASSWORD;

    @Autowired
    protected ProctorTestNavigator navigator;

    @Before
    public void setup() {
        navigator.setDriver(driver);
    }
}
