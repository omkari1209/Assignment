package com.example.rest;

/**
 * Hello world!
 *
 */

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class App 
{
	
	@BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://bfhldevapigw.healthrx.co.in/automation-campus";
    }

    @Test
    public void createUserSuccessfully() {
        String jsonBody = "{\n" +
                "  \"firstName\": \"Test\",\n" +
                "  \"lastName\": \"Test\",\n" +
                "  \"phoneNumber\": 9999999999,\n" +
                "  \"emailId\": \"test.test@test.com\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("roll-number", "1")
                .contentType("application/json")
                .body(jsonBody)
                .post("/create/user");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void duplicatePhoneNumber() {
        String jsonBody = "{\n" +
                "  \"firstName\": \"Test\",\n" +
                "  \"lastName\": \"Test\",\n" +
                "  \"phoneNumber\": 9999999999,\n" +
                "  \"emailId\": \"unique.email@test.com\"\n" +
                "}";

        RestAssured.given()
                .header("roll-number", "1")
                .contentType("application/json")
                .body(jsonBody)
                .post("/create/user");

        Response response = RestAssured.given()
                .header("roll-number", "2")
                .contentType("application/json")
                .body(jsonBody)
                .post("/create/user");

        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    public void missingRollNumber() {
        String jsonBody = "{\n" +
                "  \"firstName\": \"Test\",\n" +
                "  \"lastName\": \"Test\",\n" +
                "  \"phoneNumber\": 9999999999,\n" +
                "  \"emailId\": \"test.missingroll@test.com\"\n" +
                "}";

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(jsonBody)
                .post("/create/user");

        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void missingRequiredFields() {
        String jsonBody = "{\n" +
                "  \"firstName\": \"Test\",\n" +
                "  \"phoneNumber\": 9999999999\n" +
                "}";

        Response response = RestAssured.given()
                .header("roll-number", "1")
                .contentType("application/json")
                .body(jsonBody)
                .post("/create/user");

        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    public void invalidEmailFormat() {
        String jsonBody = "{\n" +
                "  \"firstName\": \"Test\",\n" +
                "  \"lastName\": \"Test\",\n" +
                "  \"phoneNumber\": 9999999999,\n" +
                "  \"emailId\": \"invalid-email\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("roll-number", "1")
                .contentType("application/json")
                .body(jsonBody)
                .post("/create/user");

        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    public void emptyFields() {
        String jsonBody = "{\n" +
                "  \"firstName\": \"\",\n" +
                "  \"lastName\": \"\",\n" +
                "  \"phoneNumber\": ,\n" +
                "  \"emailId\": \"\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("roll-number", "1")
                .contentType("application/json")
                .body(jsonBody)
                .post("/create/user");

        Assert.assertEquals(response.getStatusCode(), 400);
    }
	
}
