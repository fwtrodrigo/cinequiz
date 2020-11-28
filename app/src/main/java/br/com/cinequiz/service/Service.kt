package br.com.cinequiz.service

import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.FiltroFilmes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Repository {

    @GET("movie/popular")
    suspend fun getFilmesPopulares(
        @Query("api_key") api_key: String,
        @Query("language") idioma: String = "pt-BR",
    ): FiltroFilmes

    @GET("movie/{movie_id}")
    suspend fun getFilme(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") idioma: String = "pt-BR",
    ): Filme
}

val urlApiTMDB = "https://api.themoviedb.org/3/"
val retrofit = Retrofit.Builder()
    .baseUrl(urlApiTMDB)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val repository: Repository = retrofit.create(Repository::class.java)

