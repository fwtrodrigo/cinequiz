package br.com.cinequiz.ui

import android.animation.Animator
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.cinequiz.R
import br.com.cinequiz.adapters.CancelaJogoDialogAdapter
import br.com.cinequiz.adapters.ResultadoDialogAdapter
import br.com.cinequiz.databinding.ActivityJogoDicaBinding
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.Parametros
import br.com.cinequiz.domain.SingletonListaFilmes
import br.com.cinequiz.room.CinequizApplication
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class JogoDica : AppCompatActivity() {

    private lateinit var binding: ActivityJogoDicaBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var mAuth: FirebaseAuth
    private lateinit var animacaoRespostaCorreta: LottieAnimationView
    private lateinit var animacaoRespostaErrada: LottieAnimationView
    val scope = CoroutineScope(Dispatchers.Main)

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

        val listaFilmes = SingletonListaFilmes.filmes

        mAuth = FirebaseAuth.getInstance()
        val idUsuario = mAuth.currentUser?.uid
        prefs = getSharedPreferences("userPrefs_$idUsuario", MODE_PRIVATE)

        animacaoRespostaCorreta = binding.animacaoRespostaCorreta
        animacaoRespostaErrada = binding.animacaoRespostaErrada
        inicializaAnimacao(animacaoRespostaCorreta, Parametros.ID_RESPOSTA_CORRETA)
        inicializaAnimacao(animacaoRespostaErrada, Parametros.ID_RESPOSTA_ERRADA)

        jogoDicaViewModel.inicializaAudios()
        jogoDicaViewModel.filmes = listaFilmes

        if (prefs.getBoolean("musica", true)) {
            jogoDicaViewModel.tocarMusica()
        }

        novaPartida()

        binding.includeJogoDicaBotoes.imageButtonAlternativas1.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn1")
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas2.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn2")
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas3.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn3")
        }

        binding.includeJogoDicaBotoes.imageButtonAlternativas4.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn4")
        }

        binding.itemProximaDica.btnProximaDica.setOnClickListener {
            vibrarBotão()
            jogoDicaViewModel.proximaDica(Parametros.PONTUACAO_PROXIMA_DICA_JOGO_DICA)
        }
    }

    fun novaPartida() {

        jogoDicaViewModel.animacaoResposta.observe(this, { resposta ->
            if (resposta == 1) {
                if (prefs.getBoolean("sons", true)) jogoDicaViewModel.tocarRespostaCorreta()
                executaAnimacao(animacaoRespostaCorreta)
            } else {
                if (prefs.getBoolean("sons", true)) jogoDicaViewModel.tocarRespostaErrada()
                executaAnimacao(animacaoRespostaErrada)
            }
        })

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
            binding.listviewCenaDica.setSelection(binding.listviewCenaDica.getAdapter().getCount()-1);

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

        if (jogoDicaViewModel.contadorFilme >= jogoDicaViewModel.filmes.size) {

            binding.includeJogoDicaBotoes.imageButtonAlternativas1.isClickable = false
            binding.includeJogoDicaBotoes.imageButtonAlternativas2.isClickable = false
            binding.includeJogoDicaBotoes.imageButtonAlternativas3.isClickable = false
            binding.includeJogoDicaBotoes.imageButtonAlternativas4.isClickable = false

            jogoDicaViewModel.resultadoResposta(botaoPressionado)

            scope.launch {
                delay(600)

                encerraPartida()

                val resultadoDialog =
                    ResultadoDialogAdapter(
                        jogoDicaViewModel.pontuacao.value!!,
                        "dica",
                        Parametros.ID_JOGO_DICA,
                        prefs
                    )
                resultadoDialog.isCancelable = false
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
                jogoDicaViewModel.update()

            }

        } else {
            jogoDicaViewModel.resultadoResposta(botaoPressionado)
            novaRodada()
        }
    }

    fun encerraPartida() {

        if (prefs.getBoolean("musica", true)) {
            jogoDicaViewModel.pararMusica()
        }
    }

    fun inicializaAnimacao(animacao: LottieAnimationView, codAnimacao: Int) {

        animacao.addAnimatorListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator?) {

                if(codAnimacao == Parametros.ID_RESPOSTA_ERRADA) {
                    binding.layoutDicas.setBackgroundResource(R.drawable.shape_card_fundo_dica_vermelho)
                }
                Log.i("ANIMATION", "INICIANDO ANIMACAO")
            }

            override fun onAnimationRepeat(animation: Animator?) {
                Log.i("ANIMATION", "REPETINDO ANIMACAO")
            }

            override fun onAnimationEnd(animation: Animator?) {

                if(codAnimacao == Parametros.ID_RESPOSTA_ERRADA) {
                    binding.layoutDicas.setBackgroundResource(R.drawable.shape_card_fundo_dica)
                }
                animacao.visibility = View.GONE
                Log.i("ANIMATION", "FINALIZANDO ANIMACAO")
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.i("ANIMATION", "CANCELANDO ANIMACAO")
            }
        })

        if (codAnimacao == 1) {
            animacao.setAnimation(R.raw.correct_animation)
        } else {
            animacao.setAnimation(R.raw.wrong_animation)
        }
        animacao.speed = 1.8F
    }

    fun executaAnimacao(animacao: LottieAnimationView) {
        animacao.visibility = View.VISIBLE
        animacao.playAnimation()
    }

    fun vibrarBotão() {
        val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(150)
        }
    }

    override fun onBackPressed() {

        val cancelaJogoDialog = CancelaJogoDialogAdapter(jogoDicaViewModel.musicaJogoDica)
        cancelaJogoDialog.isCancelable = false
        cancelaJogoDialog.show(supportFragmentManager, "cancelaJogoDialog")
    }

}
