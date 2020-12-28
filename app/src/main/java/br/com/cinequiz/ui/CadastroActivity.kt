package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityCadastroBinding
import br.com.cinequiz.utils.ehEmailValido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlaFocoEditText()

        binding.toolbarCadastro.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnCadastroCadastrar.setOnClickListener {
            val nome = binding.edtCadastroNome.text.toString()
            val email = binding.edtCadastroEmail.text.toString()
            val senha = binding.edtCadastroSenha.text.toString()
            val confirmacao = binding.edtCadastroConfirmacaoSenha.text.toString()

            if (validaFormulario(nome, email, senha, confirmacao)) {
                criaUsuario(email, senha)
            }
        }

    }

    private fun criaUsuario(email: String, senha: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                //resposta recebida

            }.addOnSuccessListener {
                //conta criada
                showToast(getString(R.string.conta_sucesso))
                //o login do usuario eh automatico ao criar a conta, portanto joguei para o menu principal
                appLogin()

            }.addOnFailureListener {
                //falha
                showToast(it.toString())
                if (it is FirebaseAuthUserCollisionException) {
                    showToast(getString(R.string.email_ja_utilizado))
                }

                if (it is FirebaseAuthInvalidCredentialsException) {
                    showToast(getString(R.string.formato_email_invalido))
                }

                if (it is FirebaseAuthWeakPasswordException) {
                    showToast(getString(R.string.tamanho_senha_invalido, 6))
                }
            }
    }

    private fun validaFormulario(
        nome: String,
        email: String,
        senha: String,
        confirmacao: String
    ): Boolean {

        fun resetError() {
            binding.edtCadastroNome.error = null
            binding.edtCadastroEmail.error = null
            binding.edtCadastroSenha.error = null
            binding.edtCadastroConfirmacaoSenha.error = null
        }

        return when {
            nome.isEmpty() -> {
                resetError()
                binding.edtCadastroNome.requestFocus()
                binding.edtCadastroNome.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.preencher, "nome"))
                false
            }

            email.isEmpty() -> {
                resetError()
                binding.edtCadastroEmail.requestFocus()
                binding.edtCadastroEmail.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.preencher, "email"))
                false
            }

            !ehEmailValido(email) -> {
                binding.edtCadastroEmail.requestFocus()
                binding.edtCadastroEmail.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.informe_email_valido))
                false
            }

            senha.isEmpty() -> {
                resetError()
                binding.edtCadastroSenha.requestFocus()
                binding.edtCadastroSenha.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.preencher, "senha"))
                false
            }

            senha.length < 6 -> {
                resetError()
                binding.edtCadastroSenha.requestFocus()
                binding.edtCadastroSenha.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.tamanho_senha_invalido, 6))
                false
            }

            confirmacao.isEmpty() -> {
                resetError()
                binding.edtCadastroConfirmacaoSenha.requestFocus()
                binding.edtCadastroConfirmacaoSenha.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.preencher, "confirmação de senha"))
                false
            }

            senha != confirmacao -> {
                resetError()
                binding.edtCadastroConfirmacaoSenha.requestFocus()
                binding.edtCadastroConfirmacaoSenha.error = getString(R.string.confirmaca_invalida)
                false
            }
            else -> {
                resetError()
                true
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun appLogin() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun controlaFocoEditText() {
        binding.edtCadastroNome.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        binding.edtCadastroEmail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        binding.edtCadastroSenha.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        binding.edtCadastroConfirmacaoSenha.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }
    }
}

