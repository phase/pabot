package xyz.jadonfowler.pabot

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty
import com.github.steveice10.mc.protocol.data.game.world.WorldType


data class GameSettings(val difficulty: Difficulty, val dimension: Int, val gameMode: GameMode, val hardcore: Boolean,
                        val maxPlayers: Int, val worldType: WorldType)