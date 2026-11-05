package br.com.loginfirebase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EventosUtils {

    // Verifica se o evento já foi encerrado
    fun eventoEncerrado(
        data: String,
        horario: String,
        agora: Date = Date()
    ): Boolean {

        val formato = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        )

        val dataEvento = try {
            formato.parse("$data $horario")
        } catch (e: Exception) {
            null
        } ?: return false

        return agora.after(dataEvento)
    }

    // Converte a data do evento para permitir ordenação
    fun ordenarData(data: String): Long {

        return try {

            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).parse(data)?.time ?: Long.MAX_VALUE

        } catch (e: Exception) {

            Long.MAX_VALUE
        }
    }

    // Filtra os eventos por nome, categoria e situação
    fun filtrarEventos(
        eventos: List<EventoBusca>,
        busca: String,
        categoriaSelecionada: String,
        situacaoSelecionada: String,
        agora: Date = Date()
    ): List<EventoBusca> {

        return eventos.filter { evento ->

            val correspondeBusca = evento.nome.contains(
                busca,
                ignoreCase = true
            )

            val correspondeCategoria =
                categoriaSelecionada == "Todas" ||
                        evento.categoria == categoriaSelecionada

            val correspondeSituacao =
                when (situacaoSelecionada) {

                    "Próximos" -> !eventoEncerrado(
                        evento.data,
                        evento.horario,
                        agora
                    )

                    "Encerrados" -> eventoEncerrado(
                        evento.data,
                        evento.horario,
                        agora
                    )

                    else -> true
                }

            correspondeBusca &&
                    correspondeCategoria &&
                    correspondeSituacao
        }
    }
    
}
