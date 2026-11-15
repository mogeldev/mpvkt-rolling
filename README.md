# <img alt="app icon" src=".github/assets/app_icon.svg" width="48" /> unfold-mpv

An Android video player built on mpv (libmpv), with a touch interface in Kotlin and Jetpack Compose. It is a personal fork of mpvKt by abdallahmehiz, maintained as the player for **unfold-jellyfin-player**: there it is built into the app, but it also works as a standalone player.

Public and free to use, but without guarantees or support. Licensed under Apache 2.0.

## Features

- **Player:** gestures for seeking, volume and brightness (configurable per screen area), double-tap seeking, hold for faster playback, speed presets, chapters, aspect ratio, sleep timer, picture-in-picture and background playback with a media notification.
- **Tracks:** audio and subtitle tracks, a secondary subtitle, delays per track, preferred languages, external subtitles.
- **Subtitles:** font (including your own font folder), size, colours, border, shadow, background and position; can override the styling of ASS subtitles.
- **Picture:** hardware decoding, gpu-next, debanding, and brightness, contrast, saturation, gamma and hue filters.
- **For mpv users:** your own `mpv.conf` and `input.conf`, Lua scripts and custom buttons in the player (see [docs/CUSTOMBUTTONS.rst](docs/CUSTOMBUTTONS.rst)), a statistics page.
- **Remembers per file:** position, tracks, delays and speed.

## Starting it from another app

The player opens videos through an `ACTION_VIEW` intent (`content://`, `file://`, `http(s)://` and the usual streaming schemes), so other apps can hand it a video. Optional extras:

| Extra | Type | Meaning |
|-------|------|---------|
| `title` | String | Title shown in the player |
| `position` | Int | Start position in milliseconds |
| `subs` | Uri[] | Additional subtitle files |
| `subs.enable` | Uri[] | Which of `subs` to select |
| `headers` | String[] | HTTP headers as name/value pairs (a leading `User-Agent` sets the user agent) |
| `media_id` | String | Name under which the playback state is saved (useful for stream URLs, which have no file name) |
| `vid`, `aid`, `sid` | String | Video, audio and subtitle track as mpv values (`1`, `2`, …, `no`) |

Explicitly passed `position`, `vid`, `aid` and `sid` win over the state the player saved for that file.

When the player closes, it returns a result with the action `live.mehiz.mpvkt.ui.player.PlayerActivity.result` and the extras `position` and `duration` (milliseconds).

## Building it into an app (`player/`)

The `player/` module builds the same sources as `app/` as an Android library. unfold-jellyfin-player uses it like this:

- this repository as a git submodule, with the `player/` folder included as a Gradle project;
- plugin and library versions from this repository's `gradle/libs.versions.toml`, so both builds use the same ones;
- the merged manifest provides the `App` application class, `PlayerActivity` and the settings screen; the host app keeps its own name, icon and theme and removes the launcher entry and the "open with" filters;
- `MediaPlaybackService.currentPositionMs()` gives the current position, e.g. for progress reports to a media server.

The plugins and dependencies are listed in both `app/build.gradle.kts` and `player/build.gradle.kts`; when one changes, change the other too.

## Building

Requires JDK 21 and the Android SDK (`ANDROID_HOME` or `sdk.dir` in `local.properties`).

```
./gradlew detekt assembleRelease
```

- `release`: minified, unsigned APKs per ABI (`armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`) plus a universal one, in `app/build/outputs/apk/release/`
- `preview`: like release, but debug-signed and installable next to it (`.preview`)
- `debug`: `.debug`, not minified

mpv itself comes prebuilt from the `mpv-android-lib` library (version in `gradle/libs.versions.toml`); a newer mpv or FFmpeg needs a new build of that library.

## Acknowledgments

- mpvKt by abdallahmehiz, which this fork is based on
- mpv-android, whose libmpv build the player uses
- mpv and FFmpeg
