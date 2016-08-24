package xyz.jadonfowler.pabot.msg

import java.util.*
import java.util.regex.Pattern

class ChatMessage(input: String) {

    val REGEX_ORE: Pattern = Pattern.compile("""\[(.*)\](.*): (.*)""")

    val user: String?
    var command: String?
    val args: List<String>?

    init {
        val ore = REGEX_ORE.matcher(input)
        if (ore.find()) {
            user = ore.group(2)
            val message = ore.group(3)
            if (message.contains(" ")) {
                command = message.split(" ")[0]
                args = message.replaceFirst(command!! + " ", "").split(" ")
            } else {
                command = message
                args = ArrayList<String>()
            }
        }
        // TODO: other chat regexes
        else {
            user = null
            command = null
            args = null
        }
    }

}