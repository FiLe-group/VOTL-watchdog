package dev.fileeditor.watchdog.constants

enum class ExitCodes(val code: Int) {
	NORMAL(0),
	ERROR(1),
	RESTART(10),
	UPDATE(11)
}