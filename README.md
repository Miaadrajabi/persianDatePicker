# Persian Date Picker (Compose)

A clean, highly configurable Persian (Jalali) Date Picker for Android built with Jetpack Compose.

## Highlights
- Single / Multiple / Range selection modes
- Dialog or BottomSheet presentation
- Full styling via `PersianDatePickerStyle` (colors, sizes, title/nav colors, day colors, footer, …)
- Builder-based configuration (width/height/fraction, margins, paddings, gravity)
- Today button (text, colors, behavior, visibility)
- Year Picker with configurable grid/appearance

## Gradle (JitPack)
Add JitPack repository (Kotlin DSL):
```kotlin
repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}
```
Add dependency (Groovy DSL):
```groovy
dependencies {
    implementation 'com.github.Miaadrajabi:persianDatePicker:v1.0.0'
}
```

## Screens (themes and layouts)

<table>
  <tr>
    <td align="center">
      <img src="./rangepicker_light.png" alt="Range Picker - Light" width="360" />
      <br/>Range (Dialog) - Light
    </td>
    <td align="center">
      <img src="./rangepicker_dialog_dark.png" alt="Range Picker - Dark" width="360" />
      <br/>Range (Dialog) - Dark
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="./multi_select_light.png" alt="Multi Select - Light" width="360" />
      <br/>Multiple (BottomSheet) - Light
    </td>
    <td align="center">
      <img src="./mutlti_picker_dark.png" alt="Multi Select - Dark" width="360" />
      <br/>Multiple (BottomSheet) - Dark
    </td>
  </tr>
  <!-- Add more shots if needed -->
  </table>

## Quick Usage
```kotlin
val config = PersianDatePickerBuilder()
  .selectionMode(SelectionMode.RANGE)               // SINGLE, MULTIPLE, RANGE
  .presentation(PresentationStyle.DIALOG)           // DIALOG, BOTTOM_SHEET
  .gravity(DialogGravity.CENTER)                    // TOP, CENTER, BOTTOM (Dialog)
  .sizeFraction(0.95f, null)                        // or .size(width, height)
  .dialogMargin(16.dp, 16.dp)
  .contentPadding(24.dp, 24.dp)
  .style(
    PersianDatePickerStyle(
      containerBackgroundColor = Color(0xFFF5F5F5),
      cardContainerColor = Color.White,
      // title & nav colors
      monthTitleTextColor = Color(0xFF333333),
      yearCenterTextColor = Color(0xFF666666),
      // day text colors
      dayDefaultTextColor = Color(0xFF333333),
      dayTodayTextColor = Color(0xFF2196F3),
    )
  )
  .showTodayButton(true)
  .todayButtonText("امروز")
  .onTodayClick { /* handle today */ }
  .singleTitle("انتخاب تاریخ")
  .multipleTitle("انتخاب چند تاریخ")
  .rangeTitle("انتخاب بازه تاریخ")
  .enableYearPicker(true)
  .showSelectionSummaryFooter(true)
  // more: dialog/bottomSheet title colors, nav icon colors, scrim, footer styles, day sizes, etc.
  .build()

PersianDatePicker(
  isVisible = showDatePicker,
  config = config,
  onDismiss = { showDatePicker = false },
  onResult = { result ->
    when (result) {
      is SelectionResult.Single -> { /* result.date */ }
      is SelectionResult.Multiple -> { /* result.dates */ }
      is SelectionResult.Range -> { /* result.range */ }
    }
  }
)
```

## Leap Year Support (سال کبیسه)
- Library handles Jalali leap years internally for month/day calculations.
- You can also query leap years directly:
```kotlin
val isLeap = com.miaadrajabi.persiandatepicker.utils.PersianDate.isLeapYear(1404)
```

## License
MIT
