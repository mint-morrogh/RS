package net.botwithus

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.hud.interfaces.Interfaces
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
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
        // Define your bot states here
        IDLE,
        SKILLING,
        BANKING,
        // etc..
    }

    override fun initialize(): Boolean {
        super.initialize()
        // Set the script graphics context to our custom one
        this.sgc = ZezimaxGraphicsContext(this, console)
        return true
    }

    override fun onLoop() {
        val player = Client.getLocalPlayer()
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null || botState == BotState.IDLE) {
            Execution.delay(random.nextLong(2500, 5500))
            return
        }
        when (botState) {
            BotState.SKILLING -> {
                Execution.delay(handleSkilling(player))
                return
            }
            BotState.BANKING -> {
                // Execution.delay(handleBanking(player))
                return
            }
            BotState.IDLE -> {
                println("We're idle!")
                Execution.delay(random.nextLong(1500, 5000))
            }
        }
    }

    private fun handleSkilling(player: LocalPlayer): Long {
        // For example, if skilling progress interface is open, return a randomized value to keep waiting.
        if (Interfaces.isOpen(1251))
            return random.nextLong(250, 1500)

        // Check the player's adrenaline
        if (player.adrenaline < 500) {
            println("Player has less than 50% adrenaline")
        }

        // If our inventory is full, let's bank.
        if (Backpack.isFull()) {
            println("Going to banking state!")
            botState = BotState.BANKING
            return random.nextLong(250, 1500)
        }

        // Click my tree, mine my rock, etc...
        val tree: SceneObject? = SceneObjectQuery.newQuery().name("Tree").option("Chop").results().nearest()
        tree?.let {
            println("Interacted with tree: ${it.interact("Chop")}")
        }

        return random.nextLong(1500, 3000)
    }

}
