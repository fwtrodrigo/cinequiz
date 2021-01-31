package br.com.cinequiz.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cinequiz.R
import br.com.cinequiz.adapters.ResultadoDialogAdapter
import br.com.cinequiz.databinding.ActivityJogoCenaBinding
import br.com.cinequiz.domain.Filme
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_jogo_cena.*
import kotlinx.android.synthetic.main.item_botoes_alternativas.*
import kotlinx.android.synthetic.main.item_botoes_alternativas.view.*
import kotlinx.android.synthetic.main.item_card_cena.*


class JogoCena : AppCompatActivity() {

    private lateinit var binding: ActivityJogoCenaBinding

    val viewModel by viewModels<JogoCenaViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return JogoCenaViewModel() as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoCenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var contadorFilme = 0
        val listaFilmes = intent.getSerializableExtra("listaFilmes") as List<Filme>

        Log.d("JOGOCENA2", listaFilmes.toString())

        if (listaFilmes != null) {
            viewModel.filmes.value = listaFilmes
        }

        iniciarFilme(listaFilmes, contadorFilme)

        var resultadoDialog = ResultadoDialogAdapter(10, "Cena", LoadingViewModel.ID_JOGO_CENA)

        binding.includeJogoCenaBotoes.imageButtonAlternativas1.setOnClickListener {
            contadorFilme++
            if(contadorFilme == listaFilmes.size){
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            }else{
                iniciarFilme(listaFilmes, contadorFilme)
            }
        }

    }

    fun iniciarFilme(listaFilmes: List<Filme>, contadorFilme: Int){

        Picasso.get().load("https://image.tmdb.org/t/p/w500" + listaFilmes[contadorFilme].imagensFilme[0].file_path)
            .into(imgFilmeCena)

        txtAlternativa1.text = listaFilmes[contadorFilme].title
        txtAlternativa2.text = listaFilmes[contadorFilme].filmesSimilares[0].title
        txtAlternativa3.text = listaFilmes[contadorFilme].filmesSimilares[1].title
        txtAlternativa4.text = listaFilmes[contadorFilme].filmesSimilares[2].title
    }
}