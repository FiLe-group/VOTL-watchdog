package dev.fileeditor.watchdog.states

import dev.fileeditor.watchdog.Application
import dev.fileeditor.watchdog.contacts.StateHandler
import kotlin.system.exitProcess

class ShutdownState(app: Application) : StateHandler(app) {

	override fun run() {
		app.running = false

		if (app.process != null) {
			app.process!!.destroy()
		}

		exitProcess(0)
	}

}