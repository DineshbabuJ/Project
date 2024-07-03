package org.example;
import PojoClasses.PetResponse;
import PojoClasses.TagsItem;
import RequestPojo.Category;
import RequestPojo.PetRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiHits {
    Response response;
    ObjectMapper objectMapper=new ObjectMapper();
    private final String base_Uri="https://petstore.swagger.io/v2";
    File jsonBody= new File(getClass().getClassLoader().getResource("reqPojo.json").getFile());
    ReferenceVariables refId=new ReferenceVariables();

    public void getPetsAvailable(String pet,String status) throws JsonProcessingException {
        int petCount=0;
        response=RestAssured.given()
                .baseUri(base_Uri)
                .contentType(ContentType.JSON)
                .queryParam("status",status)
                .when()
                .get("pet/findByStatus")
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .extract().response();
//        System.out.println(response.body().asPrettyString());
        PetResponse[] pojo1=objectMapper.readValue(response.body().asPrettyString(),PetResponse[].class);

        for(PetResponse item:pojo1){
            if(item.getCategory()!=null && item.getCategory().getName()!=null &&item.getCategory().getName().toLowerCase().startsWith(pet)){
                petCount++;
            }
        }
        System.out.println("no of "+pet+" "+status+" :"+petCount);
    }

    void postPets(){
//        PetRequest petReqBody=new PetRequest();
//        petReqBody.setName("dog1");
//        Category category=new Category();
//        category.setId(1);
//        category.setName("dog");
//        petReqBody.setCategory(category);
//        List<String> url =new ArrayList<>();
//        url.add("dogPhoto1");
//        petReqBody.setPhotoUrls(url);
//        List<TagsItem> tags= new ArrayList<>();
//        TagsItem tagsItem=new TagsItem();
//        tagsItem.setId(123);

        response=RestAssured.given()
                .baseUri(base_Uri)
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("pet")
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .extract().response();
        refId.setId(response.jsonPath().get("id"));
        assertThat(response.jsonPath().getLong("id"), equalTo(refId.getId()));
        assertThat(response.getStatusCode(),equalTo(200));
        System.out.println(response.body().asPrettyString());
    }

    public void put(List<String> photoUrl ) throws IOException {
        PetRequest putReqBody=objectMapper.readValue(jsonBody,PetRequest.class);
        putReqBody.setId(refId.getId());
        putReqBody.setPhotoUrls(photoUrl);
        String pojoToJson=objectMapper.writeValueAsString(putReqBody);
        response=RestAssured.given()
                .baseUri(base_Uri)
                .contentType(ContentType.JSON)
                .body(pojoToJson)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .extract().response();
        assertThat(response.jsonPath().getLong("id"), equalTo(refId.getId()));
        System.out.println(response.body().asPrettyString());
    }


    public void delete() {
        response=RestAssured.given()
                .baseUri(base_Uri)
                .contentType(ContentType.JSON)
                .when()
                .delete("/pet/"+refId.getId())
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .extract().response();
       assertThat(response.jsonPath().get("type"),equalTo("unknown"));
        System.out.println("deleted successfully");
    }

    public void get(){
        response=RestAssured
                .given()
                .baseUri(base_Uri)
                .header("Content-Type", "application/json")
                .when()
                .get("/pet/"+refId.getId())
                .then()
                .statusCode(404)
                .extract().response();
        System.out.println(response.prettyPrint());
    }




}
