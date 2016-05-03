# Welcome to the TDS Regression Test Suite Application

The TDS-Regressions project contains a suite of regression tests for testing various tools, accommodations, and features in TDS.

The TDSRegressions test suite uses the JUnit test framework in conjunction with Spring to load and run the tests. All tests are written using
Selenium, a web browser automation test framework. By default, the TDS Regression test framework is built to utilize the Firefox WebDriver for all web interactions.

## Regression Tests Pre-conditions and Setup
The following steps are required in order to prepare the TDS-Regression test suite for execution.

### TDS Environment
* The IRP and Practice Test packages must be loaded into TDS.
* The configs.client_testtool and client_testtooltype tables must be seeded with the proper accommodations for each test in the IRP and Practice test package.
* The tdsregression.properties file must be updated to include credentials for a proctor user, student user (grade 3), as well as the TDS student and proctor host URLs.

### Regression Test host environment and settings
The system hosting the regression test suite has the following dependencies:

  * Firefox version 45.0.2 (Compatible with Selenium 2.53.0)
  * MVN

If a different Firefox version is used, a compatible Selenium version must be defined in the Maven POM file.


### Maven dependencies
TDSRegressions has a number of direct dependencies. These dependencies are already built and included in the Maven POM file.

* selenium (2.53.0)
* log4j
* junit
* spring-test
* spring-core
* spring-context
* browsermob (proxy)
* zohhak

## Running the JUnit Test Suite
The TDS-Regressions JUnit test suite can be run both through an IDE or via the command line, using the "mvn test" command at the project root level.

On IntelliJ IDEA, the test suite can be ran by right clicking on the test package to run (or at the "/tests" package to run all tests) and clicking "Run Tests."

Additional arguments can be provided to specify which specific tests to run. Please see the documentation located [here](https://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html)
for more information.

## Other Notes
Currently, all tools, accommodations, and designated supports are found in tests within IRP Test package with the exception of the "Global Notes" and "Dictionary" accommodation.
These two tools are found within the Grade 11 Performance test (from the "Practice Test" package).
