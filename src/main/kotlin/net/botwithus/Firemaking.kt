package net.botwithus

import net.botwithus.Zezimax.ZezimaxBotState
import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.events.impl.InventoryUpdateEvent
import net.botwithus.rs3.events.impl.SkillUpdateEvent
import net.botwithus.rs3.game.Area
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.Coordinate
import net.botwithus.rs3.game.hud.interfaces.Interfaces
import net.botwithus.rs3.game.minimenu.MiniMenu
import net.botwithus.rs3.game.minimenu.actions.SelectableAction
import net.botwithus.rs3.game.movement.Movement
import net.botwithus.rs3.game.movement.NavPath
import net.botwithus.rs3.game.movement.TraverseEvent
import net.botwithus.rs3.game.queries.builders.animations.SpotAnimationQuery
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.animation.SpotAnimation
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.game.skills.Skills
import net.botwithus.rs3.game.vars.VarManager
import net.botwithus.rs3.imgui.NativeInteger
import net.botwithus.rs3.input.GameInput
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.script.LoopingScript
import net.botwithus.rs3.script.ScriptConsole
import net.botwithus.rs3.script.config.ScriptConfig
import net.botwithus.rs3.util.Regex
import java.util.*
import java.util.concurrent.Callable
import java.util.regex.Pattern


val willowCount = Bank.getItems().filter { it.name == "Willow logs" }.sumOf { it.stackSize }
val oakCount = Bank.getItems().filter { it.name == "Oak logs" }.sumOf { it.stackSize }
val logCount = Bank.getItems().filter { it.name == "Logs" }.sumOf { it.stackSize }
var logID: Int = 0


fun startFiremaking() {
    Zezimax.botState = Zezimax.ZezimaxBotState.START_FIREMAKING
}

fun firemaking() {
    val firemakingLevel = Skills.FIREMAKING.level

    val player = Client.getLocalPlayer()
    if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN) {
        Zezimax.Logger.log("Player not logged in. Delaying execution.")
        Execution.delay(Navi.random.nextLong(2500, 7500))
        return
    }
    val nearestBank = Navi.getNearestBank(player.coordinate)
    if (nearestBank == null) {
        Zezimax.Logger.log("No nearby bank found. Delaying execution.")
        Execution.delay(Navi.random.nextLong(2500, 7500))
        return
    }
    if (!Bank.isOpen()) {
        if (!nearestBank.contains(player.coordinate)) {
            Zezimax.Logger.log("Walking to the nearest bank...")
            Movement.traverse(NavPath.resolve(nearestBank.randomWalkableCoordinate))
            Execution.delay(Navi.random.nextLong(1000, 3000))
        }
        Bank.open()
        Execution.delay(Navi.random.nextLong(1000, 2000))
    }

    var logID: Int = 0


    if (Bank.isOpen()) {
        // Get the count of each type of log in the bank
        val willowCount = Bank.getItems().filter { it.id == 1519 }.sumOf { it.stackSize }
        val oakCount = Bank.getItems().filter { it.id == 1521 }.sumOf { it.stackSize }
        val logCount = Bank.getItems().filter { it.id == 1511 }.sumOf { it.stackSize }

        if (firemakingLevel >= 30 && willowCount >= 100) {
            Bank.withdrawAll(1519) // withdraw Willows
            logID = 1519
            Execution.delay(Navi.random.nextLong(1000, 2000))
        } else if (firemakingLevel >= 15 && oakCount >= 100) {
            Bank.withdrawAll(1521) // withdraw Oaks
            logID = 1521
            Execution.delay(Navi.random.nextLong(1000, 2000))
        } else if (logCount >= 100) {
            Bank.withdrawAll(1511) // withdraw Logs
            logID = 1511
            Execution.delay(Navi.random.nextLong(1000, 2000))
        } else {
            Zezimax.Logger.log("Not enough Logs to burn, Reinitializing...")
            Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING
        }
    }

    Navi.walkToFaladorSouthFiremaking()
    Execution.delay(Navi.random.nextLong(700, 1400))

    if (logID != 0) {
        val initialPosition = player.coordinate
        Execution.delay(Navi.random.nextLong(200, 800))

        // Find the first log in the inventory and interact with it
        val logComponent = ComponentQuery.newQuery(1473)
            .componentIndex(5)
            .item(logID)
            .option("Light")
            .results()
            .firstOrNull()
        if (logComponent != null && logComponent.interact("Light")) {
            Zezimax.Logger.log("Attempting to light the log")
            Execution.delay(Navi.random.nextLong(400, 800))

            // Wait until the player leaves the initial position
            Execution.delayUntil(15000) {
                Client.getLocalPlayer()?.coordinate != initialPosition
            }
            if (Client.getLocalPlayer()?.coordinate != initialPosition) {
                Zezimax.Logger.log("Bonfire lit...")
                Execution.delay(Navi.random.nextLong(2000, 4000))

                // Interact with "Craft" option if needed
                val logComponentCraft = ComponentQuery.newQuery(1473)
                    .componentIndex(5)
                    .item(logID)
                    .option("Craft")
                    .results()
                    .firstOrNull()
                if (logComponentCraft != null && logComponentCraft.interact("Craft")) {
                    Zezimax.Logger.log("Adding Bonfire fuel...")
                    Execution.delay(Navi.random.nextLong(2000, 4000))
                    if (Interfaces.isOpen(1179)) {
                        Zezimax.Logger.log("Craft Menu found...")
                        val woodMenu = ComponentQuery.newQuery(1179)
                            .componentIndex(27)
                            .results()
                            .firstOrNull()

                        if (woodMenu != null && woodMenu.interact()) {
                            Execution.delay(Navi.random.nextLong(2000, 4000))
                            Zezimax.Logger.log("Creating Bonfire...")
                            // While loop to check if there are logs in the inventory and if fire exists nearby
                            while (true) {
                                val fireNearby = SceneObjectQuery.newQuery().name("Fire").results().nearestTo(initialPosition)
                                    ?.coordinate?.let { it.distanceTo(initialPosition) <= 1 } == true

                                if (fireNearby && !Backpack.isEmpty()) {
                                    Zezimax.Logger.log("Bonfire is still active and logs are available.")
                                    Execution.delay(Navi.random.nextLong(10000, 12000)) // Wait 10 seconds
                                } else if (!fireNearby && !Backpack.isEmpty()) {
                                    Zezimax.Logger.log("Bonfire went out.")
                                    Execution.delay(Navi.random.nextLong(2000, 10000))
                                    Zezimax.botState = ZezimaxBotState.FIREMAKING_BANKING
                                    return
                                } else if (Backpack.isEmpty()) {
                                    Zezimax.Logger.log("Backpack is empty. Ending bonfire session.")
                                    Execution.delay(Navi.random.nextLong(2000, 10000))
                                    Zezimax.botState = ZezimaxBotState.FIREMAKING_BANKING
                                    return
                                }
                            }
                        }
                    } else {
                        Zezimax.Logger.log("Craft Menu not found...")
                    }
                }
            } else {
                Zezimax.Logger.log("Failed to move from initial position")
            }
        } else {
            Zezimax.Logger.log("Failed to interact with the log")
        }
    } else {
        Zezimax.Logger.log("No logs were withdrawn from the bank")
    }
}




