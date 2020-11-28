package br.com.cinequiz.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cinequiz.BuildConfig
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.FilmePopular
import br.com.cinequiz.service.Repository
import kotlinx.coroutines.launch

class MenuViewModel(val repository: Repository) : ViewModel() {
    val apiKey: String = ""

    val listaFilmesPopulares = MutableLiveData<List<FilmePopular>>()
    val filme = MutableLiveData<Filme>()

    fun getResults() {

        try {
            viewModelScope.launch {
                listaFilmesPopulares.value = repository.getFilmesPopulares(
                    apiKey,
                    "pt-BR"
                ).listaFilmesPopulares
            }
        } catch (e: Exception) {
            Log.e("MenuViewModel", e.toString())
        }
    }

    fun getFilme(filmeID: Int) {
        try {
            viewModelScope.launch {
                filme.value = repository.getFilme(
                    filmeID,
                    apiKey,
                    "pt-BR"
                )
            }
        } catch (e: Exception) {
            Log.e("MenuViewModel", e.toString())
        }
    }
}
