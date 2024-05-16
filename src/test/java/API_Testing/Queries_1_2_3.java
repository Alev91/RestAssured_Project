package API_Testing;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Queries_1_2_3 {

    RequestSpecification reqSpec;
    String authenticityToken;

    String username = "alev27";
    String password = "1234";
    int accountID=0;
    String url = "https://api.themoviedb.org/3";



    @BeforeClass
    public void Setup() {
        baseURI = "https://www.themoviedb.org";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzYjFmNmFiMWJhNzUyNjgxYzkxZDlmODdmNzRjYTY3OCIsInN1YiI6IjY2M2U3NTI2Y2Q0MDI1MTZhNjEwNjBmMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.R2CYgWPq6aZrADMSaFu7T75kPZf-0G8no8vYGrjkvtg")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void authenticityToken() {

        authenticityToken =
                given()
                        .when()
                        .get("/login")
                        .then()
                        .statusCode(200)
                        .extract().htmlPath().getString("**.find { it.@name == 'authenticity_token' }.@value");


        System.out.println("authenticityToken = " + authenticityToken);

    }
        @Test(dependsOnMethods = "authenticityToken")
          public void Login () {

            Map<String, String> login = new HashMap<>();
            login.put("username", username);
            login.put("password", password);
            login.put("autheticityToken", authenticityToken);


            given()
                   .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzYjFmNmFiMWJhNzUyNjgxYzkxZDlmODdmNzRjYTY3OCIsInN1YiI6IjY2M2U3NTI2Y2Q0MDI1MTZhNjEwNjBmMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.R2CYgWPq6aZrADMSaFu7T75kPZf-0G8no8vYGrjkvtg")
                    .contentType(ContentType.HTML)
                    .body(login)
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(200);
        }


    @Test(dependsOnMethods = "authenticityToken")
    public void getAccountDetails() {


        accountID =

                given()
                        .spec(reqSpec)
                        .when()
                        .get(url+"/account")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("id")

        ;

        System.out.println("accountID = " + accountID);


    }


}
