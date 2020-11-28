package br.com.cinequiz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import br.com.cinequiz.R
import br.com.cinequiz.adapters.ResultadoDialogAdapter
import kotlinx.android.synthetic.main.activity_jogo_dica.*
import kotlinx.android.synthetic.main.item_botoes_alternativas.view.*

class JogoDica : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogo_dica)

        val listaDicas = arrayOf(
            "1 - John C. Reilly atuou como um dos personagens.",
            "2 - Fui produzido pelo estúdio Disney.",
            "3 - Minha estreia foi em 2012.",
            "4 - Meu diretor também dirigiu Zootopia.",
            "5 - Um dos meus personagens se chama Ralph."
        )
        val listView = findViewById<ListView>(R.id.listviewCenaDica)
        val adapter = ArrayAdapter(this, R.layout.item_lista_dica, listaDicas)
        listView.adapter = adapter

        var resultadoDialog = ResultadoDialogAdapter()

        includeJogoDicaBotoes.imageButtonAlternativas1.setOnClickListener {
            resultadoDialog.show(supportFragmentManager, "resultadoDialog")
        }
    }
}