package dev.radovanradivojevic.hiit2.presentation

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.radovanradivojevic.hiit2.data.WorkoutPhase
import dev.radovanradivojevic.hiit2.data.WorkoutState
import dev.radovanradivojevic.hiit2.service.HIITTrackingService
import dev.radovanradivojevic.hiit2.tts.TTSHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HIITViewModel(application: Application) : AndroidViewModel(application) {

    private val _workoutState = MutableLiveData(WorkoutState())
    val workoutState: LiveData<WorkoutState> = _workoutState

    private var trackingService: HIITTrackingService? = null
    private val ttsHelper = TTSHelper(application)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as HIITTrackingService.LocalBinder
            trackingService = binder.getService()
            trackingService?.setCallback { hr, distance ->
                processUpdate(hr, distance)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            trackingService = null
        }
    }

    fun setTargetDistance(meters: Float) {
        _workoutState.value = _workoutState.value?.copy(targetDistance = meters)
    }

    fun startWorkout() {
        val context = getApplication<Application>()
        Intent(context, HIITTrackingService::class.java).also { intent ->
            context.startForegroundService(intent)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        startSprint()
    }

    private fun processUpdate(hr: Int, distance: Float) {
        val state = _workoutState.value ?: return

        when (state.phase) {
            WorkoutPhase.SPRINT -> handleSprint(hr, distance, state)
            WorkoutPhase.REST -> {
                _workoutState.value = state.copy(currentHeartRate = hr)
            }
            else -> {}
        }
    }

    private fun handleSprint(hr: Int, distance: Float, state: WorkoutState) {
        val inZone = hr >= state.targetZoneMin && hr < state.targetZoneMax
        val aboveZone = hr >= state.targetZoneMax
        val belowZone = hr < state.targetZoneMin

        // End sprint: distance reached OR above 85%
        if (distance >= state.targetDistance || aboveZone) {
            trackingService?.vibrate()
            startRest()
            return
        }

        // Zone alerts
        if (belowZone && !state.belowZoneAlerted) {
            ttsHelper.speakSpeedUp()
            _workoutState.value = state.copy(
                currentHeartRate = hr,
                currentDistance = distance,
                belowZoneAlerted = true,
                isInTargetZone = false
            )
        } else if (inZone && !state.isInTargetZone) {
            ttsHelper.speakInZone()
            _workoutState.value = state.copy(
                currentHeartRate = hr,
                currentDistance = distance,
                isInTargetZone = true,
                belowZoneAlerted = false
            )
        } else {
            _workoutState.value = state.copy(
                currentHeartRate = hr,
                currentDistance = distance,
                isInTargetZone = inZone
            )
        }
    }

    private fun startSprint() {
        val state = _workoutState.value ?: return
        trackingService?.resetDistance()
        ttsHelper.speakSprintStart(state.intervalsCompleted + 1)
        _workoutState.value = state.copy(
            phase = WorkoutPhase.SPRINT,
            currentDistance = 0f,
            belowZoneAlerted = false,
            isInTargetZone = false,
            sprintSecondsElapsed = 0
        )
        startSprintTimer()
    }

    private fun startSprintTimer() {
        viewModelScope.launch {
            while (_workoutState.value?.phase == WorkoutPhase.SPRINT) {
                delay(1000)
                val current = _workoutState.value ?: break
                _workoutState.value = current.copy(sprintSecondsElapsed = current.sprintSecondsElapsed + 1)
            }
        }
    }

    private fun startRest() {
        val state = _workoutState.value ?: return
        ttsHelper.speakRestStart()
        _workoutState.value = state.copy(phase = WorkoutPhase.REST, restSecondsRemaining = 15)

        viewModelScope.launch {
            var seconds = 15
            while (seconds > 0) {
                delay(1000)
                seconds--
                _workoutState.value = _workoutState.value?.copy(restSecondsRemaining = seconds)
            }
            trackingService?.vibrate()

            val current = _workoutState.value ?: return@launch
            if (current.intervalsCompleted >= 9) {
                ttsHelper.speakFinished()
                _workoutState.value = current.copy(phase = WorkoutPhase.FINISHED)
                stopWorkout()
            } else {
                _workoutState.value = current.copy(
                    intervalsCompleted = current.intervalsCompleted + 1
                )
                startSprint()
            }
        }
    }

    fun stopWorkout() {
        val context = getApplication<Application>()
        trackingService?.stopTracking()
        context.unbindService(serviceConnection)
        context.stopService(Intent(context, HIITTrackingService::class.java))
        ttsHelper.destroy()
    }

    override fun onCleared() {
        super.onCleared()
        stopWorkout()
    }
}