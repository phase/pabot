package xyz.jadonfowler.pabot

import org.spacehq.mc.protocol.data.message.Message
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket
import org.spacehq.packetlib.event.session.ConnectedEvent
import org.spacehq.packetlib.event.session.DisconnectedEvent
import org.spacehq.packetlib.event.session.PacketReceivedEvent
import org.spacehq.packetlib.event.session.SessionAdapter
import org.spacehq.packetlib.packet.Packet
import xyz.jadonfowler.pabot.msg.ChatMessage

class PacketHandler(val bot: Bot) : SessionAdapter() {

    override fun connected(event: ConnectedEvent?) {
        println("Connected to " + event!!.session.host)
    }

    override fun disconnected(event: DisconnectedEvent?) {
        val reason = Message.fromString(event!!.reason).fullText
        println("Disconnected: " + reason)
    }

    override fun packetReceived(event: PacketReceivedEvent?) {
        if (event == null || event.getPacket<Packet>() == null) return
        val packet = event.getPacket<Packet>()
        // println(packet.toString())
        when (packet) {
            is ServerJoinGamePacket -> {
                bot.gameSettings = GameSettings(packet.difficulty, packet.dimension, packet.gameMode,
                        packet.hardcore, packet.maxPlayers, packet.worldType)
                println(bot.gameSettings)
            }
            is ServerChatPacket -> {
                val text = packet.message.fullText
                println(text)
                val message = ChatMessage(text)
                if (message.command != null && message.command!!.startsWith(bot.CHAT_PREFIX)) {
                    message.command = message.command!!.substring(1, message.command!!.length)
                    bot.executeCommand(message.command!!, message.args!!, message.user!!)
                }
            }
            is ServerMultiBlockChangePacket -> {
                for (record in packet.records) {
                    bot.updateBlock(record)
                }
            }
            is ServerBlockChangePacket -> {
                bot.updateBlock(packet.record)
            }
            is ServerChunkDataPacket -> {
                bot.chunks.add(packet.column)
            }
            is ServerPlayerPositionRotationPacket -> {
                bot.pos = Vector3d(packet.x, packet.y, packet.z)
                bot.pitch = packet.pitch
                bot.yaw = packet.yaw
                bot.sendCurrentPosition()
            }
        }
    }

}