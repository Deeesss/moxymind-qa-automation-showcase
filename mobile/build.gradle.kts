plugins {
    kotlin("jvm") version "2.4.0"
    id("com.android.application") version "9.2.1" apply false
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.appium:java-client:10.1.1")
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.1")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("android", "ios")
    }
}

tasks.register<Test>("testAndroid") {
    description = "Runs Android Appium tests."
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    environment("MOBILE_PLATFORM", "android")
    useJUnitPlatform {
        includeTags("android")
    }
}

tasks.register<Test>("testIos") {
    description = "Runs iOS Appium tests."
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    environment("MOBILE_PLATFORM", "ios")
    useJUnitPlatform {
        includeTags("ios")
    }
}
