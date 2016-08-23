package xyz.jadonfowler.pabot

import org.spacehq.mc.protocol.MinecraftConstants
import org.spacehq.mc.protocol.MinecraftProtocol
import org.spacehq.mc.protocol.data.game.chunk.Column
import org.spacehq.mc.protocol.data.game.world.block.BlockChangeRecord
import org.spacehq.packetlib.Client
import org.spacehq.packetlib.tcp.TcpSessionFactory
import xyz.jadonfowler.pabot.cmd.CommandHandler
import java.net.Proxy
import java.util.*

class Bot(username: String, password: String, host: String, port: Int = 25565) {

    var client: Client? = null
    var gameSettings: GameSettings? = null
    val chunks: ArrayList<Column> = ArrayList()
    val commandHandlers: ArrayList<CommandHandler> = ArrayList()

    init {
        val protocol = MinecraftProtocol(username, password)
        client = Client(host, port, protocol, TcpSessionFactory(Proxy.NO_PROXY))
        client?.session?.setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY)
        client?.session?.addListener(PacketHandler(this))
    }

    fun login() {
        client?.session?.connect()
    }

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

    fun addCommandHandler(handler: CommandHandler) {
        commandHandlers.add(handler)
    }

    fun executeCommand(command: String, args: List<String>, player: String) {
        for (handler in commandHandlers) {
            if (handler.command.equals(command, true)) {
                handler.execute(args, player)
            }
        }
    }

}