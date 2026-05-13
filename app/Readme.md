#  Gokula-Health

Gokula-Health is a modern Android application designed for dairy farmers to digitally manage cattle health, milk production, vaccination schedules, and breeding cycles.

The app works as a digital livestock health card that helps farmers improve dairy productivity, reduce missed vaccinations, and track cattle performance using a simple and farmer-friendly interface.

---

#  Features

##  Cattle Management
- Register cattle profiles
- Store cow photo and ear tag ID
- Track breed, age, and weight
- View complete cattle history

##  Milk Diary
- Daily morning/evening milk entries
- Automatic total milk calculation
- Monthly average yield calculation
- Milk production tracking

##  Vaccination Alerts
- Schedule vaccination reminders
- Offline notification support
- Track vaccination history
- Reminder system using AlarmManager

##  Yield Analytics
- Milk production graphs
- Monthly and weekly trends
- Production drop analysis
- Visual analytics using MPAndroidChart

##  Heat Cycle Tracking
- Record heat cycle dates
- Predict next cycle
- Breeding reminders
- Fertility tracking

##  Modern UI
- Farmer-friendly dashboard
- Modern Material 3 design
- Earthy agriculture-inspired theme

---

#  Tech Stack

## Language
- Kotlin

## UI
- Jetpack Compose
- Material 3

## Architecture
- MVVM Architecture
- Repository Pattern

## Database
- Room Database

## Dependency Injection
- Hilt

## Navigation
- Navigation Compose

## Charts
- MPAndroidChart

## Async Operations
- Kotlin Coroutines
- StateFlow

## Notifications
- AlarmManager
- BroadcastReceiver
- NotificationManager

## Image Loading
- Coil

---

#  Project Structure

```bash
com.gokulahealth
│
├── data
│   ├── local
│   ├── repository
│   └── model
│
├── di
│
├── ui
│   ├── screens
│   ├── components
│   ├── navigation
│   └── theme
│
├── viewmodel
│
├── utils
│
├── notification
│
└── MainActivity.kt
```

---
# Screenshots


___
#  Getting Started

## Prerequisites

- Android Studio Latest Version
- Kotlin Support
- Android SDK 24+
- Gradle Installed

---
#  Running the App

1. Clone the repository

```bash
git clone <your-repo-link>
```

2. Open the project in Android Studio

3. Sync Gradle

4. Connect Android device or start emulator

5. Click Run

---

#  Minimum Requirements

- Android 7.0 (API 24) or higher
- Internet not required for core features
- Notification permissions enabled

---

#  Architecture Overview

The app follows MVVM Architecture:

- UI Layer → Jetpack Compose Screens
- ViewModel Layer → State Management
- Repository Layer → Data Handling
- Local Database Layer → Room DB

Benefits:
- Clean code structure
- Scalable architecture
- Easy maintenance
- Better state handling

---

#  Design System

The app uses a custom agriculture-inspired theme:
- Forest green primary colors
- Earth brown accents
- Cream and ivory backgrounds
- Full dark mode support

The UI is designed to be:
- simple
- visual
- touch-friendly
- accessible for farmers

---

#  Offline Reminder System

Vaccination reminders work fully offline using:
- AlarmManager
- BroadcastReceiver
- Local notifications

This ensures farmers receive reminders even without internet access.

---

#  Future Improvements

- Cloud backup support
- AI-based cattle health prediction
- Voice support in regional languages
- Multi-farmer management
- Veterinary consultation system
- IoT sensor integration
- Firebase sync

---

#  Impact

Gokula-Health aims to support:
- Better livestock management
- Increased dairy productivity
- Rural digitization
- Reduced cattle health risks
- Improved farmer income

---

#  License

This project is for educational and learning purposes.

---