package com.example.converter.data.api

import com.example.converter.data.model.Courses
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("daily_json.js")
    suspend fun getCourses():Response<Courses>
}