package br.com.loginfirebase

object PreferencesManager {

    fun prepararDadosPreferencias(nome: String, email: String): Map<String, String> {
        return mapOf(
            "nome" to nome.trim(),
            "email" to email.trim()
        )
    }
}
