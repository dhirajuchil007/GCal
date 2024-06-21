package com.velocityappsdj.gcal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.velocityappsdj.gcal.common.Constants
import com.velocityappsdj.gcal.network.model.request.TokenGenerationRequest
import com.velocityappsdj.gcal.network.model.response.Event
import com.velocityappsdj.gcal.ui.google.GoogleAuthUiClient
import com.velocityappsdj.gcal.ui.screens.AddEvent
import com.velocityappsdj.gcal.ui.screens.EventDetails
import com.velocityappsdj.gcal.ui.screens.HomeScreen
import com.velocityappsdj.gcal.ui.screens.LoginScreen
import com.velocityappsdj.gcal.ui.state.LoginState
import com.velocityappsdj.gcal.ui.theme.GCalTheme
import com.velocityappsdj.gcal.ui.viewmodel.HomeViewModel
import com.velocityappsdj.gcal.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime


enum class Screen {
    HOME,
    LOGIN,
    EVENT_DETAILS,
    ADD_EVENT
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var viewModel: LoginViewModel

    companion object {
        const val RC_SIGN_IN = 1001
        private const val TAG = "MainActivity"
    }

    private val firebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GCalTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                viewModel = hiltViewModel<LoginViewModel>()
                val login = viewModel.tokenGenerationState.collectAsState()
                App(navController, context, scope, login)
            }
        }

    }

    @Composable
    private fun App(
        navController: NavHostController,
        context: Context,
        scope: CoroutineScope,
        login: State<LoginState>
    ) {
        val startDestination =
            if (firebaseAuth.currentUser != null || login.value is LoginState.Success)
                Screen.HOME.name else Screen.LOGIN.name
        val eventViewModel = hiltViewModel<HomeViewModel>()
        NavHost(navController = navController, startDestination = startDestination) {
            composable(Screen.LOGIN.name) {
                LoginScreen {
                    startLoginFlow(context, scope, viewModel)
                }
            }
            composable(Screen.HOME.name) {
                LaunchedEffect(key1 = null) {
                    eventViewModel.getEventsInitial()
                }
                val state = eventViewModel.state.collectAsState()
                HomeScreen(state.value, eventViewModel::onAction) { event ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("event", event)
                    navController.navigate(Screen.EVENT_DETAILS.name)
                }
            }
            composable(Screen.EVENT_DETAILS.name) {
                val event: Event? =
                    navController.previousBackStackEntry?.savedStateHandle?.get("event")
                EventDetails(event = event) {
                    navController.navigateUp()
                }
            }
        }
    }

    private fun startLoginFlow(context: Context, scope: CoroutineScope, viewModel: LoginViewModel) {
        scope.launch {
            GoogleAuthUiClient(context, firebaseAuth).signIn(::startCalendarPermissionFlow)
        }
    }

    private fun startCalendarPermissionFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(
                Constants.WEB_CLIENT_ID, true
            )
            .requestScopes(
                Scope(Scopes.PROFILE),
                Scope(Scopes.EMAIL),
                Scope("https://www.googleapis.com/auth/userinfo.profile"),
                Scope("https://www.googleapis.com/auth/calendar")
            )
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

            // Signed in successfully
            val serverAuthCode = account.serverAuthCode

            Log.d(TAG, "onActivityResult: ${serverAuthCode}")
            viewModel.generateToken(
                TokenGenerationRequest(
                    code = serverAuthCode,
                    clientId = Constants.WEB_CLIENT_ID,
                    clientSecret = Constants.CLIENT_SECRET,
                )
            )


        } else {
            firebaseAuth.signOut()
            viewModel.setLoginFailed("Google calendar permission denied")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GCalTheme {
        Greeting("Android")
    }
}