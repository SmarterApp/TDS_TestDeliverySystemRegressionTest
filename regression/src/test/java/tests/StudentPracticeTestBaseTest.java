package tests;

import driver.SmarterBalancedWebDriver;
import driver.impl.SmarterBalancedWebDriverImpl;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import util.navigation.ProctorTestNavigator;
import util.navigation.StudentTestNavigator;

/**
 * Created by emunoz on 2/9/16.
 */
public class StudentPracticeTestBaseTest extends SeleniumBaseTest {
    @Value( "${tds.student.baseurl}" )
    protected String BASE_URL;

    @Autowired
    protected StudentTestNavigator navigator;

    protected String sessionId;

    @Before
    public void setup() {
        navigator.setDriver(driver);
    }
}
