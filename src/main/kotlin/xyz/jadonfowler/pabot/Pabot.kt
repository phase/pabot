package xyz.jadonfowler.pabot

import xyz.jadonfowler.pabot.cmd.chat.EchoCommand
import xyz.jadonfowler.pabot.cmd.chat.HelloWorldCommand
import xyz.jadonfowler.pabot.cmd.movement.MoveCommand
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*

object Pabot {

    var bot: Bot? = null

    fun registerCommands() {
        run { // Chat Commands
            HelloWorldCommand()
            EchoCommand()
            MoveCommand()
        }
    }

}

fun main(args: Array<String>) {
    val bot = getBotFromPopup()
    Pabot.registerCommands()
    bot.login()
}

fun getBotFromPopup(): Bot {
    val panel = JPanel(BorderLayout(5, 5))

    val label = JPanel(GridLayout(0, 1, 2, 2))
    label.add(JLabel("Username", SwingConstants.RIGHT))
    label.add(JLabel("Password", SwingConstants.RIGHT))
    label.add(JLabel("Server IP", SwingConstants.RIGHT))
    panel.add(label, BorderLayout.WEST)

    val controls = JPanel(GridLayout(0, 1, 2, 2))
    val username = JTextField()
    val password = JPasswordField()
    val serverAddress = JTextField()
    serverAddress.text = "mc.openredstone.org:25565" // For testing
    controls.add(username)
    controls.add(password)
    controls.add(serverAddress)
    panel.add(controls, BorderLayout.CENTER)

    JOptionPane.showMessageDialog(null, panel, "pabot - Login", JOptionPane.QUESTION_MESSAGE)

    Pabot.bot = Bot(username.text, String(password.password), serverAddress.text.split(":")[0], serverAddress.text.split(":")[1].toInt())
    return Pabot.bot!!
}