package com.example.moviestmdb.api

import com.example.moviestmdb.repository.TmdbApi
import com.example.moviestmdb.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieService {

    val tmdbApi: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }
}

