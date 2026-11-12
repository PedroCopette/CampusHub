package br.com.loginfirebase

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val NavyColor = Color(0xFF172554)
private val BlueColor = Color(0xFF3568D4)
private val BgColor = Color(0xFFF3F6FC)
private val TextColor = Color(0xFF24324A)
private val MutedColor = Color(0xFF718096)
private val BorderColor = Color(0xFFE2EAF6)

@Composable
fun TelaLoginUI(
    entrar: (String, String) -> Unit,
    abrirCadastro: () -> Unit,
    recuperarSenha: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(245.dp)
                .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
                .background(Brush.verticalGradient(colors = listOf(NavyColor, BlueColor)))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White.copy(alpha = 0.17f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CH",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    Column {
                        Text(
                            text = "CampusHub",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "SUA UNIVERSIDADE CONECTADA",
                            color = Color.White.copy(alpha = 0.82f),
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Seu campus,\nmais conectado.",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Eventos, atividades e oportunidades num só lugar.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-24).dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
        ) {

            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Bem-vindo de volta!",
                    color = TextColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Entre com sua conta para continuar.",
                    color = MutedColor,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    placeholder = { Text("seu@email.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueColor,
                        unfocusedBorderColor = Color(0xFFD6DEEA),
                        focusedLabelColor = BlueColor,
                        cursorColor = BlueColor
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    placeholder = { Text("Digite sua senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueColor,
                        unfocusedBorderColor = Color(0xFFD6DEEA),
                        focusedLabelColor = BlueColor,
                        cursorColor = BlueColor
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = { entrar(email, senha) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "ENTRAR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = abrirCadastro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEAF0FF),
                        contentColor = NavyColor
                    )
                ) {
                    Text(
                        text = "CRIAR CONTA",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                TextButton(
                    onClick = recuperarSenha,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Esqueci minha senha",
                        color = BlueColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Text(
            text = "CAMPUSHUB • URI SANTIAGO",
            color = MutedColor,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                bottom = 18.dp
            )
        )
    }
}

@Composable
fun TelaCadastroUI(
    registrar: (String, String, String, String) -> Unit,
    voltar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var perfil by remember { mutableStateOf("aluno") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Brush.verticalGradient(colors = listOf(NavyColor, BlueColor)))
                .padding(horizontal = 24.dp, vertical = 25.dp)
        ) {

            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White.copy(alpha = 0.17f)),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "CH",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = "CampusHub",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "ÁREA DO ALUNO",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(23.dp))

                Text(
                    text = "Crie sua conta",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Faça parte da comunidade CampusHub.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Vamos começar!",
                color = TextColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Preencha seus dados para criar sua conta.",
                color = MutedColor,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(
                        text = "NOME COMPLETO",
                        color = MutedColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        placeholder = { Text("Digite seu nome completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueColor,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = BlueColor,
                            cursorColor = BlueColor
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "E-MAIL",
                        color = MutedColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        placeholder = { Text("seu@email.com") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueColor,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = BlueColor,
                            cursorColor = BlueColor
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "SENHA",
                        color = MutedColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = { Text("Senha") },
                        placeholder = { Text("Mínimo de 6 caracteres") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueColor,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = BlueColor,
                            cursorColor = BlueColor
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("TIPO DE PERFIL", color = MutedColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf("aluno" to "Aluno", "organizador" to "Organizador").forEach { (value, label) ->
                            OutlinedButton(onClick = { perfil = value }, colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = if (perfil == value) BlueColor else MutedColor)) { Text(if (perfil == value) "✓ $label" else label) }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { registrar(nome, email, senha, perfil) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueColor,
                            contentColor = Color.White
                        )
                    ) {

                        Text(
                            text = "CRIAR CONTA",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = voltar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgColor,
                            contentColor = NavyColor
                        )
                    ) {

                        Text(
                            text = "VOLTAR",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "CAMPUSHUB • URI SANTIAGO",
                color = MutedColor,
                fontSize = 10.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TelaPerfilCampusUI(
    nome: String,
    email: String,
    editar: () -> Unit,
    voltar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Brush.verticalGradient(colors = listOf(NavyColor, BlueColor)))
                .padding(24.dp)
        ) {
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White.copy(alpha = 0.17f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CH",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "CampusHub",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "ÁREA DO ALUNO",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Meu Perfil",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Consulte seus dados pessoais.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFFEAF0FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👤",
                            fontSize = 40.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = nome,
                        color = TextColor,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = "Estudante CampusHub",
                        color = MutedColor,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    CampoPerfilUI(
                        titulo = "NOME COMPLETO",
                        valor = nome
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    CampoPerfilUI(
                        titulo = "E-MAIL DA CONTA",
                        valor = email
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = editar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "EDITAR PERFIL",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = voltar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = NavyColor
                )
            ) {
                Text(
                    text = "VOLTAR",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "CAMPUSHUB • URI SANTIAGO",
                color = MutedColor,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CampoPerfilUI(
    titulo: String,
    valor: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = titulo,
            color = MutedColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .background(BgColor)
                .padding(14.dp)
        ) {
            Text(
                text = valor,
                color = TextColor,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TelaEditarPerfilUI(
    nomeInicial: String,
    email: String,
    salvar: (String) -> Unit,
    voltar: () -> Unit
) {
    var nome by remember { mutableStateOf(nomeInicial) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Brush.verticalGradient(colors = listOf(NavyColor, BlueColor)))
                .padding(horizontal = 24.dp, vertical = 25.dp)
        ) {

            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White.copy(alpha = 0.17f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CH",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = "CampusHub",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "ÁREA DO ALUNO",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(23.dp))

                Text(
                    text = "Editar Perfil",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Mantenha suas informações atualizadas.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Seus dados",
                color = TextColor,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Altere seu nome e confira o e-mail cadastrado.",
                color = MutedColor,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(21.dp)
                ) {

                    Text(
                        text = "NOME COMPLETO",
                        color = MutedColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        placeholder = { Text("Digite seu nome") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueColor,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = BlueColor,
                            cursorColor = BlueColor
                        )
                    )

                    Spacer(modifier = Modifier.height(23.dp))

                    Text(
                        text = "E-MAIL DA CONTA",
                        color = MutedColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {},
                        enabled = false,
                        label = { Text("E-mail") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MutedColor,
                            disabledLabelColor = MutedColor,
                            disabledBorderColor = Color(0xFFE0E6F0),
                            disabledContainerColor = BgColor
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "O e-mail é exibido apenas para consulta nesta tela.",
                        color = MutedColor,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = { salvar(nome) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "SALVAR ALTERAÇÕES",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = voltar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = NavyColor
                )
            ) {
                Text(
                    text = "VOLTAR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "CAMPUSHUB • URI SANTIAGO",
                color = MutedColor,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun OpcaoDashboard(
    icone: String,
    titulo: String,
    descricao: String,
    destaque: Boolean = false,
    sair: Boolean = false,
    aoClicar: () -> Unit
) {
    Card(
        onClick = aoClicar,
        modifier = Modifier
            .fillMaxWidth()
            .height(94.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (destaque) Color(0xFFF0F5FF) else if (sair) Color(0xFFFFF0F0) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = descricao, fontSize = 12.sp, color = MutedColor)
            }
        }
    }
}

@Composable
fun CartaoFiltro(
    texto: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = texto, modifier = Modifier.weight(1f), fontSize = 12.sp)
        }
    }
}

@Composable
fun CartaoEvento(
    evento: EventoBusca,
    encerrado: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = evento.nome, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(text = "${evento.data} · ${evento.horario}", fontSize = 14.sp)
            Text(text = evento.local, fontSize = 14.sp, color = MutedColor)
        }
    }
}

@Composable
fun IconeEventos(
    tipo: String,
    cor: Color,
    tamanho: Dp
) {
    Canvas(modifier = Modifier.size(tamanho)) {
        drawCircle(color = cor)
    }
}


