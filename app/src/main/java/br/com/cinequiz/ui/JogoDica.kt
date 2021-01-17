package br.com.cinequiz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cinequiz.R
import br.com.cinequiz.adapters.ResultadoDialogAdapter
import br.com.cinequiz.databinding.ActivityJogoDicaBinding
import br.com.cinequiz.domain.Filme
import kotlinx.android.synthetic.main.item_botoes_alternativas.*

class JogoDica : AppCompatActivity() {

    private lateinit var binding: ActivityJogoDicaBinding

    val viewModel by viewModels<JogoDicaViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return JogoDicaViewModel(application) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoDicaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listaFilmes = intent.getSerializableExtra("listaFilmes") as ArrayList<Filme>
        var contadorFilme = 0

        if (listaFilmes != null) {
            viewModel.filmes = listaFilmes
        }

        novaPartida()

        iniciarFilme(listaFilmes, contadorFilme)

        val resultadoDialog = ResultadoDialogAdapter()

        binding.includeJogoDicaBotoes.imageButtonAlternativas1.setOnClickListener {
            contadorFilme++
            if(contadorFilme == listaFilmes.size){
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            }else{
                iniciarFilme(listaFilmes, contadorFilme)
            }
        }

        binding.itemProximaDica.btnProximaDica.setOnClickListener {
            viewModel.proximaDica(10)
        }
    }

    fun novaPartida(){
        viewModel.pontuacao.observe(
            this,    { pontuacao ->
                binding.itemPontuacaoCena.textViewPontosDicas.text = pontuacao.toString()
            }
        )

        viewModel.listaDicas.observe(this, { listaDicas ->
            val adapter = ArrayAdapter(this, R.layout.item_lista_dica, listaDicas)
            listaDicas.forEach { Log.i("JogoDica", it)}
            binding.listviewCenaDica.adapter = adapter

        })

        novaRodada()
    }

    fun novaRodada() {
        viewModel.gerarDicas()
        //gerarAlternativas()
    }

    private fun gerarAlternativas() {
        TODO("Not yet implemented")
    }

    fun iniciarFilme(listaFilmes: List<Filme>, contadorFilme: Int){

        txtAlternativa1.text = listaFilmes[contadorFilme].title
        txtAlternativa2.text = listaFilmes[contadorFilme].filmesSimilares[0].title
        txtAlternativa3.text = listaFilmes[contadorFilme].filmesSimilares[1].title
        txtAlternativa4.text = listaFilmes[contadorFilme].filmesSimilares[2].title
    }
}
