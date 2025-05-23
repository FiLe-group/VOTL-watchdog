package dev.fileeditor.watchdog

object VersionFormatter {
	fun formatAndSend() {
		val pb = ProcessBuilder()

		pb.command("java", "-jar", "VOTL.jar", "--version", "--no-colors")

		try {
			val process = pb.start()

			val content = StringBuilder()
			process.inputStream.bufferedReader().useLines { lines ->
				lines.forEach { content.appendLine(it) }
			}

			process.waitFor()
			process.destroy()

			if (content.isBlank()) {
				println("No VOTL.jar file found, unable to show the version of bot being used.")
				println("Watchdog is version: " + AppInfo.VERSION)
				return
			}

			content.split("\n").forEach { line ->
				println(line)
				if (line.contains("Version:")) {
					val watchdogLine = StringBuilder("\tWatchdog:")
					val index = line.indexOf(line.trim().split("\\s+")[1])
					watchdogLine.repeat(" ", index)
					println(watchdogLine.append(AppInfo.VERSION))
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}