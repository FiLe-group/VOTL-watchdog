package dev.fileeditor.watchdog

import dev.fileeditor.watchdog.constants.ExitCodes
import dev.fileeditor.watchdog.states.ShutdownState
import dev.fileeditor.watchdog.states.UpdateState
import dev.fileeditor.watchdog.updater.BuildUpdater
import okio.IOException
import org.apache.commons.cli.CommandLine
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.system.exitProcess

class Application(private val commandArguments: CommandLine): Thread() {

	private val updateState = UpdateState(this)
	private val shutdownState = ShutdownState(this)

	var running = true

	private var recentBoots = 0
	private var lastBoot: Long = 0

	var process: Process? = null

	init {
		updateState.updater = BuildUpdater(this)

		if (!Main.botJar.exists()) {
			Logger.info("VOTL sources was not found at "+Main.botJar.absolutePath+"!")
			updateState.run()
		}

		start()

		val reader = BufferedReader(InputStreamReader(System.`in`))

		while (true) {
			val line = reader.readLine() ?: break
			if (line.equals("shutdown", true)) {
				shutdownState.run()
			}
		}
	}

	override fun run() {
		while (running) {
			try {
				process = boot()

				process!!.inputStream.bufferedReader().useLines { lines ->
					lines.forEach { println(it) }
				}

				val exitCode = process!!.waitFor()
				Logger.info("Bot exited with code: $exitCode")

				when (exitCode) {
					ExitCodes.UPDATE.code -> {
						Logger.info("Now updating...")
						updateState.run()
					}
					ExitCodes.NORMAL.code, 130 -> {
						Logger.info("Now shutting down...")
						shutdownState.run()
						break
					}
					else -> {
						Logger.info("Now restarting...")
					}
				}
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
	}

	@Throws(IOException::class)
	private fun boot(): Process {
		// Prevent login loop
		if (System.currentTimeMillis() - lastBoot > 120*1000) {
			recentBoots = 0
		}

		recentBoots++
		lastBoot = System.currentTimeMillis()

		if (recentBoots >= 4) {
			Logger.info("Failed to restart 3 times, probably due to login or configuration errors. Exiting...")
			exitProcess(ExitCodes.ERROR.code)
		}

		val command = mutableListOf<String>()
		command.add("java")

		if (Main.isWindows) {
			command.add("-Dfile.encoding=UTF-8")
		}

		ArgumentHandler.addJvmArguments(commandArguments, command)

		command.addAll(listOf("-jar", "VOTL.jar"))

		ArgumentHandler.addApplicationArguments(commandArguments, command)

		Logger.info("Starting process ${command.joinToString(" ")}")

		return ProcessBuilder(command)
			.inheritIO()
			.start()
	}
}