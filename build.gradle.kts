plugins {
	kotlin("jvm") version "2.1.21"
	// https://mvnrepository.com/artifact/com.gradleup.shadow/shadow-gradle-plugin
	id("com.gradleup.shadow") version "9.3.0"
	id("org.jetbrains.dokka") version "2.1.0"
}

group = "dev.fileeditor"
version = "1.1"

repositories {
	mavenCentral()
}

dependencies {
	// https://mvnrepository.com/artifact/net.dv8tion/JDA
	implementation("net.dv8tion:JDA:6.1.2")
	// https://mvnrepository.com/artifact/commons-cli/commons-cli
	implementation("commons-cli:commons-cli:1.11.0")
	// https://mvnrepository.com/artifact/org.jsoup/jsoup
	implementation("org.jsoup:jsoup:1.21.2")
}

java {
	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

kotlin {
	jvmToolchain(25)
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
