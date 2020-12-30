package br.com.cinequiz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import br.com.cinequiz.R
import br.com.cinequiz.adapters.MedalhasAdapter
import br.com.cinequiz.databinding.ActivityMedalhasBinding
import kotlinx.android.synthetic.main.activity_medalhas.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class MedalhasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedalhasBinding

    private val viewModel: MedalhaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedalhasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMedalhas.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        val listaMedalhas = viewModel.getListMedalhasGson()
        val adapter = MedalhasAdapter(listaMedalhas)

        binding.rvMedalhas.adapter = adapter

    }
}
