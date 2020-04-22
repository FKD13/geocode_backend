import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    war
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
    kotlin("plugin.jpa") version "1.3.61"
}

group = "be.ugent.webdevelopment.backend"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

repositories {
    mavenCentral()
    maven(url="https://jitpack.io")
}

dependencies {
    // Mariadb
    implementation("org.mariadb.jdbc:mariadb-java-client")
    // Swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.2.33")
    // JWT
    implementation("com.auth0:java-jwt:3.4.0")
    // Email
    implementation("com.sun.mail:javax.mail:1.6.2")
    // Security
    implementation("org.springframework.security:spring-security-core")
    // Qrcode
    implementation("com.google.zxing:core:3.4.0")
    implementation("com.google.zxing:javase:3.4.0")
    // Tracing
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // Pdf
    implementation("com.itextpdf:itextpdf:5.5.13.1")
    implementation("org.apache.pdfbox:pdfbox:2.0.19")

    implementation("org.jetbrains.kotlin:kotlin-maven-noarg")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.session:spring-session-core")
    implementation("com.github.jsonld-java:jsonld-java:0.13.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
