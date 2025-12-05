# Q Tracker (Android) - Minimal Java/XML app

This is a minimal Android app (Java + XML) that stores numeric data for named parameters and provides simple statistics:

- Add data: choose a parameter name and a numeric value
- Stats: shows average and average deviation (mean absolute deviation) over the last N values (N is configurable)
- Result: shows per-parameter score = (last value - avg) / avedev and overall score = (sum(scores)/count) * 100
- Data is stored in SharedPreferences as JSON (persistent across launches)

How to run locally:
1. Ensure you have Android Studio or the Android SDK + Gradle.
2. Import the project directory.
3. Build & run on device or emulator (minSdk 21).
4. Optionally create branch `android/init` and open a PR.

Notes:
- Storage is simplified (SharedPreferences + JSON). For production, migrate to Room/DB for large datasets / concurrency.
- Average deviation uses mean absolute deviation.
