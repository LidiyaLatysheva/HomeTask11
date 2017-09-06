package com.epam.latysheva.test;

import com.epam.latysheva.businessObject.Constant;
import com.epam.latysheva.businessObject.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SimpleTest {

    @BeforeClass
    public void initTest() {
        RestAssured.baseURI = Constant.BASE_URL;
    }

    @Test
    public void verifyStatusCode() {
        Response response = given().get("/users").andReturn();
        Assert.assertEquals(response.getStatusCode(), Constant.STATUS_CODE, "Incorrect status code" + response.getStatusCode());
    }

    @Test(dependsOnMethods = "verifyStatusCode")
    public void verifyResponseHeader() {
        Response response = given().get("/users").andReturn();
        Assert.assertTrue(response.headers().hasHeaderWithName(Constant.HEADER_NAME), "Response does not contain Content Type Header");
        String contentTypeHeaderValue = response.getHeader(Constant.HEADER_NAME);
        Assert.assertTrue(contentTypeHeaderValue.contains(Constant.CONTENT_TYPE_HEADER_VALUE), "Content header does not contain required text");
    }

    @Test(dependsOnMethods = "verifyResponseHeader")
    public void verifyResponseBody() {
        Response response = given().get("/users").andReturn();
        User[] numberOfUsers = response.as(User[].class);
        Assert.assertEquals(numberOfUsers.length, Constant.USER_COUNT_IN_RESPONSE, "Number of users is not equal to 10");
    }

    @AfterClass
    public void tearDown() {
        RestAssured.baseURI = null;
    }

}
