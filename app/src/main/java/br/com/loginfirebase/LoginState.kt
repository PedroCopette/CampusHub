package br.com.loginfirebase

data class LoginState(
    val email: String = "",
    val senha: String = "",
    val carregando: Boolean = false,
    val mensagemErro: String? = null
) {
    fun eValido(): Boolean {
        return CadastroValidator.loginCamposPreenchidos(email, senha)
    }

    fun validarComMensagem(): String? {
        val emailTratado = email.trim()
        val senhaTratada = senha.trim()
        if (emailTratado.isEmpty() || senhaTratada.isEmpty()) {
            return "Preencha o e-mail e a senha."
        }
        if (!CadastroValidator.emailValido(emailTratado)) {
            return "Digite um e-mail válido."
        }
        return null
    }
}
