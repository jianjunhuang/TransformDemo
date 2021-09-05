plugins {
    `kotlin-dsl`
    `java-library`
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.0-alpha11")
    implementation("com.android.tools.build:gradle-api:7.1.0-alpha11")
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("commons-io:commons-io:2.11.0")
}

repositories {
    google()
    mavenCentral()
}

