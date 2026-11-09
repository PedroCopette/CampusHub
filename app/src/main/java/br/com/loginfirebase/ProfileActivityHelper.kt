package br.com.loginfirebase

object ProfileActivityHelper {

    fun obterPerfilTratado(nome: String?, email: String?): Pair<String, String> {
        return ProfileManager.tratarDadosPerfil(nome, email)
    }

    fun validarEdicaoNome(nome: String): Pair<Boolean, String> {
        return ProfileManager.validarEdicaoPerfil(nome)
    }
}
