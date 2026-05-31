# Travel Companion App

A simple Android app that converts currency, fuel/distance units, and temperature. Built for SIT305 Pass Task 2.1 at Deakin University.

## What it does

Pick a category, pick your from and to units, type a number, hit Convert. Done.

Three categories:

- **Currency** — USD, AUD, EUR, JPY, GBP (uses fixed 2026 rates from the task sheet)
- **Fuel & Distance** — mpg ↔ km/L, gallons ↔ litres, nautical miles ↔ km
- **Temperature** — Celsius, Fahrenheit, Kelvin

## How it works

- **Currency** and **Temperature** use a two-step approach — convert input to a base unit (USD or Celsius), then convert the base to the target. Means fewer conversion factors to manage.
- **Fuel** does direct pair-to-pair conversions and rejects cross-type attempts (e.g. mpg → litres) with a Toast.

## Validation

- Empty input → Toast
- Non-numeric input → Toast
- Same unit on both sides → returns the same value with a heads-up
- Negative values in the fuel category → rejected

## Tech

- Java
- Android Studio
- Min SDK: whatever Android Studio defaulted to

## Run it

1. Clone the repo
2. Open in Android Studio
3. Let Gradle sync
4. Run on an emulator or device

## Files

- `MainActivity.java` — all the conversion logic and UI handling
- `activity_main.xml` — the layout
- Standard Android project structure for everything else

## Notes

LLM was used to translate my pseudocode into Java syntax — full declaration is in the submission document.
