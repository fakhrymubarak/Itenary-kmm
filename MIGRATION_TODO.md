# UI Migration Todo: Android to CommonMain

This document outlines the step-by-step migration plan to move UI components from `androidMain` to `commonMain` for true cross-platform UI sharing.

## 🎯 Goal
Move all UI components from `composeApp/src/androidMain/kotlin` to `composeApp/src/commonMain/kotlin` so they can be shared between Android and iOS.

## 📋 Migration Checklist

### Phase 1: Foundation Setup
- [ ] **1.1** Create commonMain directory structure
  - [ ] Create `composeApp/src/commonMain/kotlin/com/fakhry/itenary/` directory
  - [ ] Create subdirectories: `theme/`, `navigation/`, `features/`, `commons/`

- [ ] **1.2** Update build.gradle.kts dependencies
  - [ ] Move shared dependencies from `androidMain` to `commonMain`
  - [ ] Keep Android-specific dependencies in `androidMain`
  - [ ] Add iOS-compatible alternatives where needed

### Phase 2: Theme Migration
- [ ] **2.1** Move theme components
  - [ ] Move `theme/Color.kt` to `commonMain/theme/`
  - [ ] Move `theme/Shapes.kt` to `commonMain/theme/`
  - [ ] Move `theme/Typography.kt` to `commonMain/theme/`
  - [ ] Move `theme/Theme.kt` to `commonMain/theme/`

- [ ] **2.2** Update theme imports
  - [ ] Update all import statements in moved files
  - [ ] Test theme compilation in both platforms

### Phase 3: Common Components Migration
- [ ] **3.1** Move reusable UI components
  - [ ] Move `commons/ui/DotsIndicator.kt` to `commonMain/components/`
  - [ ] Move `commons/ui/ReorderableLazyColumn.kt` to `commonMain/components/`
  - [ ] Move `commons/ui/ShimmerComponents.kt` to `commonMain/components/`
  - [ ] Move `commons/ui/ShimmerUtils.kt` to `commonMain/components/`

- [ ] **3.2** Move model classes
  - [ ] Move `commons/model/ItineraryRequestUiModel.kt` to `commonMain/model/`

- [ ] **3.3** Update component dependencies
  - [ ] Replace Android-specific dependencies with multiplatform alternatives
  - [ ] Test component compilation

