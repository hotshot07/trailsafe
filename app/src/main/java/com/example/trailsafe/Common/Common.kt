package com.example.trailsafe.Common

import com.example.trailsafe.Remote.IGoogleAPIService
import com.example.trailsafe.Remote.RetrofitClient
import com.example.trailsafe.Remote.RetrofitScalarsClient

object Common {
    private val GOOOGLE_API_URL= "https://maps.googleapis.com/"

    //var currentResult:Results?=null

    val googleApiServiceScalars:IGoogleAPIService
        get()=RetrofitScalarsClient.getClient(GOOOGLE_API_URL).create(IGoogleAPIService::class.java)
}