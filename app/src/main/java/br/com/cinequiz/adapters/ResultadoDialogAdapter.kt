package br.com.cinequiz.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import br.com.cinequiz.MenuActivity
import br.com.cinequiz.R
import kotlinx.android.synthetic.main.layout_resultado_jogo.view.*

class ResultadoDialogAdapter: DialogFragment() {

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

        btnJogarNovamente.setOnClickListener{
            dismiss()
        }

        btnVoltarMenu.setOnClickListener{
            var intent = Intent(activity, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }

        return rootView
    }
}
