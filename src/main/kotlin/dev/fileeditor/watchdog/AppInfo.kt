package dev.fileeditor.watchdog

object AppInfo {
	val VERSION: String = javaClass.`package`.implementationVersion ?: "DEVELOPMENT"
}