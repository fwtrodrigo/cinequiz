package br.com.cinequiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import br.com.cinequiz.adapters.MedalhasAdapter
import br.com.cinequiz.domain.Medalha
import br.com.cinequiz.models.MedalhaViewModel
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_medalhas.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class MedalhasActivity : AppCompatActivity() {

    private val viewModel: MedalhaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medalhas)

        toolbarMedalhas.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        val listaMedalhas = viewModel.getListMedalhasGson()
        val adapter = MedalhasAdapter(listaMedalhas)

        rvMedalhas.adapter = adapter

    }
}
