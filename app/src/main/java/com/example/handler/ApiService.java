package com.example.handler;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("woof.json")
    Call<DogResponse> getRandomDog();
}