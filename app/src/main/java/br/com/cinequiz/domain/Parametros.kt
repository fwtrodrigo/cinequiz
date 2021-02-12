package br.com.cinequiz.domain

class Parametros {
    companion object Jogos {
        //ID's
        val ID_JOGO_CENA = 1
        val ID_JOGO_DICA = 0
        val ID_RESPOSTA_CORRETA = 1
        val ID_RESPOSTA_ERRADA = 0

        //Jogos
        val CHAVE_JOGO = "id_jogo"
        val CHAVE_QUANTIDADE_FILMES = "quantidade_filmes"
        val CHAVE_LISTA_FILMES = "lista_filmes"

        //JogoCena
        val TEMPO_JOGO_CENA: Long = 60000 //60 Segundos
        val CONTADOR_JOGO_CENA = 60
        val PONTUACAO_ACERTO_JOGO_CENA = 15
        val PONTUACAO_ERRO_JOGO_CENA = 5
        val QUANTIDADE_INICIAL_FILMES_CENA = 50

        //JogoDica
        val PONTUACAO_INICIAL_JOGO_DICA = 500
        val QUANTIDADE_INICIAL_FILMES_DICA = 10
        val PONTUACAO_ACERTO_JOGO_DICA = 50
        val PONTUACAO_ERRO_JOGO_DICA = 10
        val PONTUACAO_PROXIMA_DICA_JOGO_DICA = 10

        //id medalhas
        val CONTADOR_TOTAL_500 = "TOTAL_500"
        val CONTADOR_TOTAL_1500 = "TOTAL_1500"
        val CONTADOR_TOTAL_3000 = "TOTAL_3000"
        val CONTADOR_CENA_200 = "CENA_200"
        val CONTADOR_CENA_1000 = "CENA_1000"
        val CONTADOR_DICA_200 = "DICA_200"
        val CONTADOR_DICA_1000 = "DICA_1000"
        val CONTADOR_ANOS_70 = "ANOS_70"
        val CONTADOR_ANOS_80 = "ANOS_80"
        val CONTADOR_ANOS_90 = "ANOS_90"
        val CONTADOR_ANOS_00 = "ANOS_00"
        val CONTADOR_ANOS_10 = "ANOS_10"

    }
}
