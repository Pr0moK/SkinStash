package com.example.lab1.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab1.RouteHomeScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class UserData{
    var email = ""
    var password = ""
}

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {

    val User = remember { UserData() }
    val authState = authViewModel.authState.observeAsState()
    val state = authState.value

    Column(
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {    emailTextField(User)
        passwordTextField(User)
        RegisterButton(authViewModel, User)
    }

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated){

            CheckIfUserExist()

            navController.navigate(RouteHomeScreen)

        }


    }


}

@Composable
fun emailTextField(userdata: UserData){
    TextField(value = userdata.email, onValueChange = {newText ->
        userdata.email = newText
    })
}

@Composable
fun passwordTextField(userdata: UserData){
    TextField(value = userdata.password, onValueChange = {newText ->
        userdata.password = newText
    })
}

@Composable
fun RegisterButton(authViewModel: AuthViewModel,userdata: UserData){
    Button(onClick = {

        if (userdata.email != "" && userdata.password != ""){
            authViewModel.register(userdata.email, userdata.password)
        }
    }) {
        Text("Register")
    }
}