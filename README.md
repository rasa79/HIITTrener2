# HIIT Trener 2

High-Intensity Interval Training app for Xiaomi Watch 2 (Wear OS).

## Features

- **GPS Distance Tracking** - Customizable sprint length (default 200m)
- **Heart Rate Zones** - Visual pulsing green when in 75-85% zone (130-147 BPM)
- **Audio Coaching** - Serbian TTS: "Ubrzaj", "U zoni", "Odmor", "Kraj"
- **Automatic Rest** - Triggers when HR &gt; 85% or distance reached
- **Always-on Display** - Optimized for outdoor training

## Setup

1. Install APK on Xiaomi Watch 2
2. Grant permissions: Sensors & Location
3. Enter sprint distance (meters)
4. Press START

## Training Protocol

- **Warm-up**: 2 minutes (optional)
- **Sprint**: Run until 200m OR HR &gt; 147 BPM
- **Rest**: 15 seconds
- **Repeat**: 10 intervals
- **Audio alerts**: Speed up if HR &lt; 130 BPM

## Tech Stack

- Kotlin
- Jetpack Compose for Wear OS
- MVVM Architecture
- Direct sensor access (no Google Health Services)
- Fused Location Provider (GPS)

## Permissions

- BODY_SENSORS (Heart rate)
- ACCESS_FINE_LOCATION (GPS distance)
- FOREGROUND_SERVICE

## Author

Radovan Radivojevic
