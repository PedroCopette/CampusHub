package br.com.loginfirebase

object UiStateCalculators {

    fun calcularCorFundoHex(destaque: Boolean, sair: Boolean): Long {
        return when {
            destaque -> 0xFFF0F5FF
            sair -> 0xFFFFF0F0
            else -> 0xFFFFFFFF
        }
    }

    fun calcularCorIconeHex(sair: Boolean): Long {
        return if (sair) 0xFFD42121 else 0xFF172D59
    }

    fun calcularCorTextoHex(sair: Boolean): Long {
        return if (sair) 0xFFD42121 else 0xFF142748
    }

    fun calcularCorDescricaoHex(sair: Boolean): Long {
        return if (sair) 0xFFBE5555 else 0xFF657694
    }

    fun calcularCorBordaHex(destaque: Boolean, sair: Boolean): Long {
        return when {
            destaque -> 0xFF91B6FF
            sair -> 0xFFFFD0D0
            else -> 0xFFE2EAF6
        }
    }

    fun resolverIconeCategoria(categoria: String): String {
        return if (categoria == "Acadêmico") "academico" else "tecnologia"
    }

    fun ehTipoIconeEventoValido(tipo: String): Boolean {
        return listOf("busca", "chevron", "calendario", "localizacao", "academico", "tecnologia").contains(tipo)
    }

    fun ehTipoIconeLinhaValido(tipo: String): Boolean {
        return listOf("calendario", "ingresso", "estrela", "perfil", "sair").contains(tipo)
    }

    fun ehTipoIconeFavoritoValido(tipo: String): Boolean {
        return listOf("star", "calendar", "location", "remove", "back").contains(tipo)
    }

    fun ehTipoIconeMeusEventosValido(tipo: String): Boolean {
        return listOf("calendario", "localizacao").contains(tipo)
    }
}
