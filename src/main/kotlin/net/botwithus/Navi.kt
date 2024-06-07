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
        Coordinate(2943, 3369, 0),
        Coordinate(2947, 3373, 0)
    )
    val faladorEastBank = Area.Rectangular(
        Coordinate(3009, 3356, 0),
        Coordinate(3016, 3357, 0)
    )
    val faladorSmithBank = Area.Rectangular(
        Coordinate(3058, 3337, 0),
        Coordinate(3060, 3341, 0)
    )
    val grandexchange = Area.Rectangular(
        Coordinate(3162, 3481, 0),
        Coordinate(3167, 3484, 0)
    )
    val varrockWestBank = Area.Rectangular(
        Coordinate(3183, 3435, 0),
        Coordinate(3188, 3444, 0)
    )
    val varrockEastBank = Area.Rectangular(
        Coordinate(3250, 3420, 0),
        Coordinate(3257, 3423, 0)
    )
    val lumbridgeTopFloopBank = Area.Rectangular(
        Coordinate(3207, 3220, 2),
        Coordinate(3210, 3222, 2)
    )
    val alkharidWestBank = Area.Rectangular(
        Coordinate(3269, 3165, 0),
        Coordinate(3272, 3170, 0)
    )
    val draynorBank = Area.Rectangular(
        Coordinate(3090, 3240, 0),
        Coordinate(3095, 3246, 0)
    )
    val edgevilleBank = Area.Rectangular(
        Coordinate(3091, 3490, 0),
        Coordinate(3094, 3499, 0)
    )
    val faladorMiningGuild = Area.Rectangular(
        Coordinate(3029, 9733, 0),
        Coordinate(3051, 9747, 0)
    )
    //////////////////////////////////


    // Define a utility function for walking to an area
    fun walkTo(area: Area.Rectangular): Boolean {
        val player = Client.getLocalPlayer() ?: return false
        if (area.contains(player.coordinate)) {
            botState = BotState.STANDING
            return true
        }

        val targetCoord = area.randomWalkableCoordinate
        val path = NavPath.resolve(targetCoord)

        if (path == null) {
            println("Failed to find path to target area.")
            return false
        }

        val results = Movement.traverse(path)
        if (results == TraverseEvent.State.NO_PATH) {
            println("Failed to traverse path to target area.")
            return false
        }

        val delay = random.nextLong(1500, 4000) // Random delay between 1.5 to 4 seconds
        println("Delaying for $delay milliseconds")
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

    fun walkToFaladorSmithBank(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorSmithBank)
    }

    fun walkToMiningGuild(): Boolean {
        botState = BotState.WALKING
        return walkTo(faladorMiningGuild)
    }
}