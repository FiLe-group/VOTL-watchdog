package dev.fileeditor.watchdog

import org.apache.commons.cli.CommandLine

object ArgumentHandler {
	fun addApplicationArguments(commandLine: CommandLine, list: MutableList<String>) {
		if (commandLine.hasOption("version")) {
			list.add("--version")
		}

		if (commandLine.hasOption("no-colors")) {
			list.add("--no-colors")
		}

		if (commandLine.hasOption("debug")) {
			list.add("--debug")
		}

		if (commandLine.hasOption("shard-count")) {
			list.add("--shard-count=" + commandLine.getOptionValue("shard-count", "1"))
		}
	}

	fun addJvmArguments(commandLine: CommandLine, list: MutableList<String>) {
		if (commandLine.hasOption("jvm-argument")) {
			list.addAll(commandLine.getOptionValue("jvm-argument", "").split(" "))
		}
	}
}