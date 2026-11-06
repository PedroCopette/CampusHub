package br.com.loginfirebase

object RecoveryManager {

    fun validarEmailRecuperacao(email: String): Pair<Boolean, String> {
        val emailFinal = email.trim()

        if (emailFinal.isEmpty()) {
            return Pair(false, "Digite seu e-mail.")
        }
        if (!CadastroValidator.emailValido(emailFinal)) {
            return Pair(false, "Digite um e-mail válido.")
        }
        return Pair(true, "OK")
    }
}
