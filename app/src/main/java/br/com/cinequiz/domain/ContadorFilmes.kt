package br.com.cinequiz.domain

import android.util.Log

class ContadorFilmes {
    var totalAcertos = 0
    var totalAnos70 = 0
    var totalAnos80 = 0
    var totalAnos90 = 0
    var totalAnos00 = 0
    var totalAnos10 = 0

    fun atualizaPontuacao(f: Filme) {
        totalAcertos++

        when (arredondaAno(f.release_date)) {
            "1970" -> totalAnos70++

            "1980" -> totalAnos80++

            "1990" -> totalAnos90++

            "2000" -> totalAnos00++

            "2010" -> totalAnos10++
        }

        Log.d("CONTADORFILMES", f.toString() )
        Log.d("CONTADORFILMESPONTOS", this.toString() )
    }

    private fun arredondaAno(data: String): String {
        val ano = data.substring(0, 4)
        val seculo = ano.substring(0, 2)
        val decada = ano.substring(2, 3) + "0"
        Log.d("FILME  ANO ARREDONDADO", seculo + decada)
        return seculo + decada
    }

    override fun toString(): String {
        return "PontuacaoFilmes(totalAcertos=$totalAcertos, anos70=$totalAnos70, anos80=$totalAnos80, anos90=$totalAnos90, anos00=$totalAnos00, anos10=$totalAnos10)"
    }
}