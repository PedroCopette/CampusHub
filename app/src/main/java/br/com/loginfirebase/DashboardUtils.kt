package br.com.loginfirebase

object DashboardUtils {

    fun formatarSaudacao(nomeUsuario: String?): String {
        val nomeTratado = nomeUsuario?.trim()
        return if (!nomeTratado.isNullOrEmpty()) {
            "Bem-vindo(a), $nomeTratado!"
        } else {
            "Bem-vindo ao CampusHub!"
        }
    }

    fun obterOpcoesMenu(): List<String> {
        return listOf(
            "Eventos",
            "Meus Eventos",
            "Meus Favoritos",
            "Meu Perfil",
            "Sair"
        )
    }

    fun ehOpcaoValida(opcao: String): Boolean {
        return obterOpcoesMenu().contains(opcao)
    }
}
