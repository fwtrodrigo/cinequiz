package br.com.cinequiz.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import br.com.cinequiz.ui.MenuActivity
import br.com.cinequiz.R

class ResultadoDialogAdapter(private val pontos: Int, private val jogo: String): DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogStyle);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.layout_resultado_jogo, container, false)
        dialog?.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.cineSombraAzul)

        var btnJogarNovamente = rootView.findViewById<Button>(R.id.btnResultadoJogarNovamente)
        var btnVoltarMenu = rootView.findViewById<Button>(R.id.btnResultadoVoltarMenu)
        var ibCompartilhar = rootView.findViewById<ImageButton>(R.id.ibCompartilhar)
        var txtResultadoPontos = rootView.findViewById<TextView>(R.id.txtResultadoPontos)
        txtResultadoPontos.text = pontos.toString()

        btnJogarNovamente.setOnClickListener{
            dismiss()
        }

        btnVoltarMenu.setOnClickListener{
            var intent = Intent(activity, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }

        ibCompartilhar.setOnClickListener{
            //implementar compartilhamento.
            compartilhar(jogo)
            Toast.makeText(activity, "implementar compartilhamento", Toast.LENGTH_SHORT).show()
        }

        return rootView
    }

    private fun compartilhar(jogo: String) {
        val titulo = "Compartilhe o seu resultado com os seus amigos!"
        val mensagem = "Eu obtive $pontos pontos no modo $jogo do CineQuiz!\nBaixe agora o app na PlayStore e teste seus conhecimentos do mundo cinematográfico!"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(android.content.Intent.EXTRA_TEXT, mensagem)
        startActivity(Intent.createChooser(intent, titulo))
    }
}
