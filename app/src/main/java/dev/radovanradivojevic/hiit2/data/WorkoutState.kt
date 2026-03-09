package dev.radovanradivojevic.hiit2.data

data class WorkoutState(
    val phase: WorkoutPhase = WorkoutPhase.SETUP,
    val currentHeartRate: Int = 0,
    val currentDistance: Float = 0f,
    val targetDistance: Float = 200f,
    val restSecondsRemaining: Int = 0,
    val sprintSecondsElapsed: Int = 0,
    val intervalsCompleted: Int = 0,
    val maxHeartRate: Int = 173,
    val targetZoneMin: Int = 130, // 75%
    val targetZoneMax: Int = 147, // 85%
    val isInTargetZone: Boolean = false,
    val belowZoneAlerted: Boolean = false
)

enum class WorkoutPhase {
    SETUP,
    SPRINT,
    REST,
    FINISHED
}