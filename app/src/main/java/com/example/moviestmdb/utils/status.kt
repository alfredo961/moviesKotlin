package com.example.moviestmdb.utils

import com.example.moviestmdb.model.Movie
import com.example.moviestmdb.model.MovieResponse

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}

class Resource<T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            if (data is List<*>) {
                for (item in data) {
                    if (item !is Movie) {
                        throw IllegalArgumentException("Type mismatch: expected List<Movie>")
                    }
                }
            }
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
