package org.github.adisputraa.qshort;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
@QuarkusTest
public class UrlResourceTest {

    @Test
    public void testShortenAndRedirect() {
        // --- BAGIAN 1: Uji Endpoint POST /urls ---
        // Pertama, kirim permintaan untuk membuat URL pendek baru.
        String shortCode = given()
                .contentType(ContentType.JSON)
                .body("{\"url\":\"https://www.google.com\"}")
            .when()
                .post("/urls")
            .then()
                .statusCode(201)
                .body("originalUrl", is("https://www.google.com"))
                .body("shortCode", notNullValue())
                .extract()
                .path("shortCode"); // Ekstrak shortCode dari respons JSON

        System.out.println("Generated shortCode for testing: " + shortCode);

        // --- BAGIAN 2: Uji Endpoint GET /urls/{shortCode} ---
        // Gunakan shortCode yang  didapat untuk menguji redirect.
        // Kita tidak mengikuti redirect secara otomatis agar bisa memeriksa status code-nya.
        given()
                .redirects().follow(false) // Penting: jangan ikuti redirect
                .when()
                .get("/urls/" + shortCode)
                .then()
                .statusCode(303) // 303 See Other adalah status code yang tepat untuk redirect ini
                .header("Location", is("https://www.google.com")); // Periksa apakah header Location benar
    }

    @Test
    public void testCreateWithEmptyUrl() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"url\":\"\"}")
                .when()
                .post("/urls")
                .then()
                .statusCode(400); // 400 Bad Request
    }
}
