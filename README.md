VOTL Watchdog
=======

VOTL Watchdog is an application built for the Discord bot [VOTL](https://github.com/FiLe-group/VOTL), the app will help
manage VOTL by starting, restarting and updating the bot , the application also comes with a terminal that allows you
to see the output and interact with the bot.

### Disclaimer
This project is a rewrite of [AvaIre Watchdog](https://github.com/avaire/watchdog) by AvaIre team, licensed under MIT.  
Rewrite is done in Kotlin and includes some new features. 

## Installation

#### Server requirements

Watchdog has a few system requirements, that ensure that Watchdog and all of its features will work correctly.

- [JAVA SE / JDK](https://www.oracle.com/java/) >= 21
- 128 MB Memory

#### Installing Watchdog

Start by cloning down the source code and building the binary using Gradle, optionally you can also use one of the
[releases](https://github.com/FiLe-group/VOTL-watchdog/releases).

    git clone https://github.com/FiLe-group/VOTL-watchdog.git
    cd VOTL-watchdog
    .\gradlew build

The binary file cane be found withing the root of the `VOTL-wathcdog` directory, next we'll need to run Watchdog so it
can download out [VOTL](https://github.com/FiLe-group/VOTL) jar file, to do this run:

    java -jar VOTL-watchdog.jar

Once the Watchdog has downloaded the jar file it will automatically start trying to boot ip the bot, the first time this
happens it will fils since the configuration file has yet to be generated, once the process stops, open up the newly
generated `data/config.json` file and set up your Discord and database settings, everything else os optional.
When you're done configuring the bot, run Watchdog again to make it start the VOTL process and watch for any commands
bot might send its way.

If the bot start up normally you've successfully setup Watchdog to run VOTL!

> **Note:** If you would like to start VOTL with some different options you can pass them as arguments to Watchdog, for
> example to see what custom options VOTL support you can try using the `--help` argument.

    java -jar VOTL-watchdog.jar --help
