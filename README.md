# Memory Journal App

## Application Overview
The Memory Journal App is an Android mobile application built with Java in Android Studio that leverages Firebase Authentication, Cloud Firestore, and Firebase Storage to help people affected by Alzheimer's disease or early-stage dementia keep track of personal memories, daily events, and important information. Patients, family members, and caregivers can collaborate in real time by connecting to a shared Care Profile, with all data synchronized across authorized devices through the cloud backend.

## Key Functionalities
### 1. Account and Profile Handling
- Secure authentication using Firebase Authentication with email/password registration, login, logout, and automatic session handling via FirebaseAuth state listeners.
- After signing in, users can create a new Care Profile or join an existing profile using a profile code/invitation.
- Role-based access control determines UI visibility and permissions:
  - **Family Member**: add new journal entries.
  - **Primary Caregiver**: edit/delete entries and manage profile members.
  - **Patient**: simplified UI mode for accessibility.

### 2. Care Profile Management
- Create and edit Care Profiles (patient name, photo, age, cognitive condition notes).
- View the list of users linked to the profile.
- Add/remove users (caregiver permissions).
- Profile overview dashboard with memory statistics.
- Accessible UI with large fonts, clear navigation, and guidance icons.

### 3. Memory Entry Management (CRUD)
Each `JournalRecord` contains:
- Title and descriptive text note
- Event date (when the memory happened)
- Timestamp (when entry created)
- Categories/tags (Family, Holiday, Routine, Medical, etc.)
- Media attachments (photos from camera/gallery, audio recordings stored in Firebase Storage)

Users can:
- **Create entries**: fill text fields, select event date, add tags, upload photos, record audio notes.
- **View entries**: sort by date, filter by tag, search by keyword, browse via chronological timeline.
- **View entry details**: full-screen view with image zoom, audio playback, Android Text-to-Speech button that reads text aloud.
- **Update/Delete entries**: authorized users can modify text/tags/media or permanently delete entries (with confirmation).

### 4. Search API
- Local Firestore-based search for keywords contained in titles, tags, or note text.

### 5. Accessibility & UX
- Large buttons with high-contrast themes.
- Optional simplified mode for patients.
- Text-to-Speech support.
- Voice-to-text input for creating entries.

## Data Model
### Class: Account
| Attribute | Type | Description |
|-----------|------|-------------|
| accountId | String | Firebase Auth UID |
| email | String | User email |
| displayLabel | String | Name shown in UI |
| accessLevel | String | "Caregiver", "FamilyMember", "Patient" |
| linkedCareProfile | String | Profile ID the user belongs to |
| createdAt | Timestamp | Account creation date |

### Class: CareProfile
| Attribute | Type | Description |
|-----------|------|-------------|
| profileId | String | Firestore document ID |
| fullName | String | Patient full name |
| profilePhotoUrl | String | Firebase Storage URL |
| memberAccountIds | List<String> | Linked account IDs |
| createdAt | Timestamp | When the profile was created |

### Class: JournalRecord
| Attribute | Type | Description |
|-----------|------|-------------|
| recordId | String | Firestore document ID |
| profileId | String | Links entry to CareProfile |
| createdBy | String | accountId of author |
| heading | String | Title |
| noteBody | String | Journal text |
| eventDate | Timestamp | Date the event occurred |
| insertedAt | Timestamp | Timestamp of creation |
| categories | List<String> | Tags |
| mediaList | List<Map> | e.g., `{type: "image", url: "..."}` or `{type: "audio", url: "..."}` |

### Relationships
- **CareProfile ↔ Accounts (One-to-Many)**: A patient profile shared among multiple family members or caregivers.
- **CareProfile ↔ JournalRecord (One-to-Many)**: All memory entries belong to a specific Care Profile.
- **Account ↔ JournalRecord (One-to-Many)**: A user can contribute multiple entries to the journal.

## Technology Stack
- Android (Java, Android Studio)
- Firebase Authentication, Cloud Firestore, Firebase Storage
- Android Text-to-Speech (TTS), voice-to-text input APIs
