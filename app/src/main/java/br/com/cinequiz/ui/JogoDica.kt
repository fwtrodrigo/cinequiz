package br.com.cinequiz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import br.com.cinequiz.R
import br.com.cinequiz.adapters.ResultadoDialogAdapter
import br.com.cinequiz.databinding.ActivityJogoDicaBinding
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.room.CinequizApplication


class JogoDica : AppCompatActivity() {

    private lateinit var binding: ActivityJogoDicaBinding

    private val jogoDicaViewModel: JogoDicaViewModel by viewModels {
        JogoDicaViewModelFactory(
            application,
            (application as CinequizApplication).repositoryUsuarioRecorde
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoDicaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listaFilmes = intent.getSerializableExtra("listaFilmes") as ArrayList<Filme>

        if (listaFilmes != null) {
            jogoDicaViewModel.filmes = listaFilmes
        }

        novaPartida()

        binding.includeJogoDicaBotoes.imageButtonAlternativas1.setOnClickListener {
            selecaoAlternativa("btn1")
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas2.setOnClickListener {
            selecaoAlternativa("btn2")
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas3.setOnClickListener {
            selecaoAlternativa("btn3")
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas4.setOnClickListener {
            selecaoAlternativa("btn4")
        }

        binding.itemProximaDica.btnProximaDica.setOnClickListener {
            jogoDicaViewModel.proximaDica(10)
        }
    }

    fun novaPartida() {

        jogoDicaViewModel.usuarioRecordeLiveData.observe(this, {
            jogoDicaViewModel.usuarioRecorde = it
        })

        jogoDicaViewModel.pontuacao.observe(
            this, { pontuacao ->
                binding.itemPontuacaoCena.textViewPontosDicas.text = pontuacao.toString()
            }
        )

        jogoDicaViewModel.listaDicas.observe(this, { listaDicas ->
            val adapter = ArrayAdapter(this, R.layout.item_lista_dica, listaDicas)
            listaDicas.forEach { Log.i("JogoDica", it) }
            binding.listviewCenaDica.adapter = adapter

        })

        jogoDicaViewModel.listaAlternativas.observe(this, { listaAlternativas ->
            binding.includeJogoDicaBotoes.txtAlternativa1.text = listaAlternativas[0]
            binding.includeJogoDicaBotoes.txtAlternativa2.text = listaAlternativas[1]
            binding.includeJogoDicaBotoes.txtAlternativa3.text = listaAlternativas[2]
            binding.includeJogoDicaBotoes.txtAlternativa4.text = listaAlternativas[3]
        })

        novaRodada()
    }

    fun novaRodada() {
        jogoDicaViewModel.gerarDicas()
        jogoDicaViewModel.gerarAlternativas()
    }

    fun selecaoAlternativa(botaoPressionado: String) {
        jogoDicaViewModel.incrementaFilme()
        if (jogoDicaViewModel.contadorFilme == jogoDicaViewModel.filmes.size) {
            val resultadoDialog =
                ResultadoDialogAdapter(jogoDicaViewModel.pontuacao.value!!, "dica", LoadingViewModel.ID_JOGO_DICA)
            resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            jogoDicaViewModel.update()

        } else {
            jogoDicaViewModel.resultadoResposta(botaoPressionado)
            novaRodada()
        }
    }
}
