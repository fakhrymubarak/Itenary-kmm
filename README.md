# Itenary

An AI travel itinerary app built with Kotlin / Compose Multiplatform. You enter a destination, the number of days, time window and pace per day, and the app asks a backend to generate a day-by-day plan and streams it to the screen as it is produced.

The backend is a separate Go service: [fakhrymubarak/itenary-backend](https://github.com/fakhrymubarak/itenary-backend).

## Features

- Request an itinerary: place, one or more days (start/end time, pace), language and LLM choice.
- Streaming results: the plan is received over Server-Sent Events (`/sse/generate-itinerary`) and rendered day by day, with shimmer placeholders while loading.
- Promotional banner carousel on the home screen, loaded from the backend.
- History: generated itineraries are cached locally (Room/SQLite) and can be reopened.
- Bottom navigation: Itinerary, Food, History (the Food tab is a placeholder screen).

## Platform status

- **Android**: full app (Compose UI in `composeApp`).
- **iOS**: the `shared` module (networking, database, domain logic) builds as a `Shared` framework and is wired into the Xcode project, but `iosApp` is still the template SwiftUI screen. The Compose UI is not yet running on iOS.

## Tech stack

Kotlin 2.0.21, Compose Multiplatform 1.8.2, Material 3, Ktor 3 client (SSE, OkHttp on Android / Darwin on iOS), kotlinx.serialization, Room + bundled SQLite (KSP), Koin, Orbit MVI, Navigation Compose, Coil, Compose Shimmer, Inspektor (Android HTTP inspector). Android minSdk 29, target/compileSdk 36, JVM 17.

## Module layout

```
composeApp/   Android app: UI (Compose), navigation, theme, ViewModels (Orbit)
              features/dashboard/{itinerary,food,history}, features/itinerary/results
shared/       KMP library (Android + iOS): data (Ktor, Room, mappers), domain
              (models, repositories, use cases), Koin modules, utilities
iosApp/       Xcode project that consumes the Shared framework
docs/         Notes (multi-module refactor plan)
tools/        update_ip.sh - helper to point the app at your machine's LAN IP
```

## Build and run

Requirements: JDK 17, Android SDK (set `sdk.dir` in `local.properties`), Xcode for iOS.

Android:

```
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:installDebug      # with a device/emulator connected
```

iOS: open `iosApp/iosApp.xcodeproj` in Xcode and run. Set `TEAM_ID` in `iosApp/Configuration/Config.xcconfig` for device builds. To build just the framework: `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64`.

Unit tests: `./gradlew :composeApp:testDebugUnitTest` and `./gradlew :shared:allTests`.

## Configuration

- `local.properties`: `sdk.dir` (Android SDK path). Not committed.
- `BASE_URL` constant in `shared/src/commonMain/kotlin/com/itenary/shared/di/SharedKoinModule.kt`: backend address (the backend listens on port 8080). Change it to your backend host.
- `composeApp/src/androidMain/res/xml/network_security_config.xml`: cleartext (http) domains allowed on Android; add your backend host here.
- `tools/update_ip.sh` updates both of the above with your current `en0` IP (macOS). Its file paths are hardcoded to the author's machine, so edit them first.
- `iosApp/Configuration/Config.xcconfig`: `TEAM_ID` for iOS signing.

Run the backend first (see its repo), then launch the app.

## Known issues

1. Banner cannot be swiped.
2. ~~Places input: first typing is a double char and the arrangement is messy.~~ (fixed)
3. Request screen scroll is still buggy (Scaffold padding somehow included).
4. "Add more day" button is too big.
5. With more than one day, recomposition re-renders the entire section.
6. Streamed itinerary is not animated.
7. "Add more day" is not animated.
8. Code cleanliness: many hardcoded strings and messy theming.
