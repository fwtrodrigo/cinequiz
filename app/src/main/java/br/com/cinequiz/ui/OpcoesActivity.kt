package br.com.cinequiz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityOpcoesBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_opcoes.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class OpcoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpcoesBinding

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpcoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.toolbarOpcoes.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.swithOpcoesNotificacoes.setOnClickListener{
            Toast.makeText(this, "Notificacoes", Toast.LENGTH_SHORT).show()
        }

        binding.swithOpcoesMusica.setOnClickListener{
            Toast.makeText(this, "Musica", Toast.LENGTH_SHORT).show()
        }

        binding.swithOpcoesSons.setOnClickListener{
            Toast.makeText(this, "Sons", Toast.LENGTH_SHORT).show()
        }

        binding.btnOpcoesSair.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}