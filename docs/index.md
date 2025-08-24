---
title: Android Persian (Jalali) DatePicker
description: Jetpack Compose Persian Date Picker (Dialog/BottomSheet, Single/Multiple/Range) with full styling.
---

# Android Persian (Jalali) DatePicker for Jetpack Compose

Clean, configurable Persian (Jalali/Iranian) Date Picker for Android with Jetpack Compose.

## Install (JitPack)
```kotlin
repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
}
dependencies {
    implementation("com.github.Miaadrajabi:persianDatePicker:v1.0.0")
}
```

## Quick Usage
```kotlin
PersianDatePicker(
  isVisible = show,
  config = PersianDatePickerBuilder()
      .selectionMode(SelectionMode.RANGE)
      .presentation(PresentationStyle.DIALOG)
      .gravity(DialogGravity.CENTER)
      .style(PersianDatePickerStyle())
      .build(),
  onDismiss = { show = false },
  onResult = { /* SelectionResult */ }
)
```

See the full README for features, styling, and screenshots.

