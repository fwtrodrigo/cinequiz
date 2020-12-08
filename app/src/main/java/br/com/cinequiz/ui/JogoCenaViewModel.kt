package br.com.cinequiz.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.cinequiz.domain.Filme

class JogoCenaViewModel(): ViewModel()  {

    val filmes = MutableLiveData<List<Filme>>()

}