package br.com.cinequiz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import br.com.cinequiz.R
import br.com.cinequiz.adapters.ResultadoDialogAdapter
import br.com.cinequiz.domain.Filme
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_jogo_dica.*
import kotlinx.android.synthetic.main.item_botoes_alternativas.*
import kotlinx.android.synthetic.main.item_botoes_alternativas.view.*
import kotlinx.android.synthetic.main.item_card_cena.*

class JogoDica : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogo_dica)

        val listaFilmes = intent.getSerializableExtra("listaFilmes") as List<Filme>
        var contadorFilme = 0
        val listView = findViewById<ListView>(R.id.listviewCenaDica)

        iniciarFilme(listaFilmes, contadorFilme, listView)

        var resultadoDialog = ResultadoDialogAdapter()

        includeJogoDicaBotoes.imageButtonAlternativas1.setOnClickListener {
            contadorFilme++
            if(contadorFilme == listaFilmes.size){
                resultadoDialog.show(supportFragmentManager, "resultadoDialog")
            }else{
                iniciarFilme(listaFilmes, contadorFilme, listView)
            }
        }
    }

    fun iniciarFilme(listaFilmes: List<Filme>, contadorFilme: Int, listView: ListView){

        val listaDicas = arrayOf(
            "1 - ${listaFilmes[contadorFilme].pessoasFilme[0].name} atuou como um dos meus personagens.",
            "2 - Fui produzido pelo estúdio ${listaFilmes[contadorFilme].production_companies[0].name}.",
            "3 - Minha estreia foi em ${listaFilmes[contadorFilme].formataDataLancamento()}.",
            "4 - Um dos meus personagens se chama ${listaFilmes[contadorFilme].pessoasFilme[0].character}."
        )

        val adapter = ArrayAdapter(this, R.layout.item_lista_dica, listaDicas)
        listView.adapter = adapter

        txtAlternativa1.text = listaFilmes[contadorFilme].title
        txtAlternativa2.text = listaFilmes[contadorFilme].filmesSimilares[0].title
        txtAlternativa3.text = listaFilmes[contadorFilme].filmesSimilares[1].title
        txtAlternativa4.text = listaFilmes[contadorFilme].filmesSimilares[2].title
    }
}
