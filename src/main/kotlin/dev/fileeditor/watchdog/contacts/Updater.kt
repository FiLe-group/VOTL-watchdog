package dev.fileeditor.watchdog.contacts

import dev.fileeditor.watchdog.Application
import kotlin.jvm.Throws

abstract class Updater(app: Application) {
	@Throws(Exception::class)
	abstract fun run()
}