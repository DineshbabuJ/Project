package stepDefinitions;
import PojoClasses.PetResponse;
import io.cucumber.datatable.DataTable;
import PojoForListOfUser.ListOfUserResponseItem;
import RequestPojo.PetRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONArray;
import org.example.ReferenceVariables;
import org.junit.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MyStepdefs {
    RequestSpecification requestSpecification;
    Response response;
    ObjectMapper objectMapper=new ObjectMapper();
    File jsonBody= new File(getClass().getClassLoader().getResource("reqPojo.json").getFile());

//    File prop=new File(getClass().getClassLoader().getResource("Config.Properties").getFile());
//    FileInputStream fis=new FileInputStream(prop);

    ReferenceVariables refId= ReferenceVariables.getInstance();
    List<String> photoUrl=new ArrayList<>();
    PetRequest putReqBody=objectMapper.readValue(jsonBody,PetRequest.class);
    File listUsers;
    ListOfUserResponseItem[] listOfUserResponse;
    private List<Map<String, Object>> users;

    public MyStepdefs() throws IOException {
    }

    @Given("user has access to api endpoint of {string}")
    public void userHasAccessToApiEndpointOf(String uri) {
         requestSpecification= RestAssured.given()
                .baseUri(uri)
                .contentType(ContentType.JSON);
    }

    @When("update the photoUrl with {string}")
    public void updateThePhotoUrlWith(String url) {
        photoUrl.add(url);
    }

    @When("user hits the api with {string} method")
    public void userHitsTheApiWithMethod(String method) throws IOException {
        if("post".equals(method)){
            System.out.println("Adding pet");
            response=requestSpecification.body(jsonBody)
                    .when()
                    .post("pet").then()
                    .body(matchesJsonSchemaInClasspath("petSchema.json")).extract().response();

            putReqBody.setId(response.jsonPath().getLong("id"));
            refId.setId(response.jsonPath().getLong("id"));
            System.out.println(response.body().asPrettyString());

        }
        else if("get".equals(method)){
            response=requestSpecification
                    .when()
                    .get("/pet/"+refId.getId())
                    .then()
                    .statusCode(404)
                    .extract().response();
            System.out.println("retrieving deleted pet "+response.prettyPrint());
        }
        else if("put".equals(method)){
            System.out.println("Update pet photoUrl ");
            putReqBody.setId(refId.getId());
            putReqBody.setPhotoUrls(photoUrl);
            String pojoToJson=objectMapper.writeValueAsString(putReqBody);
            response=requestSpecification
                    .body(pojoToJson)
                    .when()
                    .put("/pet").then()
                    .body(matchesJsonSchemaInClasspath("petSchema.json")).extract().response();
            assertThat(response.jsonPath().getList("photoUrls"),equalTo(photoUrl));
            assertThat(response.jsonPath().getLong("id"),equalTo(refId.getId()));
            System.out.println(response.body().asPrettyString());
        }
        else{
            response=requestSpecification
                    .when()
                    .delete("/pet/"+refId.getId());
            assertThat(response.jsonPath().get("type"),equalTo("unknown"));
            System.out.println("deleted successfully");
        }
    }

    @When("^user enters (.*) and (.*) hits api to find the count$")
    public void userEntersPetAndStatusToFindTheCount(String pet,String status) throws JsonProcessingException {
        int petCount=0;
        response=requestSpecification
                .queryParam("status",status)
                .when()
                .get("pet/findByStatus")
                .then()
                .body(matchesJsonSchemaInClasspath("petSchemaArray.json"))
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .extract().response();
        String responseBody = response.body().asPrettyString();

        Configuration config = Configuration.builder()
                .options(Option.DEFAULT_PATH_LEAF_TO_NULL)
                .build();
        String jsonPathExpression = String.format("$.[?(@.category.name =~ /%s.*/i)]", pet);

        JSONArray matchingElements = JsonPath.using(config).parse(responseBody).read(jsonPathExpression);
        petCount = matchingElements.size();
        System.out.println("no of "+pet+" "+status+" :"+petCount);
    }

    @Then("gets response code {int}")
    public void getsResponseCode(int code) {
        response.then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .log().all()
                .extract().response();
        assertThat(response.getStatusCode(),equalTo(code));
//        assertThat(response.jsonPath().getLong("id"),equalTo(refId.getId()));
    }

    @Given("user has access to api endpoint of {string} to upload image")
    public void userHasAccessToApiEndpointOfToUploadImage(String baseUrl) {
        File imgFile=new File(getClass().getClassLoader().getResource("dogImage.webp").getFile());
        requestSpecification=RestAssured.given()
                .multiPart("file",imgFile,"multipart/form-data");

    }
    @When("user upload the file using postMethod")
    public void userUploadTheFileUsingPostMethod() {
        response=requestSpecification.post("https://petstore.swagger.io/v2/pet/1/uploadImage")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println("image uploaded successfully");

    }
    @Then("print the file upload message")
    public void printTheFileUploadMessage() {
        System.out.println(response.jsonPath().getString("message"));
    }

    @When("user hits api with list of users as body")
    public void userHitsApiWithListOfUsersAsBody(DataTable dataTable) throws JsonProcessingException {
        users = new ArrayList<>();
        List<Map<String, String>> userRows =dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : userRows) {
            Map<String, Object> user = new HashMap<>();
            user.put("username", row.get("username"));
            user.put("firstName", row.get("firstName"));
            user.put("lastName", row.get("lastName"));
            user.put("email", row.get("email"));
            user.put("password", row.get("password"));
            user.put("phone", row.get("phone"));
            user.put("userStatus", Integer.parseInt(row.get("userStatus")));
            users.add(user);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String usersJson = objectMapper.writeValueAsString(users);
        response=requestSpecification
                .body(usersJson)
                .when()
                .post("user/createWithList")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

    }

    @Then("validate list of users by getting it")
    public void validateListOfUsersByGettingIt() {
        response=null;
        for (Map<String, Object> user : users) {
            String username = (String) user.get("username");
            response = requestSpecification
                    .when()
                    .get("user/"+username);
            System.out.println("Get User Response for " + username + ": " +response.asString());
            Map<String, Object> userData = response.jsonPath().getMap("$");
            assertEquals(user.get("username"), userData.get("username"));
            assertEquals(user.get("firstName"), userData.get("firstName"));
            assertEquals(user.get("lastName"), userData.get("lastName"));
            assertEquals(user.get("email"), userData.get("email"));
            assertEquals(user.get("password"), userData.get("password"));
            assertEquals(user.get("phone"), userData.get("phone"));
            assertEquals(user.get("userStatus"), userData.get("userStatus"));
    }
}}