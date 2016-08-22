package xyz.jadonfowler.pabot

import org.spacehq.mc.protocol.data.message.Message
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket
import org.spacehq.packetlib.event.session.*
import org.spacehq.packetlib.packet.Packet

class PacketHandler(val bot: Pabot) : SessionAdapter() {

    override fun connected(event: ConnectedEvent?) {
        println("Connected to " + event!!.session.host)
    }

    override fun disconnected(event: DisconnectedEvent?) {
        val reason = Message.fromString(event!!.reason).fullText
        println("Disconnected: " + reason)
    }

    override fun disconnecting(event: DisconnectingEvent?) {
        super.disconnecting(event)
    }

    override fun packetReceived(event: PacketReceivedEvent?) {
        val packet = event!!.getPacket<Packet>()
        // println(packet.toString())
        when (packet) {
            is ServerJoinGamePacket -> {
                bot.gameSettings = GameSettings(packet.difficulty, packet.dimension, packet.gameMode,
                                                packet.hardcore, packet.maxPlayers, packet.worldType)
                println(bot.gameSettings)
                event.session.send(ClientChatPacket("Hello, I'm a bot!"))
            }
            is ServerMultiBlockChangePacket -> {
                for (record in packet.records) {
                    println(record.position.toString() + " -> " + record.block.id)
                }
            }
        }
    }

    override fun packetSent(event: PacketSentEvent?) {
        super.packetSent(event)
    }

}