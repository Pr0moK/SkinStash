package com.example.lab1.ui.theme

import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.location.LocationRequestCompat
import androidx.core.view.ContentInfoCompat
import com.example.lab1.ui.theme.ApiConnect
import com.example.lab1.RouteHomeScreen
import com.google.android.play.core.integrity.t
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.cert.TrustAnchor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var sensorManager: SensorManager
private var mSensor: Sensor? = null





@Composable
fun ScreenA(navController: NavController, authViewModel: AuthViewModel) {
    val authState by authViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            CheckIfUserExist()
            navController.navigate(RouteHomeScreen)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Zaloguj się",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextFieldEmailCustom(
            email = email,
            onEmailChange = { email = it }
        )

        TextFieldPasswordCustom(
            password = password,
            onPasswordChange = { password = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ButtonAkceptuj(
            email = email,
            password = password,
            authViewModel = authViewModel,
            isLoading = authState is AuthState.Loading
        )
    }
}

@Composable
fun TextFieldEmailCustom(email: String, onEmailChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Wpisz swój email")
        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") }
        )
    }
}

@Composable
fun TextFieldPasswordCustom(password: String, onPasswordChange: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Wpisz swoje hasło")
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Hasło") },
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

@Composable
fun ButtonAkceptuj(
    email: String,
    password: String,
    authViewModel: AuthViewModel,
    isLoading: Boolean
) {
    val context = LocalContext.current

    Button(
        onClick = {
            if (email.isNotBlank() && password.isNotBlank()) {
                authViewModel.login(email, password)
            } else {
                Toast.makeText(context, "Uzupełnij dane", Toast.LENGTH_SHORT).show()
            }
        },
        enabled = !isLoading
    ) {
        Text(if (isLoading) "Logowanie..." else "Przejdź dalej")
    }
}



fun CheckIfUserExist(){
    val database: DatabaseReference
    val databaseurl ="https://androidlab1-e1069-default-rtdb.europe-west1.firebasedatabase.app/"
    database = Firebase.database(databaseurl).reference
    val auth = FirebaseAuth.getInstance()
    val userID = auth.currentUser?.uid

    database.child("users").child(userID.toString()).get().addOnSuccessListener { snapshot ->
        if(!snapshot.exists()){
             database.child("users").child(userID.toString()).setValue(mapOf(
                "CreatedAt" to System.currentTimeMillis()

             ))
            Log.i("Autoryzacja", "Uzytkownik dodany do bazy")
        } else {
            Log.i("Autoryzacja", "Uzytkownik jest w bazie")
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DodajItem() {
    Lab1Theme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddItemsButon()
        }

       // Greeting(data)
        //TextFieldemail(data)
    }
}