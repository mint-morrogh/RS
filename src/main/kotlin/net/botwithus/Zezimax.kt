package net.botwithus

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.Coordinate
import net.botwithus.rs3.game.Area
import net.botwithus.rs3.game.movement.Movement
import net.botwithus.rs3.game.movement.NavPath
import net.botwithus.rs3.game.movement.TraverseEvent
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
    var someBoolean: Boolean = true // Ensure this is declared as var

    enum class BotState {
        IDLE,
        WALKING_TO_FALADOR,
    }

    // Define the area for the center of Falador
    private val faladorCenter = Area.Rectangular(
        Coordinate(2961, 3376, 0), // Top-left corner
        Coordinate(2971, 3386, 0)  // Bottom-right corner
    )

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
        println("Current Player Coordinates: ${player.coordinate}")

        // If the player is within the target area, switch to IDLE state
        if (faladorCenter.contains(player.coordinate)) {
            println("Reached Falador center.")
            botState = BotState.IDLE
            return
        }

        // Find a path to a random walkable coordinate within the target area
        val targetCoord = faladorCenter.randomWalkableCoordinate
        val path = NavPath.resolve(targetCoord)

        if (path == null) {
            println("Failed to find path to Falador center.")
            return
        }

        println("Navigating to Falador Center Coordinates: $targetCoord")

        val results = Movement.traverse(path)
        if (results == TraverseEvent.State.NO_PATH) {
            println("Failed to traverse path to Falador center.")
        } else {
            println("Traversing path to Falador center.")
        }

        val delay = random.nextLong(3000, 13000) // Random delay between 3 to 13 seconds
        println("Delaying for $delay milliseconds")
        Execution.delay(delay)

        // Check if the player has moved significantly closer to the target
        val newCoord = player.coordinate
        if (faladorCenter.contains(newCoord)) {
            println("Moved significantly closer to target.")
        } else {
            println("Failed to move significantly closer to target. Current Coordinates: $newCoord")
        }
    }
}
