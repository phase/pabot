package xyz.jadonfowler.pabot

import org.spacehq.mc.protocol.MinecraftConstants
import org.spacehq.mc.protocol.MinecraftProtocol
import org.spacehq.mc.protocol.data.game.chunk.Column
import org.spacehq.mc.protocol.data.game.world.block.BlockChangeRecord
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket
import org.spacehq.packetlib.Client
import org.spacehq.packetlib.packet.Packet
import org.spacehq.packetlib.tcp.TcpSessionFactory
import xyz.jadonfowler.pabot.cmd.CommandHandler
import java.net.Proxy
import java.util.*

class Bot(username: String, password: String, host: String, port: Int = 25565) {

    var client: Client? = null
    val commandHandlers: ArrayList<CommandHandler> = ArrayList()
    var gameSettings: GameSettings? = null
    val chunks: ArrayList<Column> = ArrayList()
    var pos: Vector3d = Vector3d(0.0, 64.0, 0.0)
    var pitch: Float = 0f
    var yaw: Float = 0f

    init {
        val protocol = MinecraftProtocol(username, password)
        client = Client(host, port, protocol, TcpSessionFactory(Proxy.NO_PROXY))
        client?.session?.setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY)
        client?.session?.addListener(PacketHandler(this))
    }

    fun login() {
        client?.session?.connect()
        Thread {
            while (true) {
                if (chatQueue.size > 0) sendPacket(ClientChatPacket(chatQueue.pollFirst()))
                Thread.sleep(1000)
            }
        }.start()
    }

    fun sendPacket(packet: Packet) {
        client?.session?.send(packet)
    }

    /* World Data */

    fun getChunkColumn(x: Int, z: Int): Column? {
        for (chunk in chunks) {
            if (chunk.x == x && chunk.z == z) return chunk
        }
        return null
    }

    fun updateBlock(record: BlockChangeRecord) {
        val chunkX: Int = record.position.x / 16
        val chunkY: Int = record.position.y / 16
        val chunkZ: Int = record.position.z / 16 // + (record.position.z > 0 ? 0 : 1) // maybe?
        val blockX: Int = record.position.x % 16
        val blockY: Int = record.position.y % 16
        val blockZ: Int = record.position.z % 16
        getChunkColumn(chunkX, chunkZ)!!.chunks[chunkY].blocks.set(blockX, blockY, blockZ, record.block)
    }

    /* Chat */

    val CHAT_PREFIX = "~"
    val chatQueue = LinkedList<String>()
    val whitelist: List<String> = Arrays.asList("phase", "VoltzLive")

    fun sendMessage(message: String) {
        chatQueue.add(message)
    }

    fun addCommandHandler(handler: CommandHandler) {
        commandHandlers.add(handler)
    }

    fun executeCommand(command: String, args: List<String>, player: String) {
        if (whitelist.contains(player)) {
            for (handler in commandHandlers) {
                if (handler.command.equals(command, true)) {
                    handler.execute(args, player)
                }
            }
        }
    }

    /* Movement */

    fun updatePosition(x: Double, y: Double, z: Double, pitch: Float, yaw: Float) {
        sendPacket(ClientPlayerPositionRotationPacket(false, x, y, z, pitch, yaw))
    }

    fun sendCurrentPosition() {
        updatePosition(pos.x, pos.y, pos.z, pitch, yaw)
    }

    /**
     * Relative move to location
     */
    fun moveRelative(rx: Double, ry: Double, rz: Double) {
        // TODO: Iterate over small increments and send position packets for each one
    }

    /**
     * Absolute move to location
     */
    fun moveAbsolute(ax: Double, ay: Double, az: Double) {
        moveRelative(pos.x - ax, pos.y - ay, pos.z - az)
    }

}