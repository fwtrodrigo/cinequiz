package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.cinequiz.R
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider

//851294246873-0gel4t589odrfmqa2r5vl0si8n7i68f7.apps.googleusercontent.com

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 100
    private lateinit var facebookSignInBtn: LoginButton
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        //Facebook Auth

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        facebookSignInBtn = findViewById(R.id.facebookSignInButton)

        callbackManager = CallbackManager.Factory.create()

        facebookSignInBtn.setReadPermissions("email", "public_profile")
        facebookSignInBtn.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })

        //Google Auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInButton.setOnClickListener() {
            googleSignIn()
        }


        edtLoginEmail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel_selecionado)
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        edtLoginSenha.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }


        btnLoginCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        btnLoginEntrar.setOnClickListener {
            appLogin()
        }


    }

    fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "GOOGLEsignInWithCredential:success")
                    appLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "GOOGLEsignInWithCredential:failure", task.exception)
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "FACEBOOKsignInWithCredential:success")
                    appLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "FACEBOOKsignInWithCredential:failure", task.exception)
                }
            }
    }


    fun appLogin() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}
