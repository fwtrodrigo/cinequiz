package br.com.cinequiz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import br.com.cinequiz.R
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

        loadingViewModel.getResults()

        loadingViewModel.listaFilmesVotados.observe(this) {
            for (filme in it) {
                loadingViewModel.getFilme(filme.id)
                Log.i("listaFilmesVotados", filme.id.toString() )
            }

            Log.i("QDT_UTILIZAVEIS", loadingViewModel.listaFilmesUtilizaveis.size.toString() )
            if(loadingViewModel.listaFilmesUtilizaveis.size < 10 ){
                loadingViewModel.getResults()

            }else{
                val intent: Intent = Intent(this, JogoDica::class.java)
                    .putExtra("listaFilmes", loadingViewModel.listaFilmesUtilizaveis as Serializable)

                startActivity(intent)
                finish()
            }

        }

    }
}