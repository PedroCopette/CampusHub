package br.com.loginfirebase

object ProfileUtils {

    fun formatarNomeExibicao(nome: String?, email: String?): String {
        val nomeTratado = nome?.trim()
        if (!nomeTratado.isNullOrEmpty()) {
            return nomeTratado
        }
        val emailTratado = email?.trim()
        if (!emailTratado.isNullOrEmpty() && emailTratado.contains("@")) {
            return emailTratado.substringBefore("@")
        }
        return "Estudante CampusHub"
    }

    fun formatarEmailExibicao(email: String?): String {
        val emailTratado = email?.trim()
        return if (!emailTratado.isNullOrEmpty()) {
            emailTratado
        } else {
            "E-mail não informado"
        }
    }

    fun validarEdicaoNome(nome: String): Boolean {
        return nome.trim().isNotEmpty()
    }
}
