package br.com.loginfirebase

object AuthActivityHelper {

    fun validarLoginInput(email: String, senha: String): Pair<Boolean, String> {
        return AuthManager.validarLogin(email, senha)
    }

    fun validarCadastroInput(nome: String, email: String, senha: String): Pair<Boolean, String> {
        return AuthManager.validarCadastro(nome, email, senha)
    }

    fun validarRecuperacaoInput(email: String): Pair<Boolean, String> {
        return RecoveryManager.validarEmailRecuperacao(email)
    }

    fun mensagemSucessoLogin(): String = "Login realizado com sucesso!"
    fun mensagemSucessoCadastro(): String = "Conta criada com sucesso!"
    fun mensagemSucessoRecuperacao(): String = "E-mail de recuperação enviado!"
}
