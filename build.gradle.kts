import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
}

group = "net.botwithus.example"
version = "1.0-SNAPSHOT"

configurations {
    create("includeInJar") {
        this.isTransitive = false
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

val copyJar by tasks.register<Copy>("copyJar") {
    from("build/libs/")
    into("${System.getProperty("user.home")}\\BotWithUs\\scripts\\local\\")
    include("*.jar")
}

tasks.named<Jar>("jar") {
    from({
        configurations["includeInJar"].map { zipTree(it) }
    })
    from("src/main/resources")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    finalizedBy(copyJar)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "20"
    targetCompatibility = "20"
    options.compilerArgs.add("--enable-preview")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        setUrl("https://nexus.botwithus.net/repository/maven-snapshots/")
    }
}

dependencies {
    implementation("net.botwithus.rs3:botwithus-api:1.0.0-SNAPSHOT")
    implementation("net.botwithus.xapi.public:api:1.0.0-SNAPSHOT")
    "includeInJar"("net.botwithus.xapi.public:api:1.0.0-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("com.github.kittinunf.fuel:fuel:2.3.1") // HTTP networking
    implementation ("com.github.kittinunf.fuel:fuel-gson:2.3.1") // Gson support for Fuel
    implementation ("com.google.code.gson:gson:2.8.8")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "20"
}