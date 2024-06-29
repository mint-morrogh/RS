package net.botwithus

import net.botwithus.rs3.game.Coordinate
import net.botwithus.rs3.game.Area
import net.botwithus.rs3.game.movement.Movement
import net.botwithus.rs3.game.movement.NavPath
import net.botwithus.rs3.game.movement.TraverseEvent
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.game.Client
import java.util.*

// Define Bot States for navigation
enum class BotState {
    STANDING,
    WALKING
}

object Navi {
    val random: Random = Random()
    var botState: BotState = BotState.STANDING

    // Define navigation areas here
    //////////////////////////////////
    val faladorCenter = Area.Rectangular(
        Coordinate(2961, 3376, 0), // Bottom-left corner
        Coordinate(2971, 3386, 0)  // Top-right corner
    )
    val faladorWestBank = Area.Rectangular(
        Coordinate(2945, 3368, 0),
        Coordinate(2949, 3368, 0)
    )
    val faladorEastBank = Area.Rectangular(
        Coordinate(3010, 3355, 0),
        Coordinate(3015, 3355, 0)
    )
    val faladorSouthBank = Area.Rectangular(
        Coordinate(2956, 3296, 0),
        Coordinate(2956, 3297, 0)
    )
    val faladorSmithBank = Area.Rectangular(
        Coordinate(3060, 3339, 0),
        Coordinate(3060, 3340, 0)
    )
    val grandexchange = Area.Rectangular(
        Coordinate(3163, 3484, 0),
        Coordinate(3166, 3484, 0)
    )
    val grandexchangeEntrance = Area.Rectangular(
        Coordinate(3163, 3452, 0),
        Coordinate(3169, 3458, 0)
    )
    val varrockWestBank = Area.Rectangular(
        Coordinate(3189, 3435, 0),
        Coordinate(3189, 3443, 0)
    )
    val varrockEastBank = Area.Rectangular(
        Coordinate(3251, 3420, 0),
        Coordinate(3256, 3420, 0)
    )
    val varrockEastMine = Area.Rectangular(
        Coordinate(3287, 3364, 0),
        Coordinate(3290, 3367, 0)
    )
    val varrockWestMine = Area.Rectangular(
        Coordinate(3181, 3371, 0),
        Coordinate(3184, 3375, 0)
    )
    val lumbridgeTopFloorBank = Area.Rectangular(
        Coordinate(3208, 3219, 2),
        Coordinate(3208, 3220, 2)
    )
    val alkharidWestBank = Area.Rectangular(
        Coordinate(3268, 3168, 0),
        Coordinate(3272, 3168, 0)
    )
    val draynorBank = Area.Rectangular(
        Coordinate(3092, 3241, 0),
        Coordinate(3092, 3245, 0)
    )
    val edgevilleBank = Area.Rectangular(
        Coordinate(3094, 3489, 0),
        Coordinate(3094, 3493, 0)
    )
    val burthorpeBank = Area.Rectangular(
        Coordinate(2888, 3535, 0),
        Coordinate(2887, 3536, 0)
    )
    val taverlyBank = Area.Rectangular(
        Coordinate(2875, 3416, 0),
        Coordinate(2875, 3418, 0)
    )
    val faladorMiningGuild = Area.Rectangular(
        Coordinate(3022, 9737, 0),
        Coordinate(3023, 9740, 0)
    )
    val faladorLuminite = Area.Rectangular(
        Coordinate(3038, 9761, 0),
        Coordinate(3040, 9764, 0)
    )
    val faladorSmithingFurnace = Area.Rectangular(
        Coordinate(3042, 3337, 0),
        Coordinate(3047, 3338, 0)
    )
    //////////////////////////////////