### Phase 4: Navigation Migration (Complex)
- [ ] **4.1** Research navigation solutions
  - [ ] Evaluate [Voyager](https://github.com/adrielcafe/voyager) for multiplatform navigation
  - [ ] Consider [Decompose](https://github.com/arkivanov/Decompose) for navigation
  - [ ] Or create custom navigation solution

- [ ] **4.2** Create navigation interfaces
  - [ ] Create `commonMain/navigation/NavigationManager.kt` interface
  - [ ] Define route classes in `commonMain/navigation/Routes.kt`
  - [ ] Create navigation state management

- [ ] **4.3** Implement platform-specific navigation
  - [ ] Create `androidMain/navigation/AndroidNavigationManager.kt`
  - [ ] Create `iosMain/navigation/IOSNavigationManager.kt`
  - [ ] Update dependency injection for navigation

### Phase 5: Feature Screens Migration
- [ ] **5.1** Move dashboard feature
  - [ ] Move `features/dashboard/ui/DashboardScreen.kt` to `commonMain/features/dashboard/`
  - [ ] Move `features/dashboard/ui/DashboardViewModel.kt` to `commonMain/features/dashboard/`
  - [ ] Move `features/dashboard/ui/ItenaryBottomNavigation.kt` to `commonMain/features/dashboard/`

- [ ] **5.2** Move itinerary feature
  - [ ] Move `features/dashboard/itinerary/ui/` to `commonMain/features/dashboard/itinerary/`
  - [ ] Move `features/dashboard/itinerary/model/` to `commonMain/features/dashboard/itinerary/`
  - [ ] Update navigation references

- [ ] **5.3** Move food feature
  - [ ] Move `features/dashboard/food/ui/` to `commonMain/features/dashboard/food/`
  - [ ] Move `features/dashboard/food/model/` to `commonMain/features/dashboard/food/`

- [ ] **5.4** Move history feature
  - [ ] Move `features/dashboard/history/ui/` to `commonMain/features/dashboard/history/`
  - [ ] Move `features/dashboard/history/model/` to `commonMain/features/dashboard/history/`

- [ ] **5.5** Move results feature
  - [ ] Move `features/itinerary/results/ui/` to `commonMain/features/itinerary/results/`
  - [ ] Move `features/itinerary/results/model/` to `commonMain/features/itinerary/results/`

### Phase 6: ViewModels Migration
- [ ] **6.1** Move shared ViewModels
  - [ ] Move `ItineraryViewModel.kt` to `commonMain/viewmodel/`
  - [ ] Update ViewModel dependencies
  - [ ] Ensure Koin configuration works for both platforms

- [ ] **6.2** Update ViewModel dependencies
  - [ ] Replace Android-specific lifecycle dependencies
  - [ ] Use multiplatform lifecycle libraries

### Phase 7: Main App Migration
- [ ] **7.1** Create shared App composable
  - [ ] Move `App.kt` logic to `commonMain/App.kt`
  - [ ] Create platform-specific entry points
  - [ ] Update `MainActivity.kt` to use shared App
  - [ ] Update `IOSMain.kt` to use shared App

- [ ] **7.2** Update navigation host
  - [ ] Move `navigation/AppNavHost.kt` to `commonMain/navigation/`
  - [ ] Update navigation implementation
  - [ ] Move `navigation/NavRoutes.kt` to `commonMain/navigation/`

### Phase 8: Dependency Updates
- [ ] **8.1** Replace Android-specific dependencies
  - [ ] Replace `androidx.navigation.compose` with multiplatform solution
  - [ ] Replace `coil.compose` with [Kamel](https://github.com/alialbaali/kamel) or similar
  - [ ] Replace `shimmer` with multiplatform alternative
  - [ ] Update `inspektor` usage for multiplatform

- [ ] **8.2** Add iOS-specific dependencies
  - [ ] Add iOS image loading solution
  - [ ] Add iOS-specific UI components if needed
  - [ ] Update Koin configuration for iOS

### Phase 9: Testing & Validation
- [ ] **9.1** Compilation testing
  - [ ] Test Android compilation after each phase
  - [ ] Test iOS compilation after each phase
  - [ ] Fix any compilation errors

- [ ] **9.2** Runtime testing
  - [ ] Test Android app functionality
  - [ ] Test iOS app functionality
  - [ ] Verify UI consistency between platforms

- [ ] **9.3** Performance testing
  - [ ] Test app startup time on both platforms
  - [ ] Test memory usage
  - [ ] Test UI responsiveness

### Phase 10: Cleanup & Optimization
- [ ] **10.1** Remove unused files
  - [ ] Delete moved files from `androidMain`
  - [ ] Clean up unused imports
  - [ ] Remove unused dependencies

- [ ] **10.2** Code optimization
  - [ ] Optimize shared code for both platforms
  - [ ] Add platform-specific optimizations where needed
  - [ ] Update documentation

## 🚨 Critical Dependencies to Replace

### Navigation
- **Current**: `androidx.navigation.compose` (Android-only)
- **Options**:
  - [Voyager](https://github.com/adrielcafe/voyager) - Multiplatform navigation
  - [Decompose](https://github.com/arkivanov/Decompose) - Component-based navigation
  - Custom navigation solution

### Image Loading
- **Current**: `coil.compose` (Android-only)
- **Options**:
  - [Kamel](https://github.com/alialbaali/kamel) - Multiplatform image loading
  - [Kotlinx Images](https://github.com/Kotlin/kotlinx-images) - Kotlin multiplatform images

### Shimmer Effects
- **Current**: `shimmer` (Android-only)
- **Options**:
  - Custom shimmer implementation
  - [Compose Shimmer](https://github.com/valentinilk/compose-shimmer) (check multiplatform support)

### Debug Tools
- **Current**: `inspektor` (Android-only)
- **Options**:
  - Platform-specific debug tools
  - Conditional compilation for debug features

## 📝 Migration Notes

### File Structure After Migration
```
composeApp/src/
├── commonMain/kotlin/com/fakhry/itenary/
│   ├── App.kt
│   ├── theme/
│   ├── navigation/
│   ├── features/
│   ├── components/
│   ├── model/
│   └── viewmodel/
├── androidMain/kotlin/com/fakhry/itenary/
│   ├── MainActivity.kt
│   └── navigation/ (Android-specific navigation)
└── iosMain/kotlin/com/fakhry/itenary/
    ├── IOSMain.kt
    ├── MainViewController.kt
    └── navigation/ (iOS-specific navigation)
```

### Testing Strategy
1. **Incremental Testing**: Test after each phase
2. **Platform Testing**: Test both Android and iOS after each major change
3. **Feature Testing**: Test each feature individually
4. **Integration Testing**: Test the complete app flow

### Rollback Plan
- Keep original files in `androidMain` until migration is complete
- Use Git branches for each migration phase
- Test thoroughly before removing original files

## 🎯 Success Criteria
- [ ] All UI components compile for both Android and iOS
- [ ] App runs successfully on both platforms
- [ ] UI looks and behaves consistently across platforms
- [ ] No Android-specific dependencies in commonMain
- [ ] Performance is acceptable on both platforms

## 📚 Resources
- [Compose Multiplatform Documentation](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Voyager Navigation](https://github.com/adrielcafe/voyager)
- [Decompose Navigation](https://github.com/arkivanov/Decompose)
- [Kamel Image Loading](https://github.com/alialbaali/kamel)
- [Kotlin Multiplatform Mobile](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)

