package com.example.payfromhome.api;

import com.example.payfromhome.model.User;
import com.example.payfromhome.model.mAuth;
import com.example.payfromhome.model.mResponse;
import com.example.payfromhome.model.mTransaction;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login") Call<mAuth> auth_login(
            @FieldMap HashMap<String, String> Form
    );

    @FormUrlEncoded
    @POST("register") Call<mAuth> auth_signup(
            @FieldMap HashMap<String, String> Form
    );

    @FormUrlEncoded
    @POST("users/getbalance") Call<mAuth> getBalance(
            @FieldMap HashMap<String, String> Form
    );

    @FormUrlEncoded
    @POST("transaction/create") Call<mTransaction> createdTransaction(
            @FieldMap HashMap<String, String> Form
    );

    @FormUrlEncoded
    @POST("transaction/delete") Call<mTransaction> deleteTransaction(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("transaction") Call<mResponse> getTransaction(
            @Field("user_id") String user_id
    );

    @GET("users/{id}") Call<mAuth> getUser(
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST("users") Call<mAuth> updateUser(
            @FieldMap HashMap<String, String> Form
    );
}
