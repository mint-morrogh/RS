package net.botwithus

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.skills.Skills
import net.botwithus.rs3.game.skills.Skill
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.script.LoopingScript
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.script.config.ScriptConfig


class Zezimax(
    name: String,
    scriptConfig: ScriptConfig,
    scriptDefinition: ScriptDefinition
) : LoopingScript(name, scriptConfig, scriptDefinition) {

    enum class ZezimaxBotState {
        IDLE,
        MINING,
        NAVIGATING
    }

    var botState: ZezimaxBotState = ZezimaxBotState.IDLE
    var someBoolean: Boolean = true

    override fun initialize(): Boolean {
        super.initialize()
        this.sgc = ZezimaxGraphicsContext(this, console)
        botState = ZezimaxBotState.IDLE
        println("Script initialized. State set to IDLE.")
        return true
    }

    override fun onLoop() {
        val player = Client.getLocalPlayer()
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null) {
            println("Player not logged in. Delaying execution.")
            Execution.delay(Navi.random.nextLong(2500, 7500))
            return
        }

        when (botState) {
            ZezimaxBotState.IDLE -> {
                val miningLevel = Skills.MINING.level // Assuming this is the correct method to get the current mining level
                if (miningLevel >= 60) {
                    println("Mining at the Falador Miners Guild. Mining level: $miningLevel" )
                    botState = ZezimaxBotState.MINING
                } else {
                    println("Mining level too low: $miningLevel")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                }
            }
            ZezimaxBotState.NAVIGATING -> {
                println("State: NAVIGATING. Delaying execution.")
                Execution.delay(Navi.random.nextLong(1500, 3000))
            }
            ZezimaxBotState.MINING -> {
                Mining().mine(player)
            }
        }
    }

    inner class Mining {
        fun mine(player: LocalPlayer) {
            if (!Backpack.isFull()) {
                if (!Navi.walkToMiningGuild()) {
                    println("Walking to mining guild.")
                    botState = ZezimaxBotState.NAVIGATING
                    return
                }
                // detect the rocks on entering mining guild
                // Add code to mine rune rocks here
                // if inventory full go to smith bank
                println("Mining rune rocks.")
                Execution.delay(Navi.random.nextLong(4000, 13000)) // Simulate mining delay
            } else {
                if (!Navi.walkToFaladorSmithBank()) {
                    println("Walking to Falador smith bank.")
                    botState = ZezimaxBotState.NAVIGATING
                    return
                }
                // Add code to bank the mined ore here
                // repeat from inner class mining
                println("Banking mined ore.")
                Execution.delay(Navi.random.nextLong(2000, 5000)) // Simulate banking delay
            }
        }
    }
}
