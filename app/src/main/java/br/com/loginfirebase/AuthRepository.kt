package br.com.loginfirebase

object AuthRepository {

    fun tratarResultadoLogin(isSuccessful: Boolean): String {
        return if (isSuccessful) {
            "Login realizado com sucesso!"
        } else {
            "E-mail ou senha incorretos."
        }
    }

    fun tratarResultadoCadastro(isSuccessful: Boolean, exceptionMessage: String?): String {
        return if (isSuccessful) {
            "Conta criada com sucesso!"
        } else {
            "Erro ao criar conta: ${exceptionMessage ?: "Erro desconhecido"}"
        }
    }

    fun tratarResultadoRecuperacao(isSuccessful: Boolean): String {
        return if (isSuccessful) {
            "E-mail de recuperação enviado!"
        } else {
            "Não foi possível enviar o e-mail de recuperação."
        }
    }
}
