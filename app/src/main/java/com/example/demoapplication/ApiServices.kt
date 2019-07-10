package com.example.demoapplication

import retrofit2.Call
import retrofit2.http.GET


/**
 * Created by Manish Verma on 08,Jul,2019
 * Girnarsoft Pvt. Ltd.,
 * Delhi NCR, India
 */
public interface ApiServices {

    @GET("placejson/favplace")
    fun listRepos(): Call<PlaceModel>

}