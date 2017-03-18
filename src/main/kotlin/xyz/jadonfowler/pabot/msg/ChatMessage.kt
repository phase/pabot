package xyz.jadonfowler.pabot.msg

import xyz.jadonfowler.pabot.Pabot
import java.util.*
import java.util.regex.Pattern

class ChatMessage(input: String) {

    val REGEX_ORE: Pattern = Pattern.compile("""\[(.*)\](.*): (.*)""")
    val REGEX_ORE_PM: Pattern = Pattern.compile("""\[\[(.*)](.*) -> me\] (.*)""")

    val user: String?
    var command: String
    val args: List<String>

    init {
        val ore = REGEX_ORE.matcher(input)
        val ore_pm = REGEX_ORE_PM.matcher(input)
        if (ore_pm.find()) {
            user = ore_pm.group(2)
            val message = ore_pm.group(3)
            if (message.contains(" ")) {
                command = Pabot.bot.CHAT_PREFIX + message.split(" ")[0]
                args = message.split(" ").subList(1, message.split(" ").size)
            } else {
                command = message
                args = ArrayList<String>()
            }
        } else if (ore.find()) {
            user = ore.group(2)
            val message = ore.group(3)
            if (message.contains(" ")) {
                command = message.split(" ")[0]
                args = message.replaceFirst(command + " ", "").split(" ")
            } else {
                command = message
                args = ArrayList<String>()
            }
        }
        // TODO: other chat regexes
        else {
            user = null
            command = ""
            args = ArrayList<String>()
        }
    }

}