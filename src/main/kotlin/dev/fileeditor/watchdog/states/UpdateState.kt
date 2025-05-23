package dev.fileeditor.watchdog.states

import dev.fileeditor.watchdog.Application
import dev.fileeditor.watchdog.contacts.StateHandler
import dev.fileeditor.watchdog.contacts.Updater

class UpdateState(app: Application) : StateHandler(app) {

	var updater: Updater? = null

	override fun run() {
		val cUpdater = updater ?: return

		try {
			cUpdater.run()
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

}