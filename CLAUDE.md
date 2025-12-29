# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Brain Gain App is a native Android cognitive training application featuring multiple brain exercise games:
- **Dual N-Back**: Working memory and attention training game (primary game)
- **Stroop Game**: Cognitive control and selective attention game
- **Monkey Ladder**: Another cognitive skill training game

**Package ID**: `com.monkeyladder`
**Min SDK**: 24 (Android 7.0)
**Target SDK**: 34 (Android 14)
**Java Version**: 11

## Build Commands

### Building the App
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean
```

### Running Tests
```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests com.util.DateUtilTest

# Run tests with reports
./gradlew test --info

# Run instrumented Android tests (requires emulator or device)
./gradlew connectedAndroidTest
```

### Installing to Device
```bash
# Install debug build to connected device/emulator
./gradlew installDebug

# Uninstall from device
./gradlew uninstallDebug
```

### Other Useful Commands
```bash
# List all available tasks
./gradlew tasks

# Check for dependency updates
./gradlew dependencyUpdates
```

## Architecture Overview

The app follows a **Model-View-Presenter (MVP)** architecture pattern with clear separation of concerns:

### Package Structure

```
com.monkeyladder/           # Root package (application namespace)
├── com.dualnback/          # Dual N-Back game module
│   ├── game/               # Core game logic and mechanics
│   ├── data/               # Game data (sounds, locations, trials)
│   └── ui/                 # UI components (activities, fragments)
├── com.stroop/             # Stroop game module
│   ├── ui/                 # Stroop UI components
│   └── element/            # UI elements specific to Stroop
├── com.monkeyladder/       # Monkey Ladder game module
│   ├── game/               # Game logic
│   ├── util/random/        # Random trial generators
│   └── ui/                 # UI components
├── com.mainscreen/         # Main app navigation
│   └── ui/                 # Landing, game selection, countdown
├── com.chart/              # Performance tracking and charts
│   ├── filesystem/         # File-based data persistence
│   │   ├── io/             # File I/O operations
│   │   ├── dao/            # Data access objects
│   │   └── json/           # JSON serialization
│   └── ui/                 # Chart display UI
└── com.util/               # Shared utilities
```

### Key Architectural Patterns

#### 1. MVP Pattern (Model-View-Presenter)
Each game follows the same structure:
- **Activity**: Acts as the View, handles UI rendering and user interactions
- **Presenter**: Contains business logic, coordinates between Model and View
- **Model**: Manages game state and data
- **Contracts**: Interfaces defining contracts between components (e.g., `MainActivityViewContract`, `MainActivityPresenterContract`, `MainActivityModelContract`)

#### 2. Activity Flow
All games follow this activity sequence:
```
LandingScreenActivity (Splash screen)
    ↓
GameSelectionActivity (Choose game)
    ↓
[Game-specific StartScreen/Settings if needed]
    ↓
CountdownActivity (3-2-1 countdown)
    ↓
MainActivity (Actual gameplay)
    ↓
ContinueActivity/ContinueScreenActivity (Game stats and continue)
    ↓
ChartActivity (Performance history - optional)
```

#### 3. Intent Utilities
Each activity has a corresponding `*Intent` helper class (e.g., `MainActivityIntent`, `CountdownActivityIntent`) that encapsulates intent creation and parameter passing. Use these when navigating between activities.

#### 4. Data Persistence
- **File-based storage**: Uses `FileBasedDao` for reading/writing game data
- **JSON serialization**: Custom JSON utilities for data conversion
- **DataPoint collections**: Structured data for performance tracking
- **SharedPreferences**: Used for app settings and preferences

#### 5. Screen Orientation
- Most activities are **portrait-only** (`android:screenOrientation="portrait"`)
- Exception: Stroop game and some continue screens use **landscape**
- Check AndroidManifest.xml before modifying activity orientations

### Design Patterns Used

- **Factory Pattern**: For creating game trials and configurations
- **Strategy Pattern**: For different game mechanics and scoring
- **Optional Pattern**: Extensive use of Java 8 `Optional` for null safety
- **Immutable Objects**: Many data classes are designed as immutable
- **Static Imports**: Commonly used for utilities and constants

## Key Dependencies

- **MPAndroidChart** (v3.1.0): Chart/graph visualization for performance tracking
- **Apache Commons Collections** (4.1): Collection utilities
- **Apache Commons Lang** (3.0): String and utility helpers
- **Material Design Components**: UI components
- **JUnit + Mockito**: Unit testing
- **Espresso**: UI/instrumentation testing

## Testing

Tests are located in `app/src/test/java/` for unit tests and include:
- Utility class tests (DateUtil, NumberFormatter, IntentUtility)
- DAO and persistence tests (FileBasedDao, DataPoint)
- Chart and data conversion tests

When writing tests:
- Use JUnit 4 framework
- Mockito for mocking dependencies
- Place unit tests in `app/src/test/java/`
- Place instrumented tests in `app/src/androidTest/java/`

## Code Conventions

1. **MVP Contracts**: Always define View, Presenter, and Model contracts as interfaces
2. **Intent Helpers**: Create `*Intent` classes for activities that receive parameters
3. **Optional Usage**: Prefer `Optional<T>` over null checks for cleaner code
4. **File Organization**: Keep game-specific code within its own package namespace
5. **Activity Lifecycle**: Be mindful of `android:noHistory="true"` on countdown activities
6. **Resource Naming**: Follow Android conventions (`activity_*.xml`, `fragment_*.xml`)

## Common Development Scenarios

### Adding a New Game
1. Create new package under `com.<gamename>/`
2. Implement MVP pattern (Model, View, Presenter, Contracts)
3. Create activities: StartScreen, MainActivity, ContinueScreen
4. Add countdown activity or reuse `com.mainscreen.ui.countdown.CountdownActivity`
5. Register all activities in AndroidManifest.xml with appropriate orientations
6. Add game selection entry in `GameSelectionActivity`
7. Implement data persistence using FileBasedDao pattern
8. Add chart integration for performance tracking

### Modifying Game Logic
- Game logic resides in `<package>.game.*` classes
- Presenters handle business logic and game flow
- Keep UI code in Activities/Fragments separate from game logic
- Use factories for creating game configurations and trials

### Working with Data Persistence
- Use `FileBasedDao` for reading/writing game data
- Data is stored as JSON via `chart.filesystem.json` utilities
- `DataPoint` represents individual performance data points
- Access internal storage via Android's file system APIs
