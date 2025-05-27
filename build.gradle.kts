plugins {
	kotlin("jvm") version "2.1.21"
	// https://mvnrepository.com/artifact/com.gradleup.shadow/shadow-gradle-plugin
	id("com.gradleup.shadow") version "9.0.0-beta13"
	id("org.jetbrains.dokka") version "2.0.0"
}

group = "dev.fileeditor"
version = "1.0"

repositories {
	mavenCentral()
}

dependencies {
	// https://mvnrepository.com/artifact/net.dv8tion/JDA
	implementation("net.dv8tion:JDA:5.5.1")
	// https://mvnrepository.com/artifact/commons-cli/commons-cli
	implementation("commons-cli:commons-cli:1.9.0")
	// https://mvnrepository.com/artifact/org.jsoup/jsoup
	implementation("org.jsoup:jsoup:1.20.1")
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
	jvmToolchain(21)
}

tasks.jar {
	enabled = false
}

tasks.build {
	dependsOn(tasks.shadowJar)

	doLast {
		copy {
			from("build/libs") {
				include("VOTL-*.jar")
			}
			into(".")
			rename("VOTL-(.*).jar", "VOTL-watchdog.jar")
		}
	}
}

artifacts {
	archives(tasks.shadowJar)
}

tasks.shadowJar {
	archiveBaseName.set("VOTL-watchdog")
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
			"Main-Class" to "dev.fileeditor.watchdog.Main"
		)
	}
}

tasks.compileJava {
	options.encoding = "UTF-8"
}
