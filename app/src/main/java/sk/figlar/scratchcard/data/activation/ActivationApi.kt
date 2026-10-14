package sk.figlar.scratchcard.data.activation

import retrofit2.http.GET
import retrofit2.http.Query

interface ActivationApi {
    @GET("version")
    suspend fun checkActivation(
        @Query("code") code: String,
    ): ActivationResponse
}