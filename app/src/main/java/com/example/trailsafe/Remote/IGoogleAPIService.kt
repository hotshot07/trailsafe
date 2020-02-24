package com.example.trailsafe.Remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import  retrofit2.http.Url

interface IGoogleAPIService {
    @GET("maps/api/directions/json")
    fun getDirections(@Query("origin")origin:String,@Query("destination") destination:String):Call<String>
}