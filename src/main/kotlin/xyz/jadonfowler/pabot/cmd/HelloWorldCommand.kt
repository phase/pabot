package xyz.jadonfowler.pabot.cmd

import xyz.jadonfowler.pabot.Pabot

class HelloWorldCommand : CommandHandler("hello") {

    override fun execute(args: List<String>, player: String) {
        Pabot.bot?.sendMessage("Hello, " + player)
    }
}