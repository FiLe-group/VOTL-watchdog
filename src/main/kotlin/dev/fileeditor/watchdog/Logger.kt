package dev.fileeditor.watchdog

object Logger {
	private val prefix = "[WATCHDOG]"

	fun info(message: String) {
		println("$prefix $message")
	}

	fun error(message: String) {
		System.err.println("$prefix $message")
	}
}