package xyz.jadonfowler.pabot

import com.github.steveice10.mc.protocol.data.message.Message
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket
import com.github.steveice10.packetlib.event.session.ConnectedEvent
import com.github.steveice10.packetlib.event.session.DisconnectedEvent
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent
import com.github.steveice10.packetlib.event.session.SessionAdapter
import com.github.steveice10.packetlib.packet.Packet
import xyz.jadonfowler.pabot.msg.ChatMessage

class PacketHandler(val bot: Bot) : SessionAdapter() {

    override fun connected(event: ConnectedEvent?) {
        println("Connected to " + event!!.session.host)
    }

    override fun disconnected(event: DisconnectedEvent?) {
        val reason = Message.fromString(event!!.reason).fullText
        println("Disconnected: " + reason)
        System.exit(0)
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
                if (message.command.startsWith(bot.CHAT_PREFIX)) {
                    val len = message.command.length
                    message.command = message.command.substring(if (len > 0) 1 else 0, if (len > 0) len else 0)
                    bot.executeCommand(message.command, message.args, message.user!!)
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