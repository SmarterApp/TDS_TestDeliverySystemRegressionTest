package tds.diagnostics.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.testng.annotations.Test;
import tds.base.BaseUri;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DiagnosticTests extends BaseUri {
    void statusIsIdeal(String baseURI) {
        RestAssured.baseURI = baseURI;
        given().param("level", 5)
                .contentType(ContentType.JSON)
                .when()
                .get("status")
                .then()
                .statusCode(200)
                .body("statusText", is("Ideal"));
    }

    @Test
    void artStatusIsIdeal() {
        statusIsIdeal(artBaseURI + "/rest");
    }

    @Test
    void proctorStatusIsIdeal() {
        statusIsIdeal(proctorBaseURI);
    }

    @Test
    void studentStatusIsIdeal() {
        statusIsIdeal(studentBaseURI);
    }
}
