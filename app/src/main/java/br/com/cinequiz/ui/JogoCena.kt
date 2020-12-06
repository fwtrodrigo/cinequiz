package br.com.cinequiz.ui


import android.accounts.AccountManager.get
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cinequiz.R
import br.com.cinequiz.domain.Filme
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_botao_selecao_modo_cena.*
import kotlinx.android.synthetic.main.item_botoes_alternativas.*
import kotlinx.android.synthetic.main.item_card_cena.*
import java.lang.reflect.Array.get
import java.nio.file.Paths.get

class JogoCena : AppCompatActivity() {

    val viewModel by viewModels<JogoCenaViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return JogoCenaViewModel() as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogo_cena)

        val listaFilmes = intent.getSerializableExtra("listaFilmes") as List<Filme>

        Log.d("JOGOCENA2", listaFilmes.toString())

        if (listaFilmes != null) {
            viewModel.filmes.value = listaFilmes

            Picasso.get().load("https://image.tmdb.org/t/p/w500" + listaFilmes[0].imagensFilme[0].file_path)
                .into(imgFilmeCena)

            txtAlternativa1.text = listaFilmes[0].title
            txtAlternativa2.text = listaFilmes[0].filmesSimilares[0].title
            txtAlternativa3.text = listaFilmes[0].filmesSimilares[1].title
            txtAlternativa4.text = listaFilmes[0].filmesSimilares[2].title
        }

    }
}