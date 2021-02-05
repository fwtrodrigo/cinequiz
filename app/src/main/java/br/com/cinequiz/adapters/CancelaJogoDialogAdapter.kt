package br.com.cinequiz.adapters

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import br.com.cinequiz.R
import br.com.cinequiz.ui.MenuActivity


class CancelaJogoDialogAdapter(private val mediaPlayer: MediaPlayer) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogStyle);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.layout_cancela_jogo, container, false)
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.color.cineSombraAzul)

        var btnContinuar = rootView.findViewById<Button>(R.id.btnContinuar)
        var btnVoltarMenu = rootView.findViewById<Button>(R.id.btnVoltarMenu)

        btnContinuar.setOnClickListener {
            dismiss()
        }

        btnVoltarMenu.setOnClickListener {
            mediaPlayer.stop()
            var intent = Intent(activity, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }

        return rootView
    }

}
