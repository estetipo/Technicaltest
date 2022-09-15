package com.carlos.satori.technical_test.api

object ApiConstants {
    //Static routes to consume the api
    private const val serverPath = "https://api.themoviedb.org/3/movie/"
    const val imagePath = "https://image.tmdb.org/t/p/w600_and_h900_bestv2"
    fun getServerPath(): String {
        return serverPath
    }
}