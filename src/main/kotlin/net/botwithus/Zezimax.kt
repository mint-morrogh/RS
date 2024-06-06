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
import net.botwithus.rs3.game.movement.Movement
import net.botwithus.rs3.game.Coordinate
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
                walkToFalador(player)
                return
            }
            BotState.IDLE -> {
                println("State: IDLE. Delaying execution.")
                Execution.delay(random.nextLong(1500, 5000))
            }
        }
    }

    private fun walkToFalador(player: LocalPlayer) {
        val faladorCenter = Coordinate(2965, 3380, 0) // Global coordinates of the center of Falador
        println("Current Player Coordinates: ${player.coordinate}")

        val maxStepDistance = 20 // Maximum step distance in tiles

        val currentCoord = player.coordinate

        // Calculate the distance to the target
        val distanceX = faladorCenter.x - currentCoord.x
        val distanceY = faladorCenter.y - currentCoord.y

        // Calculate the next step coordinates
        val stepX = if (Math.abs(distanceX) > maxStepDistance) {
            currentCoord.x + (if (distanceX > 0) maxStepDistance else -maxStepDistance)
        } else {
            faladorCenter.x
        }

        val stepY = if (Math.abs(distanceY) > maxStepDistance) {
            currentCoord.y + (if (distanceY > 0) maxStepDistance else -maxStepDistance)
        } else {
            faladorCenter.y
        }

        println("Next Step Coordinates: StepX = $stepX, StepY = $stepY")

        try {
            Movement.walkTo(stepX, stepY, true) // Using minimap for longer steps
            println("Invoked Movement.walkTo with coordinates: ($stepX, $stepY)")
        } catch (e: Exception) {
            println("Error invoking Movement.walkTo: ${e.message}")
        }

        val delay = random.nextLong(1000, 4000) // Random delay between 1 to 4 seconds
        println("Delaying for $delay milliseconds")
        Execution.delay(delay)

        // Check if the player has moved
        if (player.coordinate.x == stepX && player.coordinate.y == stepY) {
            println("Moved to next step.")
        } else {
            println("Failed to move to next step. Current Coordinates: ${player.coordinate}")
        }

        // Check if the player reached the target
        if (player.coordinate.x == faladorCenter.x && player.coordinate.y == faladorCenter.y) {
            println("Reached Falador center.")
            botState = BotState.IDLE
        } else {
            println("Continuing to Falador center.")
        }
    }
}
