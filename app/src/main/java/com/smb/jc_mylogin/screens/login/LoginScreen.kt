package com.smb.jc_mylogin.screens.login

import android.annotation.SuppressLint
import android.util.Log

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

import com.smb.e09_login.navigation.Screens
import com.smb.e09_login.screens.login.LoginScreenViewModel
import com.smb.jc_mylogin.R
/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    // Usamos un navController ficticio para la vista previa
    MaterialTheme {
        // Usa un NavController simulado
        val navController = rememberNavController()

        // Llama a la pantalla principal con un NavController ficticio
        LoginScreen(navController = navController)
    }// Llamamos a la función Start con el navController
}*/
@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    //Text(text = "Login")
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    // Facebook
    /*val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcherFb = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)) {
        // nothing to do. handled in FacebookCallback

        scope.launch {
            val tokenFB = AccessToken.getCurrentAccessToken()
            val credentialFB = tokenFB?.let { FacebookAuthProvider.getCredential(it.token)}
            if(credentialFB != null){
                viewModel.signInWithFacebook(credentialFB){
                    navController.navigate(Screens.HomeScreen.name)
                }
            }
        }
    }*/

    // Google
    // este token se consigue en Firebase->Proveedores de Acceso->Google->Conf del SKD->Id de cliente web
    val token = "102189252440-i2b5v33u25544iiispcsq8mn9nm93fm1.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartActivityForResult() // esto abrirá un activity para hacer el login de Google
    ) {
        val task =
            GoogleSignIn.getSignedInAccountFromIntent(it.data) // esto lo facilita la librería añadida
        // el intent será enviado cuando se lance el launcher
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential) {
                navController.navigate(Screens.HomeScreen.name)
            }
        } catch (ex: Exception) {
            Log.d("My Login", "GoogleSignIn falló")
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(bottom = 30.dp)
        ,

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(25.dp)

        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = if (showLoginForm.value) "Log in to Spotify"
                else "Sign up to start listening"

                Text(text = text1,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            val google = if (showLoginForm.value) "Continue with Google"
            else "Sign Up with Google"
            val facebook = if (showLoginForm.value) "Continue with Facebook"
            else "Sign Up with Facebook"
            val apple = if (showLoginForm.value) "Continue with Apple"
            else "Sign Up with Apple"

                //GOOGLE
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            val opciones = GoogleSignInOptions
                                .Builder(
                                    GoogleSignInOptions.DEFAULT_SIGN_IN
                                )
                                .requestIdToken(token)
                                .requestEmail()
                                .build()

                            val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
                            launcher.launch(googleSingInCliente.signInIntent)
                        },

                    ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.buscar),
                            contentDescription = "Google",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Unspecified,//esta linea inhibe el tintado que aplica el outlinedbutton a sus hijos

                        )
                        Spacer(modifier = Modifier.width(25.dp))
                        Text(text = google, color = Color.White)
                    }
                }
                OutlinedButton(//facebook
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.facebook),
                            contentDescription = "Google",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Unspecified,

                            )
                        Spacer(modifier = Modifier.width(23.dp))
                        Text(text = facebook, color = Color.White)
                    }

                }
                //apple
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.manzana),
                            contentDescription = "Google",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Unspecified,
                            )
                        Spacer(modifier = Modifier.width(25.dp))
                        Text(text = apple, color = Color.White)
                    }
                }



            Divider(modifier = Modifier.padding(vertical = 20.dp), color = Color.DarkGray)

            if (showLoginForm.value) { //si es true crea cuenta sino inicia sesion

                UserForm(isCreateAccount = false) { email, password ->
                    Log.d("My Login", "Logueando con $email y $password")
                    viewModel.signInWithEmailAndPassword(
                        email,
                        password
                    ) {//pasamos email, password, y la funcion que navega hacia home
                        navController.navigate(Screens.HomeScreen.name)
                    }

                }
            } else {
                //Text(text = "Sign Up")
                UserForm(isCreateAccount = true) { email, password ->
                    Log.d("My Login", "Creando cuenta con $email y $password")
                    viewModel.createUserWithEmailAndPassword(
                        email,
                        password
                    ) {
                        navController.navigate(Screens.HomeScreen.name)
                    }

                }
            }


            Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color.DarkGray)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = if (showLoginForm.value) "Don´t have an account?"
                else "Already have an account?"
                val text2 = if (showLoginForm.value) "Sign Up"
                else "Log In"
                Text(text = text1)
                Text(text = text2,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.secondary)
            }

            // FACEBOOK
            /*  Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(10.dp)
                      .clip(RoundedCornerShape(10.dp))
                      .clickable {

                          launcherFb.launch(listOf("email","public_profile"))
                      },
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically
              ) {
                  Image(
                      painter = painterResource(id = R.drawable.ic_fb),
                      contentDescription = "Login con facebook",
                      modifier = Modifier
                          .padding(10.dp)
                          .size(40.dp)
                  )

                  Text(
                      text = "Login con Facebook",
                      fontSize = 18.sp,
                      fontWeight = FontWeight.Bold
                  )
              }*/


        }
    }
}

@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {

    val email = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }

    var checked by rememberSaveable { mutableStateOf(false) }

    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    //controla que al hacer clic en el boton submite, el teclado se oculte
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = Modifier.padding(top = 10.dp),

    ) {

        Text("Email or username", color = Color.White)
        EmailInput(
            emailState = email
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Password", color = Color.White)
        PasswordInput(
            passwordState = password,
            passwordVisible = passwordVisible
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {

            Switch(
                checked = checked,
                onCheckedChange = { isChecked -> checked = isChecked },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF000000),
                    checkedTrackColor = Color(0xFF00d167),
                    uncheckedThumbColor = Color.DarkGray,
                    uncheckedTrackColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text("Remember me", color = Color.White)
        }


        SubmitButton(
            textId = if (isCreateAccount) "Sign Up" else "Log In",
            inputValido = valido
        ) {
            onDone(email.value.trim(), password.value.trim())
            //se oculta el teclado, el ? es que se llama la función en modo seguro
            keyboardController?.hide()
        }
        Text(
            text = "Forgot your password?",
            color = Color.White,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}


@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        modifier = Modifier.fillMaxWidth(),
        colors =  ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00d167),
            contentColor = Color.Black

        )) {
        Text(
            text = textId,
            modifier = Modifier.padding(5.dp)
        )
    }
}


@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email or username"
) {
    InputField(
        valuestate = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    valuestate: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine: Boolean = true,
) {
    OutlinedTextField(
        value = valuestate.value,
        onValueChange = { valuestate.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier = Modifier.background(Color(0xFFccd1d1).copy(alpha = 0.1f)).fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)

    )
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String = "Password",
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        modifier = Modifier.background(Color(0xFFccd1d1).copy(alpha = 0.1f)).fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        
    )
}

