package net.botwithus

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.script.LoopingScript
import net.botwithus.rs3.script.config.ScriptConfig
import java.util.*

class Zezimax(
    name: String,
    scriptConfig: ScriptConfig,
    scriptDefinition: ScriptDefinition
) : LoopingScript(name, scriptConfig, scriptDefinition) {

    private val random: Random = Random()
    var botState: BotState = BotState.IDLE
    var someBoolean: Boolean = true

    enum class BotState {
        IDLE,
        WALKING_TO_FALADOR,
    }

    override fun initialize(): Boolean {
        super.initialize()
        this.sgc = ZezimaxGraphicsContext(this, console)
        botState = BotState.WALKING_TO_FALADOR
        println("Script initialized. State set to WALKING_TO_FALADOR.")
        return true
    }

    override fun onLoop() {
        val player = Client.getLocalPlayer()
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null || botState == BotState.IDLE) {
            println("Player not logged in or state is IDLE. Delaying execution.")
            Execution.delay(random.nextLong(2500, 5500))
            return
        }
        when (botState) {
            BotState.WALKING_TO_FALADOR -> {
                println("State: WALKING_TO_FALADOR. Initiating walk to Falador.")
                if (walkToArea(faladorCenter, random)) {
                    println("Reached Falador center.")
                    botState = BotState.IDLE
                } else {
                    println("Continuing to Falador center.")
                }
                return
            }
            BotState.IDLE -> {
                println("State: IDLE. Delaying execution.")
                Execution.delay(random.nextLong(1500, 5000))
            }
        }
    }
}
