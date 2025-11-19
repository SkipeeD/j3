# Memory Journal Android App

The Memory Journal app is a Firebase-backed Android application that helps patients impacted by Alzheimer's disease or early-stage dementia preserve memories and daily events. Family members and caregivers collaborate inside a shared Care Profile where they can add journal entries, review important routines, and provide additional context through rich media.

## Project structure
```
MemoryJournal/
├── app/
│   ├── src/main/java/com/memoryjournal/app/   # Activities, repositories, data models, utilities
│   ├── src/main/res/                          # Layouts, themes, string resources
│   ├── build.gradle                           # Module-level Gradle configuration
├── build.gradle                               # Top-level Gradle configuration
└── settings.gradle                            # Settings + repository configuration
```

## Features implemented
- **Authentication** – Email/password login and registration powered by Firebase Authentication with role selection (Caregiver, FamilyMember, Patient).
- **Care Profile hub** – Displays patient notes, statistics, and a RecyclerView timeline of journal entries. Automatically listens for profile updates from Cloud Firestore.
- **Journal management** – Add entries with titles, descriptions, tags, event dates, photo uploads, and optional audio recordings. Entries stream live updates for connected devices.
- **Media capture helpers** – Use the Android photo picker plus a lightweight `AudioRecorder` utility that stores audio snippets in Firebase Storage and attaches the resulting download URLs to entries.
- **Search-ready repositories** – `JournalRepository` exposes Firestore queries for keyword searches, while `CareProfileRepository` supplies profile listeners for multi-user synchronization.
- **Accessible UI** – All screens favor large tap targets, descriptive copy, and simple navigation tailored to elderly users.

## Requirements
- Android Studio Ladybug (or newer) with the Android SDK 34 platform and build tools installed.
- Java 17 configured for Gradle builds.
- Local Gradle 8.6+ installation (Android Studio bundles one) because the binary Gradle wrapper is intentionally omitted.
- A Firebase project with Authentication, Cloud Firestore, and Storage enabled.

## Getting started
1. **Clone** this repository and open it with Android Studio *(File → Open… → select the project root)*.
2. **Add Firebase configuration:** download your `google-services.json` from the Firebase Console and place it in `app/`.
3. **Sync Gradle:** Android Studio will automatically download dependencies using its bundled Gradle distribution. If you prefer the CLI, install Gradle 8.6+ or regenerate the wrapper locally (`gradle wrapper`).
4. **Configure Firestore Security Rules** that align with your Care Profile sharing model before running a release build.
5. **Run the app:** connect a device/emulator and click **Run**. Login or create an account, then follow the prompts to create a Care Profile and add entries.

### Command-line build
Because the repository excludes binary artifacts, the Gradle wrapper is not committed. Use a locally installed Gradle distribution (8.6+ recommended) and run:
```
gradle assembleDebug
```
The output APK will be located under `app/build/outputs/apk/debug/`.

## Firebase data model
| Entity | Key fields | Notes |
|--------|------------|-------|
| `Account` | `accountId`, `email`, `displayLabel`, `accessLevel`, `linkedCareProfile`, `createdAt` | Stored in the `accounts` collection and mirrors FirebaseAuth users. |
| `CareProfile` | `profileId`, `fullName`, `profilePhotoUrl`, `memberAccountIds`, `notes`, `createdAt` | Parent document that aggregates members and journal entries. |
| `JournalRecord` | `recordId`, `profileId`, `createdBy`, `heading`, `noteBody`, `eventDate`, `insertedAt`, `categories`, `mediaList` | Nested collection inside each Care Profile for timeline entries. |

## Accessibility considerations
- Large font defaults, ample spacing, and Material components maintain clarity for seniors.
- Audio recording + playback make it easy to capture oral memories.
- Buttons and navigation text are plain-language and high-contrast, respecting WCAG recommendations.

## Next steps
- Implement invitation codes for joining existing Care Profiles.
- Add role-aware UI restrictions (e.g., hide destructive actions for read-only roles).
- Integrate Android Text-to-Speech for narrated entries and voice-to-text for dictation.
