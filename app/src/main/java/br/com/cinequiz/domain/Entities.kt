package br.com.cinequiz.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class FiltroFilmes(@SerializedName("results") @Expose var listaFilmesVotados: List<FilmeVotado>)
class FilmesSimilares(@SerializedName("results") @Expose var listaFilmesSimilares: List<FilmeSimilar>)
class ImagensFilme(@SerializedName("backdrops") @Expose var listaImagensFilme: List<ImagemFilme>)
class PessoasFilme(@SerializedName("cast") @Expose var listaPessoasFilme: List<PessoaFilme>)


data class Filme(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var filmesSimilares: List<FilmeSimilar>,
    var imagensFilme: List<ImagemFilme>,
    var pessoasFilme: List<PessoaFilme>
) : Serializable {

    fun formataDataLancamento(): String{

        val BRAZIL = Locale("pt", "BR")
        val dataOriginal = SimpleDateFormat("yyyy-MM-dd").parse(release_date)
        val dataFormatada = SimpleDateFormat("MMMM/yyyy", BRAZIL)
        return dataFormatada.format(dataOriginal)
    }

}

data class Genre(
    val id: Int,
    val name: String
) : Serializable

data class ProductionCompany(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
) : Serializable

data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
) : Serializable

data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
) : Serializable

data class FilmeVotado(
    val id: Int
) : Serializable

data class FilmeSimilar(
    val id: Int,
    val title: String
) : Serializable

data class ImagemFilme(
    val file_path: String,
) : Serializable

data class PessoaFilme(
    val id: Int,
    val gender: Int,
    val known_for_department: String,
    val name: String,
    val character: String,
) : Serializable
