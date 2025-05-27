package dev.fileeditor.watchdog

import dev.fileeditor.watchdog.constants.ExitCodes
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import java.io.File
import kotlin.system.exitProcess

object Main {

	val isWindows = System.getProperty("os.name").lowercase().contains("win")
	val botJar = File("VOTL.jar")

	@JvmStatic
	fun main(args: Array<String>) {
		val options = Options()

		options.addOption("h", "help", false, "Displays this help menu.")
		options.addOption("v", "version", false, "Displays the current version of the application.")
		options.addOption("sc", "shard-count", true, "Sets the amount of shards the bot should start up.")
		options.addOption("nocolor", "no-colors", false, "Disables colors for commands in the terminal.")
		options.addOption("d", "debug", false, "Enables debugging mode, this will log extra information to the terminal.")
		options.addOption("jarg", "jvm-argument", true, "Sets the JVM arguments that the application should be started with.")

		val parser = DefaultParser()
		val formatter = HelpFormatter()

		try {
			val cmd = parser.parse(options, args)
			if (cmd.hasOption("h")) {
				formatter.printHelp("Help menu", options)
				exitProcess(ExitCodes.NORMAL.code)
			}
			if (cmd.hasOption("v")) {
				VersionFormatter.formatAndSend()
				exitProcess(ExitCodes.NORMAL.code)
			}

			Application(cmd)
		} catch (e: ParseException) {
			println(e.message)
			formatter.printHelp("", options)

			exitProcess(ExitCodes.NORMAL.code)
		}
	}

}