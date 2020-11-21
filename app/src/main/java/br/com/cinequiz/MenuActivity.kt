package br.com.cinequiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_cena.view.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_dicas.view.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnMenuDicas.btnItemDica.setOnClickListener {
            startActivity(Intent(this, JogoDica::class.java))
        }

        btnMenuCenas.btnItemCena.setOnClickListener {
            startActivity(Intent(this, JogoCena::class.java))
        }

        btnMenuMedalhas.setOnClickListener {
            startActivity(Intent(this, MedalhasActivity::class.java))
        }

    }
}
