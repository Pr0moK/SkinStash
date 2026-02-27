package com.example.lab1.ui.theme

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab1.RouteA
import com.example.lab1.RouteHomeScreen

//@Composable
//fun AddDatatoDatabase(navController: NavController,authViewModel: AuthViewModel){
//
//
//    val database: DatabaseReference
//    val databaseurl ="https://androidlab1-e1069-default-rtdb.europe-west1.firebasedatabase.app/"
//    database = Firebase.database(databaseurl).reference
//    var dataFromFirebase by remember{ mutableStateOf(listOf<String>()) }
//    val authState = authViewModel.authState.observeAsState()
//    val state = authState.value
//
//    val data = remember { ItemData() }
//    Column(modifier = Modifier
//        .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally) {
//        Text("Wprowadzanie osobnika do bazy")
//        InputName(data)
//        Spacer(modifier = Modifier.height(30.dp))
//        Inputpassword(data)
//        Spacer(modifier = Modifier.height(30.dp))
//        SendDataButton(data)
//        Spacer(modifier = Modifier.height(30.dp))
//        BackButton(navController)
//    }

//    LaunchedEffect(state) {
//        if(state is AuthState.Unauthenticated){
//            navController.navigate(RouteA)
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        database.child("users").get().addOnSuccessListener { snapshot ->
//
//            val data = snapshot.value as? Map<String, Map<String, String>>
//
//
//            if (data != null) {
//                for ((userID, userMap) in data) {
//                    var temp = "${userMap["name"]}\n${userMap["passwordword"]}"
//                    Log.i("firebasedata", "got data ${userMap["name"]}\n${userMap["passwordword"]}")
//                    dataFromFirebase = dataFromFirebase + temp
//                }
//            }
//
//
//        }.addOnFailureListener {
//            Log.e("firebasedata", "ERROR,")
//
//        }
//    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(bottom = 200.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Użykownicy w bazie danych:")
//        Spacer(modifier = Modifier.height(20.dp))
//
//        if (dataFromFirebase.isEmpty()){
//            Text("Ładowanie...")
//        } else {
//            dataFromFirebase.forEach { item ->
//                Text(item)
//                Spacer(modifier = Modifier.height(20.dp))
//            }
//        }
//    }

//}

//@Composable
//fun BackButton(navController: NavController){
//    Column(
//        modifier = Modifier
//            .padding(top = 20.dp)
//    ) { }
//    Button(onClick = {
//        navController.navigate(RouteHomeScreen)
//    }) {
//        Text("Wróć")
//    }
//}
//
//@Composable
//fun InputName(data: UserData){
//
//    TextField(value = data.name, onValueChange = {newText ->
//        data.name = newText
//    },
//        label = { Text("Wpisz login") })
//}
//
//@Composable
//fun Inputpassword(data: UserData){
//
//    TextField(value = data.password, onValueChange = {newText ->
//        data.password = newText
//    },
//        label = { Text("Wpisz haslo") })
//}
//
//@Composable
//fun SendDataButton(data: UserData){
//    val context = LocalContext.current
//    val databaseUrl = "https://androidlab1-e1069-default-rtdb.europe-west1.firebasedatabase.app/"
//    val database = Firebase.database(databaseUrl).reference
//
//
//    Button(onClick = {
//
//
//        if (data.name != "" && data.password != "") {
//                    val newUser = database.child("users").push()
//                    newUser.setValue(data).addOnSuccessListener {
//                        Log.i("DataBaseAdd", "Added to database ${data.name},${data.password}")
//                    }.addOnFailureListener {
//                        Log.e("DataBaseAdd", "Failed to add to database", it)
//                    }
//            }
//        else {
//            Toast.makeText(context, "Nie udalo sie dodac, wypelnij pola", Toast.LENGTH_LONG).show()
//        }
//
//    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
//
//        Text("Dodaj do bazy")
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AddtodatabasePreview() {
//    Lab1Theme {
//        val data = remember { UserData() }
//        Column(modifier = Modifier
//            .fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally) {
//
//            Inputpassword(data)
//            Spacer(modifier = Modifier.height(30.dp))
//            Inputpassword(data)
//            Spacer(modifier = Modifier.height(30.dp))
//            SendDataButton(data)
//        }
//    }
//}