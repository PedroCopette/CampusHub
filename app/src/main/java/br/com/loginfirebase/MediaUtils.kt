package br.com.loginfirebase

object MediaUtils {

    fun sanitizarIdEvento(idEvento: String): String {
        return idEvento.replace(Regex("[^A-Za-z0-9_-]"), "_")
    }

    fun gerarNomeArquivoPublicacao(idEvento: String, prefixo: String, extensao: String): String {
        val idSanitizado = sanitizarIdEvento(idEvento)
        return "publicacoes_${prefixo}_${idSanitizado}.${extensao}"
    }

    fun ehArquivoMediaValido(nomeArquivo: String): Boolean {
        val extensao = nomeArquivo.substringAfterLast('.', "").lowercase()
        return listOf("jpg", "jpeg", "png", "img", "m4a", "mp3", "mp4").contains(extensao)
    }
}
