package com.mendoxy.mnotes.ui.presentation.login

import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mendoxy.mnotes.R
import com.mendoxy.mnotes.ui.presentation.components.DefaultText
import com.mendoxy.mnotes.ui.presentation.components.LoginTextField
import com.mendoxy.mnotes.ui.presentation.login.loginViewModel.LoginViewModel
import com.mendoxy.mnotes.ui.theme.MNotesTheme
import com.mendoxy.mnotes.ui.theme.dimenBig
import com.mendoxy.mnotes.ui.theme.dimenButton
import com.mendoxy.mnotes.ui.theme.dimenExtraLarge
import com.mendoxy.mnotes.ui.theme.dimenLarge
import com.mendoxy.mnotes.ui.theme.dimenMiddle
import com.mendoxy.mnotes.ui.theme.dimenMinDefault
import com.mendoxy.mnotes.ui.theme.dimenSmall
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.mendoxy.mnotes.navigation.AppRoutes
import com.mendoxy.mnotes.ui.presentation.components.ErrorMessageCard
import com.mendoxy.mnotes.ui.utils.LoginErrorType
import com.mendoxy.mnotes.ui.utils.LoginUiState
import com.mendoxy.mnotes.ui.utils.UIState
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel = hiltViewModel()
) {
    val state: LoginUiState by vm.loginState.collectAsState()
    val showError = state.loginState is UIState.Error
    val context = LocalContext.current

    // Configura Google Sign-In
//    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(context.getString(R.string.default_web_client_id))
//        .requestEmail()
//        .build()
//    val googleSignInClient = GoogleSignIn.getClient(context, gso)
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)
//            val idToken = account.idToken
//            if (idToken != null) {
//                vm.loginWithGoogle(idToken)
//            }
//        } catch (e: ApiException) {
//            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }


//    // Esto tarda 2 clicks en login para pasar a la pantalla
//    LaunchedEffect(state.loginState is UIState.Success){
//            navController.navigate(route = AppRoutes.Home.route){
//                popUpTo(AppRoutes.Login.route){inclusive = true}
//            }
//    }
//
//    // Esto hace un bug raro en el que tarda en cambiar de pantalla
//    if(state.loginState is UIState.Success){
//        navController.navigate(route = AppRoutes.Home.route){
//            popUpTo(AppRoutes.Login.route){inclusive = true}
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(dimenLarge),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(Modifier.height(dimenLarge))

            DefaultText(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(dimenMiddle))

            DefaultText(
                text = stringResource(R.string.login_message),
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(Modifier.height(dimenLarge))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
//                        val signInIntent = googleSignInClient.signInIntent
//                        launcher.launch(signInIntent)
                    }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = MaterialTheme.shapes.small
                    )
                    .fillMaxWidth()
                    .height(dimenMinDefault)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
            ) {

                DefaultText(
                    text = stringResource(R.string.login_googleButton),
                    color = MaterialTheme.colorScheme.primary,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_google),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

            Spacer(Modifier.height(dimenExtraLarge))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                DefaultText(
                    text = stringResource(R.string.login_or),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = dimenMiddle)
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(dimenExtraLarge))

            LoginTextField(
                value = state.email,
                onValueChange = {
                    vm.setEmail(it)
                },
                label = stringResource(R.string.login_userLabel),
                placeholder = stringResource(R.string.login_userPlaceholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_user),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )

            Spacer(Modifier.height(dimenLarge))

            LoginTextField(
                value = state.password,
                onValueChange = {
                    vm.setPassword(it)
                },
                label = stringResource(R.string.login_passwordLabel),
                placeholder = stringResource(R.string.login_passwordPlaceholder),
                isPassword = true,
                showPassword = state.showPassword,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_password),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(if(!state.showPassword) R.drawable.ic_visibilityon else R.drawable.ic_visibilityoff),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable {
                           vm.setShowPassword()
                        }
                    )
                }
            )

            Spacer(Modifier.height(dimenLarge))

            Button(
                onClick = {
                    vm.login()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = dimenButton),
                shape = MaterialTheme.shapes.medium,
            ) {
                if(state.loginState != UIState.Loading) {
                    DefaultText(
                        text = stringResource(R.string.login_loginButton),
                        color = MaterialTheme.colorScheme.background,
                    )
                }else{
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimenBig),
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }

            Spacer(Modifier.height(dimenExtraLarge))

            DefaultText(
                modifier = Modifier
                    .clickable {
                        navController.navigate(AppRoutes.Register.route){
                            popUpTo(AppRoutes.Login.route){
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                text = stringResource(R.string.login_newAccount),
                color = MaterialTheme.colorScheme.primary,
                leadingSpacing = dimenSmall,
                leadingIcon = {
                    DefaultText(
                        text = stringResource(R.string.login_noHaveAccount),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )

        }

        LaunchedEffect(showError) {
            if (showError) {
                delay(2000)
                vm.resetError()
            }
        }

        AnimatedVisibility(
            visible = showError,
            enter = slideInVertically(
                // Entra desde abajo
                initialOffsetY = { it },
            ) + fadeIn(),
            exit = slideOutVertically(
                // Sale hacia abajo
                targetOffsetY = { it },
            ) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
        ) {

            ErrorMessageCard(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = when(state.loginState){
                    UIState.Error(error = LoginErrorType.INVALID_EMAIL) -> {
                        stringResource(R.string.login_emailErrorMessage)
                    }
                    UIState.Error(error = LoginErrorType.INVALID_PASSWORD) -> {
                        stringResource(R.string.login_passwordErrorMessage)
                    }
                    UIState.Error(error = LoginErrorType.INVALID_lOGIN) -> {
                        stringResource(R.string.login_loginUnknowErrorMessage)
                    }
                    else -> {
                        stringResource(R.string.login_loginUnknowErrorMessage)
                    }
                },
                leadingContent = {
                    Icon(
                        painter = painterResource( R.drawable.ic_alert),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            )


        }

        LaunchedEffect(state.loginState) {
            if (state.loginState is UIState.Success) {
                navController.navigate(AppRoutes.Home.route) {
                    popUpTo(AppRoutes.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }



    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    MNotesTheme {
        LoginScreen(rememberNavController())
    }
}