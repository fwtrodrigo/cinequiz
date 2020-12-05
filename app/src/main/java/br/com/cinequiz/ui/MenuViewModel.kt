package br.com.cinequiz.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.FilmePopular
import br.com.cinequiz.service.Repository
import kotlinx.coroutines.launch

class MenuViewModel(val repository: Repository) : ViewModel() {

    val apiKey: String = ""

    val listaFilmesPopulares = MutableLiveData<List<FilmePopular>>()
    val filme = MutableLiveData<Filme>()
    val listaFilmesUtilizaveis = MutableLiveData<List<Filme>>()

    fun getResults() {

        viewModelScope.launch {
            try {

                listaFilmesPopulares.value = repository.getFilmesPopulares(
                    apiKey,
                    "pt-BR"
                ).listaFilmesPopulares

            } catch (e: Exception) {
                Log.e("MenuViewModel", e.toString())
            }

        }
    }

    fun getFilme(filmeID: Int) {
        viewModelScope.launch {
            try {

                filme.value = repository.getFilme(
                    filmeID,
                    apiKey,
                    "pt-BR"
                )

            } catch (e: Exception) {
                Log.e("MenuViewModel", e.toString())
            }
        }
    }

    fun getFilmesSimiliares(filmeID: Int) {
        viewModelScope.launch {
            try {

                val listaFilmesSimilares = repository.getFilmesSimiliares(
                    filmeID,
                    apiKey,
                    "pt-BR"
                ).listaFilmesSimilares
                filme.value?.filmesSimilares = listaFilmesSimilares
                Log.i(
                    "FILMES SIMILARES:",
                    filmeID.toString() + " " + listaFilmesSimilares.toString()
                )

            } catch (e: Exception) {
                Log.e("MenuViewModel", e.toString())
            }

        }
    }

    fun getImagensFilme(filmeID: Int) {
        viewModelScope.launch {
            try {

                val listaImagensFilme = repository.getImagensFilme(
                    filmeID,
                    apiKey,
                    "null"
                ).listaImagensFilme
                //filme.value?.imagensFilme = listaImagensFilme
                Log.i("IMAGENS:", filmeID.toString() + " " + listaImagensFilme?.toString())

            } catch (e: Exception) {
                Log.e("MenuViewModel", e.toString())
            }
        }

    }

}

