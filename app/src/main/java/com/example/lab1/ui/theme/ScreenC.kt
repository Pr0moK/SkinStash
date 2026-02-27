package com.example.lab1.ui.theme

import android.content.Context.SENSOR_SERVICE
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lab1.R
import com.example.lab1.RouteC
import kotlinx.serialization.internal.throwMissingFieldException
private lateinit var sensorManager: SensorManager

@Composable
fun ScreenC(args: RouteC){
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(0.dp,200.dp)
//
//        ,
//        horizontalAlignment = Alignment.CenterHorizontally
//
//    ){
//        if(args.choose == "Tak"){
//            Text("Gratulacje ${args.name}, pozbyłeś się oceny 5.5")
//            Image(
//
//                painter = painterResource(id = R.drawable.star5),
//                contentDescription = "grade",
//                modifier = Modifier.padding(top = 16.dp)
//
//            )
//
//        } else {
//            Text("Gratulacje ${args.name}, pozostawiłeś ocene 5.5")
//            Image(
//                painter = painterResource(id = R.drawable.star55),
//                contentDescription = "grade",
//                modifier = Modifier.padding(top = 16.dp)
//            )
//        }
//    }

    NotificationAboutDataSensorAcelero()
}

@Composable
fun Greetingss() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp,200.dp)

        ,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(
            text = "Dwa",
        )
    }
}


@Composable
fun NotificationAboutDataSensorAcelero() {

    val context = LocalContext.current
    sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var z by remember { mutableStateOf(0f) }

    var horizontalval by remember { mutableStateOf(200) }
    var veritalval by remember{ mutableStateOf(200)}

    val acelero = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


    if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
        Log.d("acelero", "Dzialam lol")

        DisposableEffect(acelero) {

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        x = it.values[0]
                        y = it.values[1]
                        z = it.values[2]
                        Log.d("acelero_SENSOR", "Sensor zmienil X: ${x} Y: ${y} Z ${z}")
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                acelero,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            Log.d("acelero", "Listener jest")

            onDispose {
                sensorManager.unregisterListener(listener)
                Log.d("acelero", "Listener usniety")
            }
        }

        Log.d("acelero", "$x; $y; $z")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("X: ${x}, Y: ${y}, Z: ${z}", color = androidx.compose.ui.graphics.Color.Black, fontSize = 20.sp)



            horizontalval = (200 - x.toInt() * 5).coerceIn(0, 500)
            veritalval = (200 + y.toInt() * 5).coerceIn(0, 500)


            Image(
                painter = painterResource(id = R.drawable.car),
               contentDescription = "car",
                modifier = Modifier
                    .padding(top = veritalval.dp)
                    .padding(start = horizontalval.dp)
 )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingssPreview() {
    Lab1Theme {

    }
}}