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
    val listaFilmesUtilizaveis = mutableListOf<Filme>()

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

                val f = repository.getFilme(
                    filmeID,
                    apiKey,
                    "pt-BR"
                )

                val fSimilares = repository.getFilmesSimiliares(
                    filmeID,
                    apiKey,
                    "pt-BR"
                ).listaFilmesSimilares


                val imgsFilme = repository.getImagensFilme(
                    filmeID,
                    apiKey,
                    "null"
                ).listaImagensFilme

                f.imagensFilme = imgsFilme
                f.filmesSimilares = fSimilares
                filme.value = f

            } catch (e: Exception) {
                Log.e("MenuViewModel", e.toString())
            }
        }
    }
}

