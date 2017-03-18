package xyz.jadonfowler.pabot.cmd

import xyz.jadonfowler.pabot.Pabot

abstract class CommandHandler(val command: String) {

    init {
        Pabot.bot.addCommandHandler(this)
    }

    /**
     * @param args Arguments for command
     * @param player Player who executed the command
     */
    abstract fun execute(args: List<String>, player: String)

}