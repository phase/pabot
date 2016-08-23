package xyz.jadonfowler.pabot.msg

class ChatMessage(input: String) {

    val user: String?
    var command: String?
    val args: List<String>?

    init {
        if(input.matches(Regex.fromLiteral("[(.*)](.*): (.*)"))) {
            // [Rank]User: Message
            user = input.split("]")[1].split(":")[0]
            val message = input.replace(input.split(": ")[0], "")
            command = message.split(" ")[0]
            args = message.replace(command!!, "").split(" ")
        }
        // TODO: other chat regexes
        else {
            user = null
            command = null
            args = null
        }
    }

}