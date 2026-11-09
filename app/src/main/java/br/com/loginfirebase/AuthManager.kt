package br.com.loginfirebase

object AuthManager {

    fun validarLogin(email: String, senha: String): Pair<Boolean, String> {
        val emailFinal = email.trim()
        val senhaFinal = senha.trim()

        if (!CadastroValidator.loginCamposPreenchidos(emailFinal, senhaFinal)) {
            return Pair(false, "Preencha o e-mail e a senha.")
        }
        if (!CadastroValidator.emailValido(emailFinal)) {
            return Pair(false, "Digite um e-mail válido.")
        }
        return Pair(true, "OK")
    }

    fun validarCadastro(nome: String, email: String, senha: String): Pair<Boolean, String> {
        val nomeFinal = nome.trim()
        val emailFinal = email.trim()
        val senhaFinal = senha.trim()

        if (!CadastroValidator.camposPreenchidos(nomeFinal, emailFinal, senhaFinal)) {
            return Pair(false, "Preencha todos os campos.")
        }
        if (!CadastroValidator.emailValido(emailFinal)) {
            return Pair(false, "Digite um e-mail válido.")
        }
        if (!CadastroValidator.senhaValida(senhaFinal)) {
            return Pair(false, "A senha deve ter pelo menos 6 caracteres.")
        }
        return Pair(true, "OK")
    }

    fun processarRespostaLogin(isSuccessful: Boolean): String {
        return if (isSuccessful) "Login realizado com sucesso!" else "E-mail ou senha incorretos."
    }

    fun processarRespostaCadastro(isSuccessful: Boolean, exceptionMessage: String?): String {
        return if (isSuccessful) "Conta criada com sucesso!" else "Erro ao criar conta: ${exceptionMessage ?: "Erro desconhecido"}"
    }

    fun processarRespostaRecuperacao(isSuccessful: Boolean): String {
        return if (isSuccessful) "E-mail de recuperação enviado!" else "Não foi possível enviar o e-mail de recuperação."
    }
}
