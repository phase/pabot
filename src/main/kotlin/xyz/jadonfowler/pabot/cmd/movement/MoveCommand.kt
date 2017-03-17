package xyz.jadonfowler.pabot.cmd.movement

import xyz.jadonfowler.pabot.Pabot
import xyz.jadonfowler.pabot.cmd.CommandHandler

class MoveCommand : CommandHandler("move") {

    override fun execute(args: List<String>, player: String) {
        if (args.size != 3) {
            Pabot.bot!!.sendPrivateMessage("Usage: move x y z", player)
            return
        }
        val x = if (args[0].startsWith("~")) Pabot.bot!!.pos.x - args[0].substring(1, args[0].length - 1).toDouble() else args[0].toDouble()
        val y = if (args[1].startsWith("~")) Pabot.bot!!.pos.y - args[1].substring(1, args[1].length - 1).toDouble() else args[1].toDouble()
        val z = if (args[2].startsWith("~")) Pabot.bot!!.pos.z - args[2].substring(1, args[2].length - 1).toDouble() else args[2].toDouble()
        Thread { Pabot.bot!!.moveRelative(x, y, z) }.start()
    }
}