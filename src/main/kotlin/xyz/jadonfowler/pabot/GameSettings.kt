package xyz.jadonfowler.pabot

import org.spacehq.mc.protocol.data.game.entity.player.GameMode
import org.spacehq.mc.protocol.data.game.setting.Difficulty
import org.spacehq.mc.protocol.data.game.world.WorldType

data class GameSettings(val difficulty: Difficulty, val dimension: Int, val gameMode: GameMode, val hardcore: Boolean,
                   val maxPlayers: Int, val worldType: WorldType)