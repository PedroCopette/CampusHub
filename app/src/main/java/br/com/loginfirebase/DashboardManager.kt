package br.com.loginfirebase

object DashboardManager {

    fun formatarSaudacaoCompleta(nome: String?): String {
        return DashboardUtils.formatarSaudacao(nome)
    }

    fun obterMenuOpcoesList(): List<String> {
        return DashboardUtils.obterOpcoesMenu()
    }
}
