# Welcome to the TDS Regression Test Suite Application
The TDS-Regressions project contains a suite of regression tests for testing various tools, accommodations, and features in TDS.

## Regression Tests Pre-conditions and Setup
The following steps are required in order to prepare the TDS-Regression test suite for execution.

### TDS Environment
* The Implementation Readiness Package (IRP), Practice, and Training Test packages must be loaded into TDS and ART/TSB.
* Run tdsregression_setup.sql, located in src/main/resources/ on the TDS MySQL database. This script does the following:
    * Updates IRP, Practice, Training assessment names to more unique, descriptive names that the regression test suite utilizes
    * Prepares the segment properties table
    * Adds accommodation and test tool seed data as required by many of the regression tests
    
### Regression Test host environment and settings
The system hosting the regression test suite has the following dependencies:
  * Firefox version 54.0
  * Selenium IDE 2.9.1 (Firefox extension)
  * Install Add-ons used by the regression suite:
      - [Stored-Variables](https://addons.mozilla.org/en-US/firefox/addon/stored-variables-viewer-seleni/) 
      - [Flow-Control](https://addons.mozilla.org/en-US/firefox/addon/flow-control/?src=ss)
  * Update Firefox's settings to allow proctor's and student's URLs to open in pop-up windows 
  
## Running the Test Suite
The TDS-Regressions test suite is executed via SeleniumIDE directly. 
1. Open Firefox/Selenium IDE
2. Open the test suite by select "File" -> "Open Test Suite..." and then browse to and select one of the following test suite files:
    * Guest (tds_selenium_ide/practice_tests/Regression_Test_Suite_for_GUEST_Student) 
    * Students (tds_selenium_ide/student_and_proctors/Regression_Test_Suite_Regular_students)
3. Before running any test script in the suite, update the "Config" test script to set URLs, proctor's credentials and student's name.
4. The script "Create New user" will create students for grades 3, 4, 5, 6, 7, 8 and 11 in ART with no particular accommodations using the student's name provided in the config file. 
    * If the test suite is ran more than once, make sure to update the name of the student, otherwise an error may occur when create a student with duplicate data
    * The error/failed test case above can either be ignored, or the student creation step can be deleted or skipped from the test suite.
4. The test suite can run without error as many times as the maximum number of test opportunities in the assessments. Once the student has reached the maximum number of test opportunitites, the test scripts will fail. We have 2 options:
    * Provide a new student name in the config file or
    * Update the assessments with a higher number of maximum test opportunities (via ART or directly in the configs database).
5. Click the button "Play entire test suite" to execute all test scripts or run individual test cases by clicking "Play current test case". 
    * If you choose to run individual test cases, make sure you stop the proctor's test session after the script completes. The scripts were written to be run one after the other therefore they don't stop the test session automatically.
6. If the name of the assessments change (labels), the test scripts must be updated to use the new names.  

## Links
* [Implementation Readiness Package (IRP)](ftp://ftps.smarterbalanced.org/~sbacpublic/Public/ImplementationReadiness/2015.08.19.IrpTestPackageAndContent.zip)
* [Practice Test Package](ftp://ftps.smarterbalanced.org/~sbacpublic/Public/PracticeAndTrainingTests/2015-08-28_PracticeTestPackagesAndContent.zip)
* [Training Test Package](ftp://ftps.smarterbalanced.org/~sbacpublic/Public/PracticeAndTrainingTests/2015-08-28_TrainingTestPackagesAndContent.zip)
* [SeleniumIDE Extension for Firefox](https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/)

## End to End API User Tests Pre-conditions and Setup
The following steps are required in order to prepare the end to end User testing for execution.

### Properties File
A properties file must include the following data:
* authenticateURI= (URI for OAuth access)
* userURI=(user URI for creating, updating and deleting a user)
* realm=(attribute to indicate the scope of protection)
* clientId=(client ID credential)
* clientSecret=(client secret credential for accessing a token)
* grantType=(client credentials for exchanging a password for an access token)
* password=(a password)
* username=(valid username)
* authenticateURIEnd=(OAuth URI ending)
Maven must be set up and a -D flag must be used to specify the location of the properties file. Example:
* mvn test -Dconfig=/pathto/config.properties