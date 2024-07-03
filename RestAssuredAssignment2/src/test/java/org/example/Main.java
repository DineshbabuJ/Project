package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        ApiHits apihits=new ApiHits();
        apihits.postPets();
        List<String> photoUrl=new ArrayList<>();
        photoUrl.add("photoDog123Updated");
        apihits.put(photoUrl);
        apihits.getPetsAvailable("dog","available");
        apihits.getPetsAvailable("cat","available");
        apihits.getPetsAvailable("dog","sold");
        apihits.getPetsAvailable("cat","sold");

        apihits.delete();
        apihits.get();

    }

}