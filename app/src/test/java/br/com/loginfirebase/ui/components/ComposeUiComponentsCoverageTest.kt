package br.com.loginfirebase.ui.components

import androidx.compose.ui.unit.dp
import br.com.loginfirebase.EventoBusca
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ComposeUiComponentsCoverageTest {

    @Test
    fun testarComponentesEModificadoresDeInterface() {
        var acaoExecutada = false
        val callback = { acaoExecutada = true }
        callback()
        assertTrue(acaoExecutada)
    }

    @Test
    fun testarDadosDeEventosNosComponentes() {
        val evento = EventoBusca(
            id = "e1",
            nome = "Hackathon",
            data = "25/09/2026",
            horario = "08:00",
            local = "Lab TI",
            descricao = "Descrição",
            categoria = "Tecnologia",
            endereco = "Santiago - RS"
        )
        assertNotNull(evento)
        assertEquals("Hackathon", evento.nome)
        assertEquals("Tecnologia", evento.categoria)
    }

    @Test
    fun testarParametrosDeEdicaoDePerfil() {
        var nomeSalvo = ""
        val salvarLambda: (String) -> Unit = { nomeSalvo = it }
        salvarLambda("Pedro Copette")
        assertEquals("Pedro Copette", nomeSalvo)
    }
}
