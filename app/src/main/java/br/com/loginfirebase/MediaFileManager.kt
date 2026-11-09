package br.com.loginfirebase

import java.io.File

object MediaFileManager {

    fun obterPastaMidia(baseDir: File, tipo: String): File {
        val pasta = File(baseDir, "${tipo}_eventos")
        if (!pasta.exists()) {
            pasta.mkdirs()
        }
        return pasta
    }

    fun obterArquivoJsonPublicacoes(baseDir: File, tipo: String, idEvento: String): File {
        val idSeguro = idEvento.replace(Regex("[^A-Za-z0-9_-]"), "_")
        val prefixo = when (tipo) {
            "fotos" -> "publicacoes"
            "audios" -> "publicacoes_audios"
            "videos" -> "publicacoes_videos"
            else -> "publicacoes_$tipo"
        }
        return File(baseDir, "${prefixo}_$idSeguro.json")
    }

    fun validarInclusaoMidia(usuarioLogado: Boolean): Pair<Boolean, String> {
        if (!usuarioLogado) {
            return Pair(false, "Faça login para publicar mídia.")
        }
        return Pair(true, "OK")
    }

    fun validarExclusaoMidia(usuarioIdLogado: String?, usuarioIdAutor: String): Pair<Boolean, String> {
        if (usuarioIdLogado == null) {
            return Pair(false, "Faça login para excluir mídia.")
        }
        if (usuarioIdLogado != usuarioIdAutor) {
            return Pair(false, "Você só pode excluir seus próprios arquivos.")
        }
        return Pair(true, "OK")
    }
}
