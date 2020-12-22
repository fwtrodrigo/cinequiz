package br.com.cinequiz.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.cinequiz.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*

//851294246873-0gel4t589odrfmqa2r5vl0si8n7i68f7.apps.googleusercontent.com

class LoginActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInButton.setOnClickListener(){
            googleSignIn()
        }


        edtLoginEmail.setOnFocusChangeListener { view, hasFocus ->
           if(hasFocus) {
               view.background =  ContextCompat.getDrawable(this, R.drawable.shape_anel_selecionado)
           } else {
               view.background =  ContextCompat.getDrawable(this, R.drawable.shape_anel)
           }
        }

        edtLoginSenha.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus) {
                view.background =  ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background =  ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }


        btnLoginCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        btnLoginEntrar.setOnClickListener {
            appLogin()
        }


    }

    fun googleSignIn (){
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try{
            val account = completedTask.getResult((ApiException::class.java))
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if(acct != null){
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl

                Toast.makeText(this, "user: $personName", Toast.LENGTH_SHORT).show()
                personName?.let{appLogin()}
            }
        }catch (e: ApiException){
            Log.d("SignInResult: ", e.toString())
        }
    }

    fun appLogin(){
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }


}
