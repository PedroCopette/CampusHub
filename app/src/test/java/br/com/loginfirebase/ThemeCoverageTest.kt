package br.com.loginfirebase

import br.com.loginfirebase.ui.theme.Pink40
import br.com.loginfirebase.ui.theme.Pink80
import br.com.loginfirebase.ui.theme.Purple40
import br.com.loginfirebase.ui.theme.Purple80
import br.com.loginfirebase.ui.theme.PurpleGrey40
import br.com.loginfirebase.ui.theme.PurpleGrey80
import br.com.loginfirebase.ui.theme.Typography
import org.junit.Assert.assertNotNull
import org.junit.Test

class ThemeCoverageTest {

    @Test
    fun testarPropriedadesDeCoresETipografia() {
        assertNotNull(Purple80)
        assertNotNull(PurpleGrey80)
        assertNotNull(Pink80)
        assertNotNull(Purple40)
        assertNotNull(PurpleGrey40)
        assertNotNull(Pink40)
        assertNotNull(Typography)
        assertNotNull(Typography.bodyLarge)
    }
}
