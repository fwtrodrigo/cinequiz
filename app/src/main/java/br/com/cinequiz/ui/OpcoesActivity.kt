package br.com.cinequiz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityOpcoesBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
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

        binding.swithOpcoesNotificacoes.setOnClickListener {
            var notificacao = binding.swithOpcoesNotificacoes.isChecked

            Log.i("OpcoesActivity", "ver o valor de notificacoes que é: $notificacao")

            if (notificacao) {
                Toast.makeText(this, "Notificação ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notificação OFF", Toast.LENGTH_SHORT).show()
            }
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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MenuActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.i("MenuActivity", "token $token")
            Toast.makeText(baseContext, "o token é $token", Toast.LENGTH_SHORT).show()
        })
    }
}