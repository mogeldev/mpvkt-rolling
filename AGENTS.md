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

## Build and checks
- `./gradlew detekt assembleRelease` (JDK 21). Release APKs are unsigned; `preview` build type is debug-signed.
- No Android SDK on the dev machine (`ANDROID_HOME` points to a missing folder), so local builds do not work. There is no CI either: GitHub Actions workflows were removed on purpose. Unverified changes stay unverified unless a workflow is re-added.

## Git and README
- Commits use the repo-local identity `mogeldev` with the GitHub noreply address; keep it that way (public repo, no real email).
- README contains no external links on purpose; do not re-add badges or store links.
- Checked-out files use CRLF on Windows; the LF-to-CRLF warnings from git are harmless.
