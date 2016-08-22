package xyz.jadonfowler.pabot

import org.spacehq.mc.protocol.MinecraftConstants
import org.spacehq.mc.protocol.MinecraftProtocol
import org.spacehq.packetlib.Client
import org.spacehq.packetlib.tcp.TcpSessionFactory
import java.awt.BorderLayout
import java.awt.GridLayout
import java.net.Proxy
import javax.swing.*

class Pabot(username: String, password: String, val host: String, val port: Int = 25565) {

    val client: Client
    var gameSettings: GameSettings? = null

    init {
        val protocol = MinecraftProtocol(username, password)
        client = Client(host, port, protocol, TcpSessionFactory(Proxy.NO_PROXY))
        client.session.setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY)
    }

    fun login() {
        client.session.addListener(PacketHandler(this))
        client.session.connect()
    }

}

fun main(args: Array<String>) {
    val bot = getBotFromPopup()
    bot.login()
}

fun getBotFromPopup(): Pabot {
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
    serverAddress.text = "84.215.69.184:25569" // For testing
    controls.add(username)
    controls.add(password)
    controls.add(serverAddress)
    panel.add(controls, BorderLayout.CENTER)

    JOptionPane.showMessageDialog(null, panel, "pabot - Login", JOptionPane.QUESTION_MESSAGE)

    return Pabot(username.text, String(password.password), serverAddress.text.split(":")[0], serverAddress.text.split(":")[1].toInt())
}