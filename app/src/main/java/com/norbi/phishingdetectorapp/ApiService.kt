package com.norbi.phishingdetectorapp.network

import retrofit2.http.Body
import retrofit2.http.POST

data class ClassificationResult(
    val label: String,
    val confidence: Float
)

interface ApiService {
    @POST("/classify")
    suspend fun classify(@Body payload: Map<String, String>): ClassificationResult
}
