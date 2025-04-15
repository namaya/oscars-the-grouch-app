package com.namaya.oscarsthegrouch_app.api

import com.namaya.oscarsthegrouch_app.model.Game
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }

    @Provides
    @Singleton
    fun provideBackendApiClient(retrofit: Retrofit): BackendApiClient {
        return retrofit.create(BackendApiClient::class.java)
    }
}

interface BackendApiClient {
    data class CreateUserRequest(val name: String)
    data class CreateUserResponse(val id : String, val name: String)
    @POST("api/users")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): CreateUserResponse

    data class ListAvatarsResponse(val avatars: List<String>)
    @GET("api/users/avatars")
    suspend fun listAvatars(): ListAvatarsResponse

    @GET("static/avatars/{avatar}")
    @Streaming
    suspend fun getAvatar(@Path("avatar") avatar: String): Response<ResponseBody>

    data class ListGamesResponse(val games: List<Game>)
    @GET("api/games")
    suspend fun listGames(@Header("Authorization") token: String): ListGamesResponse
}