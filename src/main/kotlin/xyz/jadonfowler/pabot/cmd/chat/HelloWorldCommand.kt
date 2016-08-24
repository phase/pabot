package xyz.jadonfowler.pabot.cmd.chat

import xyz.jadonfowler.pabot.Pabot
import xyz.jadonfowler.pabot.cmd.CommandHandler

class HelloWorldCommand : CommandHandler("hello") {

    override fun execute(args: List<String>, player: String) {
        Pabot.bot?.sendMessage("Hello, " + player)
    }
}