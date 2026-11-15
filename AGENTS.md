# AGENTS.md

## What this is
unfold-mpv: personal, public fork of mpvKt (Android mpv front-end, Kotlin/Compose). Apache 2.0.
GitHub: `mogeldev/unfold-mpv`. Single `main` branch, history was squashed to one commit on purpose; there is no upstream remote.

## Naming (deliberately inconsistent)
- Display name `unfold-mpv` (`app_name` in all `res/values*/strings.xml`), applicationId `io.github.mogeldev.unfoldmpv` (no hyphen allowed).
- Kotlin packages and `namespace` stay `live.mehiz.mpvkt`: renaming them touches hundreds of files, not worth it.
- The Lua table `mpvkt` (`assets/mpvkt.lua`, `docs/CUSTOMBUTTONS.rst`) is a user-facing scripting API: do not rename.
- `RESULT_INTENT` in `PlayerActivity` is an external-player protocol constant: leave it.
- `privacy_policy_url` in the strings still points at the original author's page (no own page exists).

## Dependencies
- mpv comes from `io.github.abdallahmehiz:mpv-android-lib` (version in `gradle/libs.versions.toml`), built from a fork of mpv-android/mpv-android. Latest published version is the newest available; a newer mpv/FFmpeg needs a custom build of that library.

## Embedding in Unfold (`player/`)
- `player/` builds the same sources as `app/` as an Android library; unfold-jellyfin-player includes it as the git submodule `android/mpvkt` and starts `PlayerActivity` from its own WebView app.
- Plugins and dependencies are duplicated in `player/build.gradle.kts`: change both when `app/build.gradle.kts` changes. Unfold takes all versions from `gradle/libs.versions.toml`.
- Intent extras Unfold relies on, besides the mpv-android ones: `media_id` (key for the saved playback state) and `vid`/`aid`/`sid`/`position`, which win over the saved state. `MediaPlaybackService.currentPositionMs()` is read for progress reports.

## Build and checks
- `./gradlew detekt assembleRelease` (JDK 21). Release APKs are unsigned; `preview` build type is debug-signed.
- The Android SDK on the dev machine is at `D:\Android\Sdk`, but the `ANDROID_HOME` environment variable points to a missing folder: set `ANDROID_HOME=D:\Android\Sdk` for the build (or put `sdk.dir` in `local.properties`). An emulator (`scda_x64`) is available for testing. There is no CI: GitHub Actions workflows were removed on purpose.

## Git and README
- Commits use the repo-local identity `mogeldev` with the GitHub noreply address; keep it that way (public repo, no real email).
- README contains no external links on purpose; do not re-add badges or store links.
- Checked-out files use CRLF on Windows; the LF-to-CRLF warnings from git are harmless.
