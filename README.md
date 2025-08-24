# Persian Date Picker (Compose)

An easy-to-use, highly configurable Persian (Jalali) Date Picker for Android built with Jetpack Compose.

## Features
- Dialog or BottomSheet presentation
- Selection modes: Single, Multiple, Range
- Fully styleable via `PersianDatePickerStyle` (colors, sizes, fonts)
- Builder-based API for configuration (gravity, margins, paddings, sizes)
- Today button with custom action and visibility
- Year picker with customizable grid/appearance
- Configurable selection summary footer

## Gradle (JitPack)
1. Add JitPack in your root `settings.gradle` repositories if needed:
```kotlin
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }
}
```
2. Add dependency:
```kotlin
dependencies {
  implementation("com.github.<YOUR_GITHUB_USERNAME>:persianDatePicker:<version>")
}
```

## Usage
```kotlin
val config = PersianDatePickerBuilder()
  .selectionMode(SelectionMode.RANGE)
  .presentation(PresentationStyle.DIALOG)
  .gravity(DialogGravity.CENTER)
  .sizeFraction(0.95f, null)
  .dialogMargin(16.dp, 16.dp)
  .contentPadding(24.dp, 24.dp)
  .style(
    PersianDatePickerStyle(
      containerBackgroundColor = Color(0xFFF5F5F5),
      cardContainerColor = Color.White,
      // ... other style overrides
    )
  )
  .showTodayButton(true)
  .todayButtonText("امروز")
  .onTodayClick { /* handle */ }
  .singleTitle("انتخاب تاریخ")
  .multipleTitle("انتخاب چند تاریخ")
  .rangeTitle("انتخاب بازه تاریخ")
  .enableYearPicker(true)
  .showSelectionSummaryFooter(true)
  .build()

PersianDatePicker(
  isVisible = showDatePicker,
  config = config,
  onDismiss = { showDatePicker = false },
  onResult = { result -> /* handle result */ }
)
```

## License
Apache-2.0
