package br.com.loginfirebase

data class RegistrationState(
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val carregando: Boolean = false
) {
    fun validar(): String? {
        val nomeTratado = nome.trim()
        val emailTratado = email.trim()
        val senhaTratada = senha.trim()

        if (!CadastroValidator.camposPreenchidos(nomeTratado, emailTratado, senhaTratada)) {
            return "Preencha todos os campos."
        }
        if (!CadastroValidator.emailValido(emailTratado)) {
            return "Digite um e-mail válido."
        }
        if (!CadastroValidator.senhaValida(senhaTratada)) {
            return "A senha deve ter pelo menos 6 caracteres."
        }
        return null
    }
}
