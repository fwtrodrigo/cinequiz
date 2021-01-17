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

        if (listaFilmes != null) {
            viewModel.filmes = listaFilmes
        }

        novaPartida()

        val resultadoDialog = ResultadoDialogAdapter()

        binding.includeJogoDicaBotoes.imageButtonAlternativas1.setOnClickListener {
            viewModel.incrementaFilme()
            if(viewModel.contadorFilme == listaFilmes.size){
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            }else{
                viewModel.resultadoResposta("btn1")
                novaRodada()
            }
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas2.setOnClickListener {
            viewModel.incrementaFilme()
            if(viewModel.contadorFilme == listaFilmes.size){
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            }else{
                viewModel.resultadoResposta("btn2")
                novaRodada()
            }
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas3.setOnClickListener {
            viewModel.incrementaFilme()
            if(viewModel.contadorFilme == listaFilmes.size){
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            }else{
                viewModel.resultadoResposta("btn3")
                novaRodada()
            }
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas4.setOnClickListener {
            viewModel.incrementaFilme()
            if(viewModel.contadorFilme == listaFilmes.size){
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            }else{
                viewModel.resultadoResposta("btn4")
                novaRodada()
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

        viewModel.listaAlternativas.observe(this, {listaAlternativas ->
            binding.includeJogoDicaBotoes.txtAlternativa1.text = listaAlternativas[0]
            binding.includeJogoDicaBotoes.txtAlternativa2.text = listaAlternativas[1]
            binding.includeJogoDicaBotoes.txtAlternativa3.text = listaAlternativas[2]
            binding.includeJogoDicaBotoes.txtAlternativa4.text = listaAlternativas[3]
        })

        novaRodada()
    }

    fun novaRodada() {
        viewModel.gerarDicas()
        viewModel.gerarAlternativas()
    }

}
