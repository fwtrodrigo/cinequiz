package br.com.cinequiz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.cinequiz.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_opcoes.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class OpcoesActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opcoes)

        mAuth = FirebaseAuth.getInstance()

        toolbarOpcoes.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }


        btnOpcoesSair.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}