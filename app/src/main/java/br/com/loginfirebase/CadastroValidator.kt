package br.com.loginfirebase

object CadastroValidator {

    fun camposPreenchidos(
        nome: String,
        email: String,
        senha: String
    ): Boolean {
        return nome.trim().isNotEmpty() &&
                email.trim().isNotEmpty() &&
                senha.trim().isNotEmpty()
    }

    fun senhaValida(senha: String): Boolean {
        return senha.trim().length >= 6
    }

    fun emailValido(email: String): Boolean {
        return email.trim().matches(
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        )
    }

    fun loginCamposPreenchidos(
        email: String,
        senha: String
    ): Boolean {
        return email.trim().isNotEmpty() &&
                senha.trim().isNotEmpty()
    }
}