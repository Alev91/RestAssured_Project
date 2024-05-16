package API_Testing;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;

public class Queries_4_5_6 {
    RequestSpecification reqSpec;
    String url = "https://api.themoviedb.org/3/account";
    int id;

    @BeforeClass
    public void Setup() {

        baseURI = "https://www.themoviedb.org";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMWFhNjczNjcwOTJmNjA1ZWM3MTRhNGM5MWM2ZjYxZiIsInN1YiI6IjY2M2U3M2ZjYzdlYzg0ZTFkN2MxZWJjZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zq4a2LKVa7lgcGtjUQhre6sLeAQcxf32rh0evheoTn0")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void AddMovietoFavorites() {
        Map<String, Object> body1 = new HashMap<>();
        String media_type = "movie";
        String media_id = "550";
        Boolean favorite = true;

        body1.put("media_type", media_type);
        body1.put("media_id", media_id);
        body1.put("favorite", favorite);

        given()
                .spec(reqSpec)
                .body(body1)

                .when()
                .post(url + "/" + id + "/" + "favorite")


                .then()
                .log().body()

                .statusCode(201)

        ;

    }
    @Test
    public void GetFavoriteMovies() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/" + id + "/" + "favorite" + "/movies")

                .then()
                .statusCode(200)
        ;
    }
    @Test
    public void GetFavoriteTV() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/" + id + "/" + "favorite" + "/tv")


                .then()
                .statusCode(200)
        ;
    }

}
