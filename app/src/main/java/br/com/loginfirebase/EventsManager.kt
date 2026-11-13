package br.com.loginfirebase

object EventsManager {

    fun obterEnderecoPadrao(id: String): String {
        return when (id) {
            "semana_academica" ->
                "Av. Batista Bonotto Sobrinho, 733, Santiago - RS, Brasil"
            "hackathon_campushub" ->
                "Rua Tio Virgílio, 423, Santiago - RS, Brasil"
            "palestra_tecnologia" ->
                "Rua Bento Gonçalves, 1642, Santiago - RS, Brasil"
            else ->
                "Santiago, Rio Grande do Sul, Brasil"
        }
    }

    fun MapearDocumentoParaEvento(id: String, dados: Map<String, Any?>): EventoBusca? {
        val nome = dados["nomeEvento"] as? String ?: return null
        val data = dados["dataEvento"] as? String ?: return null
        val horario = dados["horarioEvento"] as? String ?: return null
        val local = dados["localEvento"] as? String ?: dados["enderecoEvento"] as? String ?: return null
        val descricao = dados["descricaoEvento"] as? String ?: ""
        val categoria = dados["categoria"] as? String ?: "Acadêmico"
        val endereco = (dados["enderecoEvento"] as? String) ?: obterEnderecoPadrao(id)

        return EventoBusca(
            id = id,
            nome = nome,
            data = data,
            horario = horario,
            local = local,
            descricao = descricao,
            categoria = categoria,
            endereco = endereco
        )
    }

    fun MapearListaDocumentos(documentos: List<Pair<String, Map<String, Any?>>>): List<EventoBusca> {
        return documentos.mapNotNull { (id, dados) ->
            MapearDocumentoParaEvento(id, dados)
        }.sortedBy { evento ->
            EventosUtils.ordenarData(evento.data)
        }
    }

    fun obterEventosIniciais(): List<EventoBusca> {
        return listOf(
            EventoBusca(
                id = "semana_academica",
                nome = "Semana Acadêmica",
                data = "20/09/2026",
                horario = "19:00",
                local = "Auditório da URI",
                descricao = "Evento acadêmico com palestras, apresentações e atividades para os estudantes.",
                categoria = "Acadêmico",
                endereco = obterEnderecoPadrao("semana_academica")
            ),
            EventoBusca(
                id = "hackathon_campushub",
                nome = "Hackathon CampusHub",
                data = "25/09/2026",
                horario = "08:00",
                local = "Laboratório de Informática",
                descricao = "Competição de programação e desenvolvimento de soluções tecnológicas.",
                categoria = "Tecnologia",
                endereco = obterEnderecoPadrao("hackathon_campushub")
            ),
            EventoBusca(
                id = "palestra_tecnologia",
                nome = "Palestra de Tecnologia",
                data = "30/09/2026",
                horario = "20:00",
                local = "Sala 12",
                descricao = "Palestra sobre tecnologia, inovação e tendências da área de computação.",
                categoria = "Tecnologia",
                endereco = obterEnderecoPadrao("palestra_tecnologia")
            )
        )
    }
}
