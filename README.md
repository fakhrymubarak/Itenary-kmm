This is a Kotlin Multiplatform project targeting Android, iOS.

# TODO :

## Development & Infrastructure

- Setup CICD and pass the build to firebase apptester

## Performance Optimization Learning & Implementation

### 1. Cold Start Optimization

- [ ] Learn about baseline profiles and how they work
- [ ] Implement baseline profiles for the app
- [ ] Study lazy DI initialization patterns
- [ ] Implement lazy DI init in Koin modules
- [ ] Learn startup tracing with Macrobenchmark
- [ ] Set up Perfetto for performance analysis
- [ ] Measure and document cold start improvements

### 2. UI Performance & Jank Reduction

- [ ] Study list rendering optimization techniques
- [ ] Learn about stable item keys in LazyColumn/RecyclerView
- [ ] Understand layout thrash and how to avoid it
- [ ] Implement derivedStateOf for state management
- [ ] Use snapshotFlow for reactive updates
- [ ] Set up Frame Rendering stats monitoring
- [ ] Learn Choreographer data analysis
- [ ] Target <1% jank on low-end devices

### 3. Memory & Rendering Optimization

- [ ] Study RecyclerView/LazyColumn paging prefetch
- [ ] Implement view recycling audits
- [ ] Learn image caching strategies
- [ ] Implement bitmap pool management
- [ ] Set up memory budget controls
- [ ] Use Android Studio Profiler for heap analysis
- [ ] Integrate LeakCanary for memory leak detection
- [ ] Achieve 0 leaks in CI pipeline

### 4. Build Performance & App Size

- [ ] Learn Gradle configuration cache setup
- [ ] Implement build cache optimization
- [ ] Set up per-module test targets
- [ ] Study R8 rules for code shrinking
- [ ] Implement trace builds in CI
- [ ] Set up regression alerts for build performance
- [ ] Document build time and app size improvements

### 5. Performance Monitoring & Analytics

- [ ] Learn Firebase Performance Monitoring
- [ ] Implement custom performance traces
- [ ] Set up TTI (Time to Interactive) metrics
- [ ] Monitor network latency metrics
- [ ] Learn A/B testing for performance validation
- [ ] Understand p-value thresholds for statistical significance
- [ ] Set up performance regression alerts

### 6. Architecture & State Management

- [ ] Study MVI (Model-View-Intent) pattern
- [ ] Learn unidirectional data flow principles
- [ ] Identify suitable screens for MVI migration
- [ ] Implement MVI architecture for key screens
- [ ] Understand how MVI improves performance predictability
- [ ] Set up performance regression detection with MVI

## Learning Resources

- [ ] Android Performance documentation
- [ ] Kotlin Multiplatform performance best practices
- [ ] Compose performance optimization guides
- [ ] Firebase Performance Monitoring docs
- [ ] Macrobenchmark library documentation
- [ ] Perfetto trace analysis tutorials

# List Known Bug :

1. ✅Banner cannot be swipe
    - Actual : Banner cannot be swipe
    - Expected : Banner possible to be swipe
2. ✅Places Input
    - Actual : First typing is double char and the arrangement is messy
    - Expected : Should like normal input
3. ✅Request Screen Scroll Still Buggy (Scaffold someh including padding).
4. ✅Button Add More Day Too Big
5. ✅When day more than 1, the re-composition recomposing entire section.
6. When removed day, it recompose entire sections
7. Stream Itinerary not animated.
8. ✅Add More day not animated.
9. Dirty Code
    - A Lot Of Hardcoded Strings
    - Messy theming