    // Finding closest bank
    private val bankAreas = listOf(
        faladorWestBank,
        faladorEastBank,
        faladorSouthBank,
        faladorSmithBank,
        // grandexchange, to avoid getting stuck in agility, only if members should this be valid
        varrockWestBank,
        varrockEastBank,
        lumbridgeTopFloorBank,
        alkharidWestBank,
        draynorBank,
        edgevilleBank,
        burthorpeBank,
        taverlyBank
    )

    fun getNearestBank(playerCoord: Coordinate): Area.Rectangular? {
        return bankAreas.minByOrNull { it.center().distanceTo(playerCoord) }
    }

    private fun Area.Rectangular.center(): Coordinate {
        val centerX = (topLeft.x + bottomRight.x) / 2
        val centerY = (topLeft.y + bottomRight.y) / 2
        return Coordinate(centerX, centerY, topLeft.z)
    }

    private fun Coordinate.distanceTo(other: Coordinate): Double {
        return Math.sqrt(Math.pow((this.x - other.x).toDouble(), 2.0) + Math.pow((this.y - other.y).toDouble(), 2.0))
    }




    // Define a utility function for walking to an area
    fun walkTo(area: Area.Rectangular): Boolean {
        val player = Client.getLocalPlayer() ?: return false
        if (area.contains(player.coordinate)) {
            botState = BotState.STANDING
            return true
        }

        if (player.animationId != -1) {
            // Walk to current player coordinates to break the animation
            val currentCoord = player.coordinate
            Zezimax.Logger.log("**TRAVELLING** Player is animating. Breaking animation...")

            // Walk to the current tile without using the minimap
            Movement.walkTo(currentCoord.x, currentCoord.y, false)
            Execution.delay(random.nextLong(1000, 2000))
        }

        val targetCoord = area.randomWalkableCoordinate
        val path = NavPath.resolve(targetCoord)

        if (path == null) {
            Zezimax.Logger.log("Failed to find path to target area.")
            return false
        }

        val results = Movement.traverse(path)
        if (results == TraverseEvent.State.NO_PATH) {
            Zezimax.Logger.log("Failed to traverse path to target area.")
            return false
        }

        val delay = random.nextLong(300, 1200) // Random delay
        Zezimax.Logger.log("**TRAVELLING** Delaying next input for $delay milliseconds")
        Execution.delay(delay)

        return area.contains(player.coordinate)
    }




    // Wrapper functions for walking to specific areas
    fun walkToFaladorCenter(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorCenter)
    }

    fun walkToFaladorWestBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorWestBank)
    }

    fun walkToFaladorEastBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorEastBank)
    }

    fun walkToFaladorSouthBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorSouthBank)
    }

    fun walkToVarrockWestBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(varrockWestBank)
    }

    fun walkToVarrockEastBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(varrockEastBank)
    }

    fun walkToVarrockEastMine(): Boolean {
        botState = BotState.WALKING
        return walkTo(varrockEastMine)
    }

    fun walkToVarrockWestMine(): Boolean {
        botState = BotState.WALKING
        return walkTo(varrockWestMine)
    }

    fun walkToDraynorBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(draynorBank)
    }

    fun walkToEdgevilleBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(edgevilleBank)
    }

    fun walkToBurthorpeBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(burthorpeBank)
    }

    fun walkToTaverlyBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(taverlyBank)
    }

    fun walkToAlkharidWestBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(alkharidWestBank)
    }

    fun walkToLumbridgeTopFloorBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(lumbridgeTopFloorBank)
    }

    fun walkToGrandExchange(): Boolean {
        botState = BotState.WALKING
        return walkTo(grandexchange)
    }

    fun walkToGrandExchangeEntrance(): Boolean {
        botState = BotState.WALKING
        return walkTo(grandexchangeEntrance)
    }

    fun walkToMiningGuild(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorMiningGuild)
    }

    fun walkToFaladorSmithBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorSmithBank)
    }
    fun walkToFaladorLuminite(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorLuminite)
    }
    fun walkToFaladorSmithingFurnace(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorSmithingFurnace)
    }
}