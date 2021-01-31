package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.cinequiz.R
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.service.repository
import java.io.Serializable

class LoadingActivity : AppCompatActivity() {

    private val loadingViewModel: LoadingViewModel by viewModels {
        LoadingViewModelFactory(
            repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val idJogo = intent.getIntExtra("id_jogo", LoadingViewModel.ID_JOGO_DICA)
        val requisitoQuantidadeFilmes =
            intent.getIntExtra("quantidade_filmes", LoadingViewModel.QUANTIDADE_INICIAL_FILMES_DICA)

        loadingViewModel.getResults()

        loadingViewModel.listaFilmesVotados.observe(this) {
            for (filme in it) {
                loadingViewModel.getFilme(filme.id)
                Log.i("listaFilmesVotados", filme.id.toString())
            }

            Log.i("QDT_UTILIZAVEIS", loadingViewModel.listaFilmesUtilizaveis.size.toString())
            if (loadingViewModel.listaFilmesUtilizaveis.size < requisitoQuantidadeFilmes) {
                loadingViewModel.getResults()

            } else {

                val a = if (idJogo == LoadingViewModel.ID_JOGO_DICA) {
                    JogoDica::class.java
                } else {
                    JogoCena::class.java
                }

                val intent: Intent = Intent(this, a)
                val listaFilmes = mutableListOf<Filme>()
                listaFilmes.addAll(loadingViewModel.listaFilmesUtilizaveis.subList(0, requisitoQuantidadeFilmes))
                listaFilmes.shuffle()

                Log.i("qdtFilmes", listaFilmes.size.toString())

                intent.putExtra(
                    "listaFilmes",
                    listaFilmes as Serializable
                )

                startActivity(intent)
                finish()
            }

        }

    }
}