package br.com.cinequiz.adapters

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import br.com.cinequiz.R
import br.com.cinequiz.domain.Parametros
import br.com.cinequiz.ui.LoadingActivity
import br.com.cinequiz.ui.MenuActivity



class ResultadoDialogAdapter(
    private val pontos: Int,
    private val jogo: String,
    private val idJogo: Int,
    private val prefs: SharedPreferences
) : DialogFragment() {

    lateinit var somAplausos: MediaPlayer
    lateinit var somItemSelecionado: MediaPlayer
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogStyle);
    }

    override fun dismiss() {
        super.dismiss()
        getActivity()?.finish();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.layout_resultado_jogo, container, false)
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.color.cineSombraAzul)

        somItemSelecionado = MediaPlayer.create(context, R.raw.item_menu_som)
        somAplausos = MediaPlayer.create(context, R.raw.aplausos_som)
        
        var btnJogarNovamente = rootView.findViewById<Button>(R.id.btnResultadoJogarNovamente)
        var btnVoltarMenu = rootView.findViewById<Button>(R.id.btnResultadoVoltarMenu)
        var ibCompartilhar = rootView.findViewById<ImageButton>(R.id.ibCompartilhar)
        var txtResultadoPontos = rootView.findViewById<TextView>(R.id.txtResultadoPontos)
        txtResultadoPontos.text = pontos.toString()

        btnJogarNovamente.setOnClickListener {

            if (prefs.getBoolean("sons", true)){
                somItemSelecionado.setOnCompletionListener (object: MediaPlayer.OnCompletionListener{
                    override fun onCompletion(p0: MediaPlayer?) {
                        somItemSelecionado.release()
                    }
                })
                somItemSelecionado.start()
            }

            val intent = if (idJogo == Parametros.ID_JOGO_DICA) {
                Intent(activity, LoadingActivity::class.java)
                    .putExtra(Parametros.CHAVE_JOGO, Parametros.ID_JOGO_DICA)
                    .putExtra(
                        Parametros.CHAVE_QUANTIDADE_FILMES,
                        Parametros.QUANTIDADE_INICIAL_FILMES_DICA
                    )
            } else {
                Intent(activity, LoadingActivity::class.java)
                    .putExtra(Parametros.CHAVE_JOGO, Parametros.ID_JOGO_CENA)
                    .putExtra(
                        Parametros.CHAVE_QUANTIDADE_FILMES,
                        Parametros.QUANTIDADE_INICIAL_FILMES_CENA
                    )
            }
            startActivity(intent)
        }

        btnVoltarMenu.setOnClickListener {
            if (prefs.getBoolean("sons", true)){
                somItemSelecionado.setOnCompletionListener (object: MediaPlayer.OnCompletionListener{
                    override fun onCompletion(p0: MediaPlayer?) {
                        somItemSelecionado.release()
                    }
                })
                somItemSelecionado.start()
            }

            var intent = Intent(activity, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }

        ibCompartilhar.setOnClickListener {
            //implementar compartilhamento.
            compartilhar(jogo)
        }

        return rootView
    }

    override fun onStart() {
        if (prefs.getBoolean("sons", true)){
            somAplausos.setOnCompletionListener (object: MediaPlayer.OnCompletionListener{
                override fun onCompletion(p0: MediaPlayer?) {
                    somAplausos.release()
                }
            })
            somAplausos.start()
        }
        super.onStart()
    }

    private fun compartilhar(jogo: String) {
        val titulo = "Compartilhe o seu resultado com os seus amigos!"
        val mensagem =
            "Eu obtive $pontos pontos no modo $jogo do CineQuiz!\nBaixe agora o app na PlayStore e teste seus conhecimentos do mundo cinematográfico!"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(android.content.Intent.EXTRA_TEXT, mensagem)
        startActivity(Intent.createChooser(intent, titulo))
    }
}
