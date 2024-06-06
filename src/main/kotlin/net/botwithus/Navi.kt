package net.botwithus

import net.botwithus.rs3.game.Coordinate
import net.botwithus.rs3.game.Area
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.movement.Movement
import net.botwithus.rs3.game.movement.NavPath
import net.botwithus.rs3.game.movement.TraverseEvent
import net.botwithus.rs3.script.Execution
import java.util.*

// Define navigation areas here
//////////////////////////////////
val faladorCenter = Area.Rectangular(
    Coordinate(2961, 3376, 0), // Top-left corner
    Coordinate(2971, 3386, 0)  // Bottom-right corner
)

val faladorSouthBank = Area.Rectangular(
    Coordinate(2945, 3368, 0), // Example coordinates
    Coordinate(2950, 3373, 0)
)
//////////////////////////////////




// Define a utility function for walking to an area
fun walkToArea(area: Area.Rectangular, random: Random): Boolean {
    val player = Client.getLocalPlayer() ?: return false
    if (area.contains(player.coordinate)) {
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

    val delay = random.nextLong(3000, 8000) // Random delay between 3 to 13 seconds
    println("Delaying for $delay milliseconds")
    Execution.delay(delay)

    return area.contains(player.coordinate)
}
