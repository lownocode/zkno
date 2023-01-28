plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.6.20" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.20" apply false
    id("com.google.gms.google-services") version "4.3.13" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}