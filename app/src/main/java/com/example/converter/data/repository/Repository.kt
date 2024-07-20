package com.example.converter.data.repository

import com.example.converter.data.api.RetrofitInstance
import com.example.converter.data.model.Courses
import retrofit2.Response

class Repository {
    private val api = RetrofitInstance.api

    suspend fun getCourses(): Response<Courses> {
        return api.getCourses()
    }
}
