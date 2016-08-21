package xyz.jadonfowler.pabot

import org.spacehq.mc.protocol.MinecraftConstants
import org.spacehq.mc.protocol.MinecraftProtocol
import org.spacehq.packetlib.Client
import org.spacehq.packetlib.tcp.TcpSessionFactory
import java.net.Proxy

class Pabot(username : String, password : String, val host : String, val port : Int = 25565) {

    val client : Client

    init {
        val protocol = MinecraftProtocol(username, password)
        client = Client(host, port, protocol, TcpSessionFactory(Proxy.NO_PROXY))
        client.session.setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY)
    }

    fun login() {
        client.session.connect()
    }

}

fun main(args : Array<String>) {
    val bot = Pabot(args[0], args[1], args[2])
    bot.login()
}