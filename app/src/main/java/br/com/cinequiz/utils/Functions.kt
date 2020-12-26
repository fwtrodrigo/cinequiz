package br.com.cinequiz.utils

fun ehEmailValido(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}