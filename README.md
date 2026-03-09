# HIIT Trener 2

**High-Intensity Interval Training companion for Wear OS smartwatches (Xiaomi Watch 2)**

[![Platform](https://img.shields.io/badge/platform-Wear%20OS-4285F4)](https://wearos.google.com/)
[![Language](https://img.shields.io/badge/language-Kotlin-7F52FF)](https://kotlinlang.org/)
[![Min SDK](https://img.shields.io/badge/min%20sdk-30-green)](https://developer.android.com/studio/releases/platforms)

## Overview

HIIT Trener 2 is a specialized interval training application designed for Wear OS smartwatches, specifically optimized for the **Xiaomi Watch 2**. Unlike generic fitness apps, it provides real-time heart rate zone guidance with visual and audio feedback to help you maintain optimal training intensity.

### Designed For
- **Age**: 47 years
- **Max Heart Rate**: 173 BPM (220 - age)
- **Target Zone**: 75-85% (130-147 BPM)
- **Intervals**: 10 rounds
- **Sprint Distance**: Customizable (default 200m)
- **Rest Period**: 15 seconds

## Features

### 🎯 Zone-Based Training
- **Green Pulsing Background**: Visual feedback when you're in the optimal 75-85% heart rate zone
- **Audio Coaching**: Text-to-speech prompts in Serbian ("Ubrzaj", "U zoni", "Odmor", "Kraj")
- **Automatic Rest Trigger**: Instantly switches to rest when HR exceeds 85% (147 BPM)

### 📍 GPS Distance Tracking
- Real-time distance calculation during sprints
- Customizable sprint length (input before each session)
- Accurate tracking using Fused Location Provider

### 🔔 Smart Notifications
- **Vibration alerts** for interval transitions
- **Speed up alerts** when HR drops below 75%
- **Always-on display** support for easy glancing

### 🎨 Minimalist UI
- Clean, high-contrast interface optimized for outdoor visibility
- Large HR display with color-coded zones:
  - 🔴 Red: Above target (&gt;85%)
  - 🟢 Green: Optimal zone (75-85%)
  - 🟡 Yellow: Below target (&lt;75%)

## Screenshots

*Note: Screenshots will be added here*

## Architecture

app/ <br>
├── data/ <br>
│ └── WorkoutState.kt # Data classes & enums <br>
├── service/ <br>
│ └── HIITTrackingService.kt # Foreground service (HR + GPS) <br>
├── tts/ <br>
│ └── TTSHelper.kt # Text-to-speech manager <br>
└── presentation/ <br>
├── MainActivity.kt # UI (Compose) <br>
└── HIITViewModel.kt # Business logic & state management<br>


### Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose for Wear OS
- **Architecture**: MVVM (Model-View-ViewModel)
- **Services**: Foreground Service with Health & Location types
- **Permissions**: BODY_SENSORS, ACCESS_FINE_LOCATION, FOREGROUND_SERVICE

## Installation

### Prerequisites
- Xiaomi Watch 2 (or any Wear OS 3+ device)
- Android Studio Iguana or newer
- ADB debugging enabled on watch

### Build & Install
1. Clone the repository:
   ```bash
   git clone https://github.com/rasa79/HIITTrener2.git
   
### Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose for Wear OS
- **Architecture**: MVVM (Model-View-ViewModel)
- **Services**: Foreground Service with Health & Location types
- **Permissions**: BODY_SENSORS, ACCESS_FINE_LOCATION, FOREGROUND_SERVICE

## Installation

### Prerequisites
- Xiaomi Watch 2 (or any Wear OS 3+ device)
- Android Studio Iguana or newer
- ADB debugging enabled on watch

### Build & Install
1. Clone the repository:
   ```bash
   git clone https://github.com/rasa79/HIITTrener2.git
2. Open in Android Studio and sync Gradle
3. Enable ADB over Wi-Fi on your watch:
    - Settings → Developer Options → ADB Debugging → On
    - Settings → Developer Options → Debug over Wi-Fi → On
4. Connect and install:
 ```bash
 adb connect YOUR_WATCH_IP:5555
 ./gradlew installDebug
```

## Usage

1.  **Launch the app** on your watch
    
2.  **Enter sprint distance** in meters (e.g., 200)
    
3.  **Tap START**
    
4.  **First 2 minutes**: Warm-up phase (optional, can be skipped)
    
5.  **Sprint Phase**:
    
    -   Run until you reach either:
        
        -   Target distance (200m), OR
            
        -   Heart rate > 147 BPM (85%)
            
    -   Watch will vibrate and announce "Odmor" (Rest)
        
6.  **Rest Phase**: 15-second countdown
    
7.  **Repeat** for 10 intervals
    
8.  **Finish**: "Kraj" announcement and summary
    

### Tips for Best Results

-   Wear the watch snugly, 2-3cm above your wrist bone
    
-   Enable "Always-on display" for easy checking
    
-   Grant Location permission "While using the app"
    
-   The app works standalone - no phone required during workout

| Permission             | Reason                        |
| ---------------------- | ----------------------------- |
| `BODY_SENSORS`         | Heart rate monitoring         |
| `ACCESS_FINE_LOCATION` | GPS distance tracking         |
| `FOREGROUND_SERVICE`   | Keep tracking while running   |
| `VIBRATE`              | Haptic feedback for intervals |

## Version History
### v2.0 (Current)
-✅ Direct sensor access (no Google Health Services dependency)<br>
-✅ GPS distance tracking with customizable length<br>
-✅ Heart rate zone visualization (pulsing green background)<br>
-✅ Serbian text-to-speech coaching<br>
-✅ Always-on display support<br>

### v1.0 (Legacy)

-Google Health Services integration<br>
-Time-based intervals only (no GPS)<br>

## Contributing
This is a personal project, but suggestions and forks are welcome! 
If you have a Xiaomi Watch 2 and want to test, please open an issue with your feedback.
## License
**MIT License - Feel free to use and modify for personal training.**

## Author <br>
**Radovan Radivojevic (rasa79)** <br>

Created for personal HIIT training sessions.<br>

Train smart, not just hard.

