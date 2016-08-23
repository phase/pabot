package xyz.jadonfowler.pabot.cmd

import xyz.jadonfowler.pabot.Pabot

abstract class CommandHandler(val command: String, bot: Pabot) {

    init {
        bot.addCommandHandler(this)
    }

    abstract fun execute(args: List<String>, player: String)

}