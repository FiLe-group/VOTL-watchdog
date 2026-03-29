package dev.fileeditor.watchdog.updater

import dev.fileeditor.watchdog.Application
import dev.fileeditor.watchdog.Logger
import dev.fileeditor.watchdog.Main.botJar
import dev.fileeditor.watchdog.contacts.Updater
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jsoup.Jsoup
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class BuildUpdater(app: Application) : Updater(app) {

	@Throws(Exception::class)
	override fun run() {
		Logger.info("Downloading the latest version using stable build.")

		// Fetch latest tag
		val apiRes = Jsoup.connect("https://api.github.com/repos/FiLe-group/VOTL/releases/latest")
			.timeout(10000)
			.ignoreContentType(true)
			.execute()

		val tag = Json.parseToJsonElement(apiRes.body()).jsonObject.jsonObject["tag_name"]?.jsonPrimitive?.content
		val jarName = "VOTL-$tag.jar"
		val downloadUrl = "https://github.com/FiLe-group/VOTL/releases/latest/download/$jarName"

		Logger.info("Downloading $jarName...")

		val res = Jsoup.connect(downloadUrl)
			.timeout(10000)
			.ignoreContentType(true)
			.maxBodySize(1024 * 1024 * 40)
			.followRedirects(true)
			.execute()

		Files.copy(
			res.bodyStream(),
			botJar.toPath(),
			StandardCopyOption.REPLACE_EXISTING
		)

		Logger.info("The build has been downloaded successfully!")
	}

}