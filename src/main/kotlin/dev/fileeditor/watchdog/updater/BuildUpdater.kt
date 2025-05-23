package dev.fileeditor.watchdog.updater

import dev.fileeditor.watchdog.Application
import dev.fileeditor.watchdog.Logger
import dev.fileeditor.watchdog.Main.botJar
import dev.fileeditor.watchdog.contacts.Updater
import org.jsoup.Jsoup
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class BuildUpdater(app: Application) : Updater(app) {

	@Throws(Exception::class)
	override fun run() {
		Logger.info("Downloading the latest version using stable build.")

		val res = Jsoup.connect("https://github.com/FiLe-group/VOTL/releases/latest/download/VOTL.jar")
			.timeout(10000)
			.ignoreContentType(true)
			.maxBodySize(1024 * 1024 * 40)
			.execute()

		Files.copy(
			res.bodyStream(),
			botJar.toPath(),
			StandardCopyOption.REPLACE_EXISTING
		)

		Logger.info("The build has been downloaded successfully!")
	}

}