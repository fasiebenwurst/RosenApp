# RosenApp 🌹

An offline Android app for cataloguing the roses (and other plants) in your
garden: snap a photo, record the variety and care details, then generate a
**printable label** to place in the bed.

## Features

- 📷 **Photograph plants** with the camera or pick an existing image from the gallery.
- 🌿 **Rose catalog autocomplete**: pick from a bundled list of well-known roses (or type your own); the **Latin/botanical name** pre-fills automatically.
- 🏷️ **Capture details** per plant: name/variety, Latin name, garden location, planting date, and care notes.
- 🖨️ **Generate printable labels** (90 × 60 mm) that combine the photo, details, and a
  **QR code** encoding the plant info.
- 📄 **Export as PDF or PNG**, share via the Android share sheet, or **print directly**
  through Android's print framework.
- 💾 **Backup & restore** the whole garden to a single ZIP archive (plant data **and** photos),
  via the system file picker — easy to move between devices. Import offers **Merge** (add to
  the current garden) or **Replace all**.
- 🔒 **Fully offline** — everything is stored locally on the device (Room database +
  internal photo storage). No accounts, no servers.

## Tech stack

| Concern        | Choice                                            |
|----------------|---------------------------------------------------|
| Language        | Kotlin                                           |
| UI              | Jetpack Compose + Material 3                      |
| Architecture    | MVVM (ViewModel + StateFlow), single-Activity    |
| Navigation      | Navigation-Compose                               |
| Persistence     | Room (offline SQLite)                             |
| Images          | Coil (loading) + Android Canvas (label rendering)|
| QR codes        | ZXing core                                        |
| Printing        | `android.graphics.pdf.PdfDocument` + AndroidX `PrintHelper` |
| Min / target SDK| 26 / 35                                           |

## Project layout

```
app/src/main/java/de/empirius/rosenapp/
├── RosenApplication.kt        # app-wide singletons (no DI framework)
├── MainActivity.kt
├── data/                      # Room entity, DAO, database, repository
├── photo/                     # PhotoStorage – capture/import/delete photos on disk
├── label/                     # LabelRenderer, LabelExporter (PDF/PNG), QrCodeGenerator
└── ui/
    ├── theme/                 # Compose Material 3 theme (rose palette)
    ├── RosenNavHost.kt        # navigation graph
    ├── plants/                # plant list
    ├── edit/                  # add / edit form
    ├── detail/                # plant detail
    └── label/                 # label preview + export/print
```

## Building

Requires the **Android SDK** (API 35) and JDK 17+.

```bash
# from the repo root
./gradlew assembleDebug          # build the debug APK
./gradlew installDebug           # build & install on a connected device/emulator
```

Open the folder in **Android Studio** (Ladybug or newer) and let it sync — the
Gradle wrapper (8.14.3), version catalog, and module config are all checked in.

> The first command-line build needs network access to download AGP, Compose,
> Room, ZXing, etc. from `google()` / `mavenCentral()`. In Android Studio this
> happens automatically on Gradle sync.

## How the label is built

`LabelRenderer` draws the whole label onto a `Bitmap` at 300 dpi — photo
(center-cropped, rounded), title, location, planting date, care notes, and a QR
code. The **same bitmap** feeds the on-screen preview, the PNG export, and the
PDF page (scaled to the physical 90 × 60 mm point size), so what you preview is
exactly what prints.
