package xyz.jadonfowler.pabot.cmd.chat

import xyz.jadonfowler.pabot.Pabot
import xyz.jadonfowler.pabot.cmd.CommandHandler

class EchoCommand : CommandHandler("echo") {

    override fun execute(args: List<String>, player: String) {
        var message = ""
        for (m in args) message += m + " "
        message = message.substring(0, if (message.length > 0) message.length - 1 else 0)
        Pabot.bot?.sendMessage(message)
    }

}