package br.com.loginfirebase

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

data class PublicacaoLocal(val usuarioId: String, val nomeAutor: String, val dataMillis: Long)

object LocalMediaMetadata {
    fun salvar(arquivo: File, usuarioId: String, nomeAutor: String, dataMillis: Long = System.currentTimeMillis()) {
        val props = Properties().apply {
            setProperty("usuarioId", usuarioId)
            setProperty("nomeAutor", nomeAutor)
            setProperty("dataMillis", dataMillis.toString())
        }
        FileOutputStream(File(arquivo.parentFile, "${arquivo.name}.meta")).use { props.store(it, null) }
    }

    fun ler(arquivo: File): PublicacaoLocal {
        val props = Properties()
        val meta = File(arquivo.parentFile, "${arquivo.name}.meta")
        if (meta.exists()) runCatching { FileInputStream(meta).use(props::load) }
        val prefixo = arquivo.name.substringBefore('_', "")
        val uid = props.getProperty("usuarioId") ?: prefixo.takeIf { it.length >= 20 }.orEmpty()
        return PublicacaoLocal(uid, props.getProperty("nomeAutor") ?: uid.ifBlank { "Autor desconhecido" }, props.getProperty("dataMillis")?.toLongOrNull() ?: arquivo.lastModified())
    }

    fun excluir(arquivo: File): Boolean {
        val meta = File(arquivo.parentFile, "${arquivo.name}.meta")
        val removido = arquivo.delete()
        meta.delete()
        return removido
    }
}
