package com.example.tikoshopping.API;

import com.example.tikoshopping.Service.CartItem;
import com.example.tikoshopping.Service.FormAddProductIntoCart;
import com.example.tikoshopping.Service.Login;
import com.example.tikoshopping.Service.LoginResult;
import com.example.tikoshopping.Service.ResultAddProductIntoCart;
import com.example.tikoshopping.Service.ResultCart;
import com.example.tikoshopping.Service.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APICart {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    APICart apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.0.101:3000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APICart.class);

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJRFVzZXIiOjEsImFjY2VzcyI6MCwidXNlck5hbWUiOiJhZG1pbiIsImlhdCI6MTY4MTA0OTQxOSwiZXhwIjoxNjgzNjQxNDE5fQ.vHEF_WIe1D_g3IzdkjHRL4vGasFaNB3rlATBRZw89uE"
    })
    @GET("/api/cart/getall")
    Call<ResultCart> getAllProduct ();



    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJRFVzZXIiOjEsImFjY2VzcyI6MCwidXNlck5hbWUiOiJhZG1pbiIsImlhdCI6MTY4MTA0OTQxOSwiZXhwIjoxNjgzNjQxNDE5fQ.vHEF_WIe1D_g3IzdkjHRL4vGasFaNB3rlATBRZw89uE"
    })
    @POST("/api/cart/add")
    Call<ResultAddProductIntoCart> AddProductInCart(@Body FormAddProductIntoCart form);



}