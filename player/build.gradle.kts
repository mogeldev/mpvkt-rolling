import org.apache.commons.io.output.ByteArrayOutputStream
import org.gradle.process.ExecOperations
import javax.inject.Inject

// The player as a library, for apps that build it in (e.g. Unfold). Same sources and resources as :app, no copy;
// the embedding app gets App, PlayerActivity and the settings screens through the merged manifest.
// shortcut: plugins and dependencies are duplicated from app/build.gradle.kts; keep both in sync when they change.
plugins {
  alias(libs.plugins.ksp)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.compose.compiler)
  alias(libs.plugins.room)
  alias(libs.plugins.about.libraries)
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.metro)
}

android {
  namespace = "live.mehiz.mpvkt"
  compileSdk = 37

  defaultConfig {
    minSdk = 24
    vectorDrawables {
      useSupportLibrary = true
    }
    // libraries get no VERSION_NAME; keep it equal to versionName in app/build.gradle.kts
    buildConfigField("String", "VERSION_NAME", "\"0.2.0\"")
    buildConfigField("String", "GIT_SHA", "\"${getCommitSha()}\"")
    buildConfigField("int", "GIT_COUNT", getCommitCount())
  }
  sourceSets.named("main") {
    manifest.srcFile("../app/src/main/AndroidManifest.xml")
    java.directories += "../app/src/main/java"
    kotlin.directories += "../app/src/main/java"
    res.directories += "../app/src/main/res"
    assets.directories += "../app/src/main/assets"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll(
      "-Xwhen-guards",
      "-Xcontext-parameters",
      "-XXLanguage:+PropertyParamAnnotationDefaultTargetMode",
    )
  }
}

room {
  schemaDirectory("${layout.buildDirectory.get()}/schemas") // generated, not kept in the repo
}

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.material3.android)
  implementation(libs.androidx.ui.tooling.preview)
  debugImplementation(libs.androidx.ui.tooling)
  implementation(libs.bundles.compose.navigation3)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.compose.constraintlayout)
  implementation(libs.androidx.material3.icons.extended)
  implementation(libs.androidx.compose.animation.graphics)
  implementation(libs.material)
  implementation(libs.androidx.preferences.ktx)
  implementation(libs.androidx.documentfile)
  implementation(libs.mediasession)
  implementation(libs.saveable)

  implementation(libs.mpv.lib)

  implementation(libs.metrox.viewmodel.compose)
  implementation(libs.seeker)
  implementation(libs.compose.prefs)
  implementation(libs.bundles.about.libs)
  implementation(libs.simple.icons)

  implementation(libs.room.runtime)
  ksp(libs.room.compiler)
  implementation(libs.room.ktx)

  implementation(libs.kotlinx.immutable.collections)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.truetype.parser)
  implementation(libs.fsaf)
}

aboutLibraries {
  export.excludeFields.add("generated")
}

interface InjectedExecOps {
  @get:Inject val execOps: ExecOperations
}

fun getCommitCount(): String = runCommand("git", "rev-list", "--count", "HEAD")
fun getCommitSha(): String = runCommand("git", "rev-parse", "--short", "HEAD")

fun runCommand(vararg command: String): String {
  val stdOut = ByteArrayOutputStream()
  val injected = project.objects.newInstance<InjectedExecOps>()
  injected.execOps.exec {
    commandLine(*command)
    standardOutput = stdOut
    workingDir = projectDir
  }
  return String(stdOut.toByteArray()).trim()
}
