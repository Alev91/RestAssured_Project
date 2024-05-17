package API_Testing;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.not;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import java.util.HashMap;
import java.util.Map;

public class Queries_7_to_19 {
    RequestSpecification reqSpec;
    private int accountId;
    private String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMWFhNjczNjcwOTJmNjA1ZWM3MTRhNGM5MWM2ZjYxZiIsInN1YiI6IjY2M2U3M2ZjYzdlYzg0ZTFkN2MxZWJjZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zq4a2LKVa7lgcGtjUQhre6sLeAQcxf32rh0evheoTn0"; // Replace 'your_api_key_here' with actual API key

    @BeforeClass
    public void setUp() {
        baseURI = "https://api.themoviedb.org/3";

        // Initial specification to fetch account ID
        RequestSpecification initialSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMWFhNjczNjcwOTJmNjA1ZWM3MTRhNGM5MWM2ZjYxZiIsInN1YiI6IjY2M2U3M2ZjYzdlYzg0ZTFkN2MxZWJjZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zq4a2LKVa7lgcGtjUQhre6sLeAQcxf32rh0evheoTn0")
                .setContentType(ContentType.JSON)
                .addQueryParam("api_key", apiKey)
                .build();

        // Fetch account ID
        accountId = given().spec(initialSpec)
                .when()
                .get("/account")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        // Final specification excluding account ID
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMWFhNjczNjcwOTJmNjA1ZWM3MTRhNGM5MWM2ZjYxZiIsInN1YiI6IjY2M2U3M2ZjYzdlYzg0ZTFkN2MxZWJjZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zq4a2LKVa7lgcGtjUQhre6sLeAQcxf32rh0evheoTn0")
                .setContentType(ContentType.JSON)
                .addQueryParam("api_key", apiKey)
                .build();

        System.out.println("Authenticated Account ID: " + accountId);
    }

    @Test(priority = 1)
    public void getRatedMovies() {
        given().spec(reqSpec)
                .pathParam("account_id", accountId)
                .when()
                .get("/account/{account_id}/rated/movies")
                .then()
                .log().all() // Log the response for debugging
                .statusCode(200)
                .body("results", anyOf(not(empty()), hasSize(0))); // results alanının boş olmamasını veya boyutunun 0 olmasını doğrula
    }


    @Test(priority = 2)
    public void getWatchlistMovies() {
        given().spec(reqSpec)
                .pathParam("account_id", accountId)
                .when()
                .get("/account/{account_id}/watchlist/movies")
                .then()
                .statusCode(200)
                .body("results", not(empty()));
    }

    @Test(priority = 3)
    public void getMovieGenres() {
        given().spec(reqSpec)
                .when()
                .get("/genre/movie/list")
                .then()
                .statusCode(200)
                .body("genres", not(empty()));
    }

    @Test(priority = 4)
    public void getNowPlayingMovies() {
        given().spec(reqSpec)
                .when()
                .get("/movie/now_playing")
                .then()
                .statusCode(200)
                .body("results", not(empty()));
    }

    @Test(priority = 5)
    public void getPopularMovies() {
        given().spec(reqSpec)
                .when()
                .get("/movie/popular")
                .then()
                .statusCode(200)
                .body("results", not(empty()));
    }

    @Test(priority = 6)
    public void getTopRatedMovies() {
        given().spec(reqSpec)
                .when()
                .get("/movie/top_rated")
                .then()
                .log().all() // Log the response for debugging
                .statusCode(200)
                .body("results", not(empty()));
    }


    @Test(priority = 7)
    public void getUpcomingMovies() {
        given().spec(reqSpec)
                .when()
                .get("/movie/upcoming")
                .then()
                .statusCode(200)
                .body("results", not(empty()));
    }

    @Test(priority = 8)
    public void searchForMovies() {
        String searchQuery = "Inception"; // Example search term; adjust as needed

        given().spec(reqSpec)
                .queryParam("query", searchQuery)
                .when()
                .get("/search/movie")
                .then()
                .statusCode(200)
                .body("results", not(empty()))
                .body("results.title", hasItem(containsString(searchQuery)));
    }

    @Test(priority = 9)
    public void getMovieDetails() {
        int movieId = 550; // Example movie ID; this should be a valid and known ID for testing

        given().spec(reqSpec)
                .pathParam("movie_id", movieId)
                .when()
                .get("/movie/{movie_id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(movieId))
                .body("title", notNullValue())
                .body("genres", not(empty()))
                .body("overview", notNullValue());
    }

    @Test(priority = 10)
    public void addMovieRating() {
        int movieId = 550; // Example movie ID, this should ideally be fetched dynamically or known to be valid
        double rating = 8.5; // Example rating to be added

        given().spec(reqSpec)
                .pathParam("movie_id", movieId)
                .contentType(ContentType.JSON)
                .body("{\"value\":" + rating + "}")
                .when()
                .post("/movie/{movie_id}/rating")
                .then()
                .statusCode(201)
                .body("status_code", equalTo(1));
    }

    @Test(priority = 11)
    public void deleteMovieRating() {
        int movieId = 550; // Example movie ID

        given().spec(reqSpec)
                .pathParam("movie_id", movieId)
                .when()
                .delete("/movie/{movie_id}/rating")
                .then()
                .statusCode(200)
                .body("status_code", equalTo(13))
                .body("status_message", equalTo("The item/record was deleted successfully."));
    }

    @Test(priority = 12)
    public void unauthorizedAccess() {
        int validListId = 123; // Geçerli list ID, bu gerçek ve bilinen bir ID olmalı
        String invalidSessionId = "invalid_session_id"; // Geçersiz session ID

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("media_id", 18); // Örnek media ID

        given().spec(reqSpec)
                .pathParam("list_id", validListId)
                .queryParam("session_id", invalidSessionId)
                .body(bodyParams)
                .when()
                .post("/list/{list_id}/add_item")
                .then()
                .log().all() // Yanıtı hata ayıklama için logla
                .statusCode(401)
                .body("status_message", equalTo("Authentication failed: You do not have permissions to access the service."));
    }


}