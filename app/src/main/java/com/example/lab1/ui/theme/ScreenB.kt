package com.example.lab1.ui.theme

import android.R
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import com.example.lab1.RouteB
import com.example.lab1.RouteC

private lateinit var sensorManager: SensorManager

class RadioData {
    var selectedOption by mutableStateOf("")
}

@Composable
fun ScreenB(navController: NavController, args: RouteB){
    val dataa = remember { RadioData() }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("${args.name}, czy jesteś za zniesieniem oceny 5.5?")
//        RadioButtonSingleSelection(dataa)
//    }

    NotificationAboutDataSensorLight()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        buttondwa(dataa.selectedOption,args.name,navController)
    }

}

@Composable
fun RadioButtonSingleSelection(decision: RadioData, modifier: Modifier = Modifier) {
    val radioOptions = listOf("Tak", "Nie")

    Column(modifier.selectableGroup()) {

        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == decision.selectedOption),
                        onClick = { decision.selectedOption = text },
                        role = Role.RadioButton,

                    )
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                RadioButton(
                    selected = (text == decision.selectedOption),
                    onClick = null //
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun buttondwa(tekst: String,datafromA: String, navController: NavController) {

    val context = LocalContext.current


        Button(onClick = {
            if(tekst =="")
                navController.navigate(RouteC(
                    name = datafromA,
                    choose = tekst
                ))
            Toast.makeText(context, tekst, Toast.LENGTH_LONG).show() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green, contentColor = Color.Black)
        ) {
            Text("Przejdź dalej")
        }
}

@Composable
fun NotificationAboutDataSensorLight() {

    val context = LocalContext.current
    sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    var sensorData: Int by remember { mutableIntStateOf(0) }


    val lightmeter = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)


    if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
        Log.d("LIGHT", "Dzialam lol")

        DisposableEffect(lightmeter) {

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        sensorData = it.values[0].toInt()
                        Log.d("LIGHT_SENSOR", "Sensor changed: $sensorData")
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                lightmeter,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            Log.d("LIGHT", "Listener jest")

            onDispose {
                sensorManager.unregisterListener(listener)
                Log.d("LIGHT", "Listener usniety")
            }
        }

        Log.d("LIGHT_UI", "$sensorData")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = when (sensorData) {
                        in 0..10 -> Color.Black
                        in 11..70 -> Color.Gray
                        in 71..100 -> Color.LightGray
                        in 101..200 -> Color.Red
                        else -> Color.White
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mamy ${sensorData} luxów", color = Color.Green)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingsPreview(navController: NavController, args: RouteB) {
        Lab1Theme {
            val dataa = remember { RadioData() }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Czy jesteś za zniesieniem oceny 5.5?")
                RadioButtonSingleSelection(dataa)
                buttondwa(dataa.selectedOption, args.name, navController)
            }
        }
    }
}