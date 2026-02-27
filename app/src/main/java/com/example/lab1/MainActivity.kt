package com.example.lab1
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.lab1.ui.theme.AddDatatoDatabase
import com.example.lab1.ui.theme.ApiConnect
import com.example.lab1.ui.theme.AuthViewModel
import com.example.lab1.ui.theme.AutoCompleteTextField
import com.example.lab1.ui.theme.HomePageScreen
import com.example.lab1.ui.theme.RegisterScreen
import com.example.lab1.ui.theme.ScreenA
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var isChecking = true

        Config.loadConfig(this)

        Log.i("url","${Config.getDbUrl()}")

        lifecycleScope.launch {
            delay(3000L)
            isChecking = false
        }
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                isChecking
            }
        }
        setContent {

            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = RouteA, builder ={
             composable<RouteA> {this
                 ScreenA(navController, authViewModel = AuthViewModel())
                 //RegisterScreen(navController, authViewModel = AuthViewModel())
             }
             composable<RouteHomeScreen>{
                 HomePageScreen(navController, authViewModel = AuthViewModel())
             }
             composable<RegisterScreen> {
                 RegisterScreen(navController, authViewModel = AuthViewModel())
             //    val args = it.toRoute<RouteC>()
                 //AddDatatoDatabase(navController, authViewModel = AuthViewModel())
             }

            })
        }
    }
}

@Serializable
object RouteA

@Serializable
object RouteAddDatabase

@Serializable
data class RouteB(
    var name: String
)

@Serializable
object RouteHomeScreen

@Serializable
object RegisterScreen

@Serializable
data class RouteC(
    var name: String,
    var choose: String
)