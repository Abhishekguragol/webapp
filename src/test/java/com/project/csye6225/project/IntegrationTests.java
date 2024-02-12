package com.project.csye6225.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.csye6225.project.pojo.User;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;


import static org.hamcrest.Matchers.*;

@SpringBootTest
public class IntegrationTests {

    @Test
    public void Test1(){

        User user = new User();
        user.setFirst_name("Abhi");
        user.setLast_name("Guragol");
        user.setPassword("MyPass");
        user.setUsername("abhishek@gmail.com");

        // Adding a new User  to the database using the Post request
        given()
            .port(8080)
            .contentType(ContentType.JSON) // Set the request content-type
            .accept(ContentType.JSON) 
            .body(user)
        .when()
            .post("/v1/user")
        .then()
            .statusCode(HttpStatus.CREATED.value());


        // Checking if the user exists in the database using get request
        given()
                .port(8080)
                .auth().preemptive().basic("abhishek@gmail.com", "MyPass")
                .accept(ContentType.JSON) 
        .when()
            .get("/v1/user/self")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("username", equalTo("abhishek@gmail.com"))
            .body("first_name", equalTo("Abhi"))
            .body("last_name", equalTo("Guragol"));
            
    }

    @Test
    public void Test2(){

        User user = new User();
        user.setFirst_name("Abhis");
        user.setLast_name("G");
        user.setPassword("MyPass");
        user.setUsername("abhishek@gmail.com");

        // Updating a user present in the Database using  the put request
        given()
            .port(8080)
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("abhishek@gmail.com", "MyPass")
            .body(user)
        .when()
            .put("/v1/user/self")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
        
        // Validating if the user details has been updated in the database by fetching User using get request
            given()
                .port(8080)
                .auth().preemptive().basic("abhishek@gmail.com", "MyPass")
                .accept(ContentType.JSON) 
            .when()
                .get("/v1/user/self")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("abhishek@gmail.com"))
                .body("first_name", equalTo("Abhis"))
                .body("last_name", equalTo("G"));;
    }
    
}
