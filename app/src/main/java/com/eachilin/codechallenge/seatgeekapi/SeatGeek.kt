package com.eachilin.codechallenge.seatgeekapi

import com.eachilin.codechallenge.EventResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SeatGeek {

    @GET("events?")
    fun getSeatGeekInfo(
        @Query("client_id") client:String,
        @Query("q") q:String
    ): Call<EventResult>
}