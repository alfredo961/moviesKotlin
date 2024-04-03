package com.example.moviestmdb.repository

import com.example.moviestmdb.model.MovieResponse
import com.example.moviestmdb.BuildConfig


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface TmdbApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY, @Query("page") page: Int): Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY, @Query("page") page: Int): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY, @Query("page") page: Int): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY, @Query("page") page: Int): Response<MovieResponse>
}


class MovieRepository(private val tmdbApi: TmdbApi) {

    suspend fun getNowPlayingMovies(page: Int): Result<MovieResponse> {
        return try {
            val response = tmdbApi.getNowPlayingMovies(page = page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Body is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPopularMovies(page: Int): Result<MovieResponse> {
        return try {
            val response = tmdbApi.getPopularMovies(page = page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Body is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopRatedMovies(page: Int): Result<MovieResponse> {
        return try {
            val response = tmdbApi.getTopRatedMovies(page = page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Body is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUpcomingMovies(page: Int): Result<MovieResponse> {
        return try {
            val response = tmdbApi.getUpcomingMovies(page = page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Body is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

