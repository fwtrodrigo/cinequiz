package br.com.cinequiz.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.cinequiz.databinding.ActivityOpcoesBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_opcoes.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class OpcoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpcoesBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpcoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        var idUsuario = mAuth.currentUser?.uid
        prefs = getSharedPreferences("userPrefs_$idUsuario", MODE_PRIVATE)
        val editor = prefs.edit()
        binding.swithOpcoesNotificacoes.isChecked = prefs.getBoolean("notificacoes", true)
        binding.swithOpcoesMusica.isChecked = prefs.getBoolean("musica", true)
        binding.swithOpcoesSons.isChecked = prefs.getBoolean("sons", true)


        binding.toolbarOpcoes.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.swithOpcoesNotificacoes.setOnCheckedChangeListener{compoundButton, onSwitch->
            if (onSwitch){
                editor.putBoolean("notificacoes", true).apply()
                Toast.makeText(this, "Notificações ativadas", Toast.LENGTH_SHORT).show()

            }else{
                editor.putBoolean("notificacoes", false).apply()
                Toast.makeText(this, "Notificações desativadas.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.swithOpcoesMusica.setOnCheckedChangeListener{compoundButton, onSwitch->
            if (onSwitch){
                editor.putBoolean("musica", true).apply()
                Toast.makeText(this, "Músicas ativadas", Toast.LENGTH_SHORT).show()

            }else{
                editor.putBoolean("musica", false).apply()
                Toast.makeText(this, "Músicas desativadas.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.swithOpcoesSons.setOnCheckedChangeListener{compoundButton, onSwitch->
            if (onSwitch){
                editor.putBoolean("sons", true).apply()
                Toast.makeText(this, "Sons ativados", Toast.LENGTH_SHORT).show()

            }else{
                editor.putBoolean("sons", false).apply()
                Toast.makeText(this, "Sons desativados.", Toast.LENGTH_SHORT).show()
            }
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
            //Toast.makeText(baseContext, "o token é $token", Toast.LENGTH_SHORT).show()
        })
    }
}