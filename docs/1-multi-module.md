# Refactor to Multi Module

This document will create multi module project in order to make this apps scalable.

## New tree structure
```
.
├─ settings.gradle.kts
├─ build.gradle.kts
├─ build-logic/                          # optional: convention plugins to keep builds DRY
│  ├─ build.gradle.kts
│  └─ src/main/kotlin/
│     ├─ KotlinMultiplatformConventionPlugin.kt
│     ├─ AndroidAppConventionPlugin.kt
│     └─ AndroidLibraryConventionPlugin.kt
│├─ gradle/
│  └─ libs.versions.toml
│
├─ apps/
│  └─ androidApp/                     # (optional) if you want a thin Android wrapper; can keep current too
│
├─ composeApp/                        # your current KMP app shell (App, NavHost, DI)
│  └─ src/{commonMain,androidMain,...}
│
├─ core/
│  ├─ model/                          # pure models & UiState
│  │  └─ src/{commonMain,commonTest}/kotlin/...
│  └─ data/                           # repositories, Room/SQL/Network glue (KMP where possible)
│     └─ src/{commonMain,androidMain,iosMain}/kotlin/...
│
├─ common-ui/
│  ├─ design/                         # theme, colors, typography, spacing
│  │  └─ src/commonMain/kotlin/...
│  └─ components/                     # reusable composables: shimmer, dots, lists
│     └─ src/commonMain/kotlin/...
│
├─ features/
│  ├─ dashboard/                      # bottom bar & nested graphs (food, location, history entry)
│  │  └─ src/{commonMain,androidMain,iosMain}/kotlin/...
│  ├─ food/                           # from dashboard/food
│  │  └─ src/{commonMain,androidMain,iosMain}/kotlin/...
│  ├─ history/                        # results history + detail
│  │  └─ src/{commonMain,androidMain,iosMain}/kotlin/...
│  ├─ itinerary-request/              # the “Request/Input” flow
│  │  └─ src/{commonMain,androidMain,iosMain}/kotlin/...
│  └─ itinerary-results/              # the “Results” flow
│     └─ src/{commonMain,androidMain,iosMain}/kotlin/...
│
└─ testing/
   └─ test-utils/                     # shared test helpers, fake repos, coroutine rules

```

## Setting Gradle
```kts
rootProject.name = "itenary"

include(
  ":composeApp",

  // core
  ":core:model",
  ":core:data",

  // common ui
  ":common-ui:design",
  ":common-ui:components",

  // features
  ":features:dashboard",
  ":features:food",
  ":features:history",
  ":features:itinerary-request",
  ":features:itinerary-results",

  // testing
  ":testing:test-utils"
)
```
