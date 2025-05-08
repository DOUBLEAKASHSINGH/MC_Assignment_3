# News App

**Submitted to:** Professor Arani Bhattacharya

## Description
This is a simple Android News App made with Kotlin and Jetpack Compose.  
Users can:
- Register and log in on their device  
- Read top news headlines  
- Search for news by keyword or source  
- See local news based on their location  
- Tap a headline to read the full article in a web browser  

## Features
- **User Authentication**: Sign up and log in with a local database (Room)  
- **Top Headlines**: Fetches and shows latest news from NewsAPI  
- **Search**: Find news articles by typing a keyword  
- **Location News**: Detects your country/city and shows local headlines  
- **Modern UI**: Built entirely with Jetpack Compose for a clean look  

## Tech Stack
- **Android Studio** (Kotlin + Jetpack Compose)  
- **Room Database** for storing users  
- **Retrofit** for HTTP calls to NewsAPI  
- **NewsAPI** (newsapi.org) for news data  
- **Android Location Services** for getting user location  
- **ViewModel + LiveData** for UI data handling  

## Prerequisites
- Android Studio Arctic Fox or newer  
- An API key from [NewsAPI.org](https://newsapi.org/)  
- Android device or emulator running Android 8.0 (API 26) or above  

## Installation & Run
1. Clone or unzip the project folder.  
2. Open it in Android Studio.  
3. In `NewsApi.kt`, replace `YOUR_API_KEY` with your NewsAPI key.  
4. Build and run on emulator or device.  
5. Grant location permission when prompted to see local news.  

## Project Structure

MC_Project_2024-main/
├─ app/
│ ├─ src/main/java/com/example/news/
│ │ ├─ api/ ← Retrofit interfaces & data classes
│ │ ├─ database/ ← Room entities, DAO, ViewModel
│ │ ├─ bottomNavigation/ ← Navigation setup
│ │ ├─ LoginScreen.kt
│ │ ├─ RegisterScreen.kt
│ │ ├─ Headline.kt ← Home screen
│ │ ├─ Search.kt ← Search screen
│ │ ├─ Profile.kt ← Profile & local news
│ │ └─ MainActivity.kt
│ └─ src/main/res/ ← App resources (drawables, values)
├─ build.gradle.kts ← Gradle settings and dependencies
└─ README.md ← This file


## How It Works
1. **Launch** the app.  
2. **Register** a new account or **Login**.  
3. You see the **Home** screen with top headlines.  
4. Use the **Search** screen to find news on any topic.  
5. Go to **Profile** to see your info and local headlines.  
6. Tap any headline to read the full article in your browser.  

## Notes
- All UI is in Jetpack Compose—no XML layouts.  
- Default tests (`ExampleUnitTest`, `ExampleInstrumentedTest`) are included but not used for core features.  
- Make sure internet and location permissions are allowed.  

---
Feel free to update or expand this as needed!  

