package dev.radovanradivojevic.hiit2.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import dev.radovanradivojevic.hiit2.data.WorkoutPhase
import dev.radovanradivojevic.hiit2.data.WorkoutState

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: HIITViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // Permissions granted
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        viewModel = ViewModelProvider(this).get(HIITViewModel::class.java)

        checkPermissions()

        setContent {
            MaterialTheme {
                HIITApp(viewModel)
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val needed = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (needed.isNotEmpty()) {
            requestPermissionLauncher.launch(needed.toTypedArray())
        }
    }
}

@Composable
fun HIITApp(viewModel: HIITViewModel) {
    val state by viewModel.workoutState.observeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state?.phase) {
            WorkoutPhase.SETUP -> SetupScreen(viewModel)
            WorkoutPhase.SPRINT -> SprintScreen(state!!)
            WorkoutPhase.REST -> RestScreen(state!!)
            WorkoutPhase.FINISHED -> FinishScreen(viewModel)
            else -> SetupScreen(viewModel)
        }
    }
}

@Composable
fun SetupScreen(viewModel: HIITViewModel) {
    var distance by remember { mutableStateOf("200") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "HIIT 2",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Distanca (m):",
            fontSize = 14.sp,
            color = Color.Gray
        )
        BasicTextField(
            value = distance,
            onValueChange = { distance = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Button(
            onClick = {
                distance.toFloatOrNull()?.let {
                    viewModel.setTargetDistance(it)
                    viewModel.startWorkout()
                }
            }
        ) {
            Text("START")
        }
    }
}

@Composable
fun SprintScreen(state: WorkoutState) {
    // Pulsing green background when in zone
    val targetColor = if (state.isInTargetZone) Color(0xFF00C853) else Color.Black

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = if (state.isInTargetZone) {
            infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            )
        } else {
            tween(300)
        },
        label = "background"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "SPRINT ${state.intervalsCompleted + 1}",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${state.currentDistance.toInt()}m / ${state.targetDistance.toInt()}m",
                fontSize = 28.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${state.currentHeartRate}",
                fontSize = 48.sp,
                color = when {
                    state.currentHeartRate >= state.targetZoneMax -> Color.Red
                    state.currentHeartRate >= state.targetZoneMin -> Color(0xFF00C853)
                    else -> Color.Yellow
                }
            )
            Text(
                "bpm",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Cilj: ${state.targetZoneMin}-${state.targetZoneMax}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${state.sprintSecondsElapsed}s",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun RestScreen(state: WorkoutState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "ODMOR",
            fontSize = 24.sp,
            color = Color.Green
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "${state.restSecondsRemaining}",
            fontSize = 56.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "sekundi",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "HR: ${state.currentHeartRate}",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun FinishScreen(viewModel: HIITViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "ZAVRŠENO!",
            fontSize = 28.sp,
            color = Color.Green
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "10 intervala\nzavršeno",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { viewModel.stopWorkout() }) {
            Text("IZAĐI")
        }
    }
}