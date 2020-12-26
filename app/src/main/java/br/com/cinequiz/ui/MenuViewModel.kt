package br.com.cinequiz.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.FilmeVotado
import br.com.cinequiz.domain.PessoaFilme
import br.com.cinequiz.service.Repository
import kotlinx.coroutines.launch

class MenuViewModel(val repository: Repository) : ViewModel() {

    val apiKey: String = ""

    val listaFilmesVotados = MutableLiveData<List<FilmeVotado>>()
    val listaFilmesUtilizaveis = mutableListOf<Filme>()

    fun getResults() {
        viewModelScope.launch {
            try {

                listaFilmesVotados.value = repository.getFilmesVotados(
                    apiKey,
                    (1..10).random(),
                    "pt-BR"
                ).listaFilmesVotados

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


                val fImagens = repository.getImagensFilme(
                    filmeID,
                    apiKey,
                    "null"
                ).listaImagensFilme

                val fPessoas = repository.getPessoasFilme(
                    filmeID,
                    apiKey,
                    "pt-BR"
                ).listaPessoasFilme

                f.imagensFilme = fImagens
                f.filmesSimilares = fSimilares
                f.pessoasFilme = fPessoas

                if (filtroFilmeUtilizavel(f)) {
                    Log.i("MENUACTIVITY", "Adicionado: ${f.id.toString()}")
                    listaFilmesUtilizaveis.add(f)

                } else {
                    Log.i("MENUACTIVITY", "Não adicionado: ${f.id.toString()}")
                }

            } catch (e: Exception) {
                Log.e("MenuViewModel", e.toString())
            }
        }
    }

    private fun filtroFilmeUtilizavel (filme: Filme): Boolean {

        return when {
            filme.equals(null) -> false
            filme.title.isEmpty() -> false
            filme.production_companies.isEmpty() -> false
            filme.release_date.isEmpty() -> false
            filme.pessoasFilme.isEmpty() -> false
            filme.imagensFilme.isEmpty() -> false
            filme.imagensFilme[0].file_path.isEmpty() -> false
            filme.filmesSimilares.isEmpty() -> false
            filme.filmesSimilares[0].title.isEmpty() -> false
            else -> true
        }
    }
}

