# UI Migration Todo: Android to CommonMain

## ✅ Progress So Far
- [x] Added Voyager dependencies and base navigator in `commonMain`
- [x] Shared dashboard entry screen through Voyager `Tab`
- [x] Moved itinerary streaming models and card/day composables to `commonMain`
- [x] Removed Android-only timeline UI and legacy navigation scaffolding (`NavHost`, `NavRoutes`, etc.)
- [x] Deleted Android NavHost feature graphs (food, history, itinerary)
- [x] Added Voyager `screenmodel` + `koin` dependencies to shared module

## 🔜 Next Steps to Reach Full KMP

### Phase A: Shared State & ScreenModels
- [x] Convert `ItineraryViewModel` logic into a Voyager `ScreenModel` in `commonMain`
- [x] Expose streaming itinerary state with immutable models and shared coroutine scope
- [x] Provide ScreenModel bindings via Koin for both platforms

### Phase B: Feature UI Migration
- [x] Re-implement `ResultStreamItineraryScreen` as a shared composable using Voyager navigation
- [x] Port itinerary request screen (carousel, days, form) to `commonMain`
- [x] Migrate dashboard home surface and sections (food/history/itinerary tabs) to shared code
- [x] Move supporting components (bottom navigation, cards, loaders) to shared modules

### Phase C: Navigation Overhaul
- [ ] Replace remaining Android `NavController` usages (food/history graphs) with Voyager screens/tabs
- [ ] Introduce Voyager destinations for food/history/results flows in `commonMain`
- [ ] Update Android and iOS entry points to bootstrap Voyager navigator + ScreenModel DI

### Phase D: Device-Specific Glue
- [ ] Wire Koin modules for Android/iOS to use shared ScreenModels
- [ ] Ensure platform preview/inspektor tooling guarded behind expect/actual or platform checks
- [ ] Audit image loading + shimmer dependencies and swap for multiplatform options (e.g., Kamel, custom shimmer)

### Phase E: Validation & Cleanup
- [ ] Run `./gradlew composeApp:compileDebugKotlinAndroid`
- [ ] Run iOS build (`composeApp:compileKotlinIosArm64` or Xcode target)
- [ ] Remove obsolete Android-only files after shared implementations verified
- [ ] Document new module structure and update README if needed
