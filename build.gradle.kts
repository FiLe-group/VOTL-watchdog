plugins {
	kotlin("jvm") version "2.1.21"
	// https://mvnrepository.com/artifact/com.gradleup.shadow/shadow-gradle-plugin
	id("com.gradleup.shadow") version "9.0.0-beta13"
	id("org.jetbrains.dokka") version "2.0.0"
}

group = "dev.fileeditor"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	// https://mvnrepository.com/artifact/net.dv8tion/JDA
	compileOnly("net.dv8tion:JDA:5.5.1")
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
	jvmToolchain(21)
}

tasks.build {
	dependsOn(tasks.shadowJar)
}

artifacts {
	archives(tasks.shadowJar)
}

tasks.shadowJar {
	archiveBaseName.set("votl-watchdog")
	archiveClassifier.set("")
	archiveVersion.set(version.toString())

	minimize()  // Removes unused classes

	exclude("META-INF/*.SF")
	exclude("META-INF/*.DSA")
	exclude("META-INF/*.RSA")
	exclude("META-INF/LICENSE*")
	exclude("META-INF/NOTICE*")
	exclude("META-INF/DEPENDENCIES")

	manifest {
		attributes(
			"Implementation-Title" to project.name,
			"Implementation-Version" to project.version,
			"Implementation-Vendor" to "FiLe group",
		)
	}

	finalizedBy("sourcesJar")
}

tasks.compileJava {
	options.encoding = "UTF-8"
}
