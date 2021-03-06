package xyz.jadonfowler.pabot.cmd.chat

import xyz.jadonfowler.pabot.Pabot
import xyz.jadonfowler.pabot.cmd.CommandHandler

class EchoCommand : CommandHandler("echo") {

    override fun execute(args: List<String>, player: String) {
        var message = ""
        for (m in args) message += m + " "
        message = message.substring(0, if (message.isNotEmpty()) message.length - 1 else 0)
        Pabot.bot.sendPrivateMessage(message, player)
    }

}

class HelloWorldCommand : CommandHandler("hello") {

    override fun execute(args: List<String>, player: String) {
        Pabot.bot.sendPrivateMessage("Hello, " + player, player)
    }

}

class SudoCommand : CommandHandler("sudo") {

    override fun execute(args: List<String>, player: String) {
        val command = args.joinToString(" ")
        Pabot.bot.sendMessage("/$command")
    }

}
