# Moon Phase for Android

A small native Android home screen widget. Works offline, needs no permissions,
and displays a rendered moon, phase name, and illuminated percentage.

## Build from your phone with GitHub

No Android Studio is needed for this route. The included GitHub Actions workflow
builds a debug APK on GitHub's servers.

1. Extract the project ZIP on your phone. Create a GitHub repository, then
   upload **the contents of the `moon-widget` folder** to its root. You should
   see `settings.gradle`, `app`, and `.github` directly in the repository.
   GitHub does not extract uploaded ZIP files. Code Studio can edit these files;
   use a Git client or GitHub's browser uploader to commit them.
2. Make sure `.github/workflows/build-apk.yml` is uploaded. Some Android file
   managers hide folders beginning with a dot; enable **Show hidden files**.
   If needed, use GitHub's **Add file → Create new file** to create that exact
   path and paste the workflow contents. Switch your browser to **Desktop site**
   if GitHub's upload controls are missing.
3. Open the repository's **Actions** tab and enable workflows if prompted.
   Select **Build Android APK → Run workflow → Run workflow**. A push to
   `main` or `master` also starts a build automatically.
4. Wait for a green check, then open the completed run. Under **Artifacts**,
   download **moon-phase-debug-apk** while signed in to GitHub.
5. Extract the downloaded ZIP and open `app-debug.apk`. If Android asks, allow
   your browser or file manager to **Install unknown apps**, then install it.
6. Open **Moon Phase → Add widget**, or long-press your home screen and choose
   **Widgets → Moon Phase**.

The artifact is retained for 30 days; rerun the workflow if it expires. This is
a debug build for personal testing. GitHub runners generate their own debug
signing keys, so a later APK may require uninstalling the previous version first.
Release distribution would need a persistent signing key.

## Build with Android Studio (optional)

1. Open this folder in Android Studio. Use JDK 17 or newer, Android SDK 35,
   and Gradle 8.11.1 when prompted for a local Gradle distribution (a wrapper is
   not included).
2. Sync the project, then run the `app` configuration on an Android 8.0+ device.
3. Open Moon Phase and tap **Add widget**, or long-press the home screen and
   select **Widgets → Moon Phase**.

With Gradle 8.11.1 installed, `gradle :app:assembleDebug` builds the APK at
`app/build/outputs/apk/debug/app-debug.apk`.

## Behavior and accuracy

The widget requests an update every 30 minutes; Android may defer updates to
save battery. Tapping it refreshes immediately. Clock/timezone changes also
refresh installed widgets. No internet, location, service, or exact alarm is used.

Phase is estimated using a 29.530588853-day mean synodic month from the
January 6, 2000 new moon. This is suitable for a general home screen display;
actual phase timings can differ by several hours. Rendering is north-up
(waxing light on the right), rather than local sky orientation. Phase names
use the nearest eighth of the lunar cycle.

## Validation

The source XML and lunar model were checked in the development workspace.
The workspace has no Android SDK, Gradle, or Java compiler; an APK build and
device/launcher verification remain necessary. The GitHub workflow runs both
the debug build and Android lint; its first successful run confirms those checks.
