package br.com.loginfirebase

data class EventoBusca(
    val id: String = "",
    val nome: String = "",
    val data: String = "",
    val horario: String = "",
    val local: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val endereco: String = ""
)

data class FavoritoEvento(
    val id: String = "",
    val nome: String = "",
    val data: String = "",
    val horario: String = "",
    val local: String = ""
)

data class Evento(
    val id: String = "",
    val nome: String = "",
    val data: String = "",
    val horario: String = "",
    val local: String = ""
)

data class Comentario(
    val id: String = "",
    val texto: String = "",
    val usuarioId: String = "",
    val autorNome: String = "",
    val dataPublicacao: String = ""
)

data class FotoPublicada(
    val id: String = "",
    val arquivo: String = "",
    val autor: String = "",
    val usuarioId: String = "",
    val dataPublicacao: String = ""
)

data class AudioPublicado(
    val id: String = "",
    val arquivo: String = "",
    val autor: String = "",
    val usuarioId: String = "",
    val dataPublicacao: String = ""
)

data class VideoPublicado(
    val id: String = "",
    val arquivo: String = "",
    val autor: String = "",
    val usuarioId: String = "",
    val dataPublicacao: String = ""
)
