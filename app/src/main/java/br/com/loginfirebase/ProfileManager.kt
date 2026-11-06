package br.com.loginfirebase

object ProfileManager {

    fun tratarDadosPerfil(nome: String?, email: String?): Pair<String, String> {
        val nomeTratado = ProfileUtils.formatarNomeExibicao(nome, email)
        val emailTratado = ProfileUtils.formatarEmailExibicao(email)
        return Pair(nomeTratado, emailTratado)
    }

    fun validarEdicaoPerfil(nome: String): Pair<Boolean, String> {
        val nomeFinal = nome.trim()
        if (nomeFinal.isEmpty()) {
            return Pair(false, "Digite um nome.")
        }
        return Pair(true, "OK")
    }
}
