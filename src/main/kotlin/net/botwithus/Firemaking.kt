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
import net.botwithus.rs3.game.queries.builders.characters.PlayerQuery
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.queries.results.ResultSet
import net.botwithus.rs3.game.scene.entities.animation.SpotAnimation
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.game.scene.entities.characters.player.Player
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


val logID = DecisionTree.logToBurn
val firemakingGetName = Utilities.getNameById(DecisionTree.logToBurn)

fun startFiremaking() {
    Zezimax.botState = Zezimax.ZezimaxBotState.START_FIREMAKING
}

fun hasItemInInventory(itemId: Int): Boolean {
    val results = InventoryItemQuery.newQuery(93).ids(logID).results()
    val item = results.firstOrNull()
    return (item?.stackSize ?: 0) > 0
}

fun firemaking() {

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


    if (Bank.isOpen()) {
        if (!Backpack.isEmpty()) {
            Execution.delay(Navi.random.nextLong(1000, 2000))
            Bank.depositAll()
            Execution.delay(Navi.random.nextLong(1000, 2000))
        }
        Execution.delay(Navi.random.nextLong(1000, 2000))
        // Get the count of each type of log in the bank
        if (Utilities.isItemIdInBank(logID)) {
            val logCount = Bank.getItems().filter { it.id == DecisionTree.logToBurn }.sumOf { it.stackSize }
            Zezimax.Logger.log("Logs left in bank: $logCount")
            Zezimax.Logger.log("Withdrawing all $firemakingGetName...")
            Bank.withdrawAll(DecisionTree.logToBurn)
            Execution.delay(Navi.random.nextLong(1000, 2000))
        } else {
            Zezimax.Logger.log("Out of logs, Re-Initializing...")
            Execution.delay(Navi.random.nextLong(1000, 2000))
            Zezimax.botState = ZezimaxBotState.INITIALIZING
            return

        }
    }


    Navi.walkToFaladorSouthFiremaking()
    Execution.delay(Navi.random.nextLong(700, 1400))

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
        Zezimax.Logger.log("Attempting to light $firemakingGetName")
        Execution.delay(Navi.random.nextLong(400, 800))

        // Wait until the player leaves the initial position
        Execution.delayUntil(15000) {
            Client.getLocalPlayer()?.coordinate != initialPosition
        }
        if (Client.getLocalPlayer()?.coordinate != initialPosition) {
            Zezimax.Logger.log("Bonfire lit...")
            Execution.delay(Navi.random.nextLong(1300, 3000))

            // Interact with "Craft" option if needed, or back to 'Craft' option
            val logComponentCraft = ComponentQuery.newQuery(1473)
                .componentIndex(5)
                .item(logID)
                .option("Craft")
                .results()
                .firstOrNull()
            val craftMenu = ComponentQuery.newQuery(1371)
                .componentIndex(22)
                .results()
                .firstOrNull()
            val selectToolButton = ComponentQuery.newQuery(1371)
                .componentIndex(14)
                .results()
                .firstOrNull()
            if (logComponentCraft != null && logComponentCraft.interact("Craft")) {
                Zezimax.Logger.log("Adding Bonfire fuel...")
                Execution.delay(Navi.random.nextLong(2000, 4000))
                if (Interfaces.isOpen(1371)) {
                    if (craftMenu != null) {
                        Zezimax.Logger.log("Backing out of Fletching menu...")
                        selectToolButton?.interact("Select")
                        Execution.delay(Navi.random.nextLong(1900, 3000))
                    }
                }
                if (Interfaces.isOpen(1179)) {
                    Zezimax.Logger.log("Craft Menu found...")
                    val woodMenu = ComponentQuery.newQuery(1179)
                        .componentIndex(27)
                        .results()
                        .firstOrNull()

                    if (woodMenu != null && woodMenu.interact()) {
                        Execution.delay(Navi.random.nextLong(1200, 2500))
                        Zezimax.Logger.log("Creating Bonfire...")

                        // While loop to check if there are logs in the inventory and if fire exists nearby
                        while (true) {
                            val hasLogs = hasItemInInventory(logID)
                            val currentPosition = player.coordinate
                            val fireNearby = SceneObjectQuery.newQuery().name("Fire").results().nearestTo(currentPosition)
                                ?.coordinate?.let { it.distanceTo(currentPosition) <= 1 } == true
                            val fireSpiritNearby = NpcQuery.newQuery().name("Fire spirit").results().nearestTo(currentPosition)
                                ?.coordinate?.let { it.distanceTo(currentPosition) <= 2 } == true

                            if (fireSpiritNearby) {
                                val fireSpirit = NpcQuery.newQuery().name("Fire spirit").results().nearestTo(currentPosition)
                                Execution.delay(Navi.random.nextLong(5000, 9000))
                                fireSpirit?.interact("Collect reward")
                                Zezimax.Logger.log("Collected reward from Fire spirit.")
                                Execution.delay(Navi.random.nextLong(2000, 3000))
                            }
                            if (fireNearby && hasLogs) {
                                Zezimax.Logger.log("Bonfire is still active and $firemakingGetName are available.")
                                Execution.delay(Navi.random.nextLong(10000, 12000))
                            } else if (!fireNearby && hasLogs) {
                                Zezimax.Logger.log("Bonfire went out.")
                                Execution.delay(Navi.random.nextLong(2000, 10000))
                                Zezimax.botState = ZezimaxBotState.FIREMAKING_BANKING
                                return
                            } else if (!hasLogs) {
                                Zezimax.Logger.log("No more $firemakingGetName in inventory. Returning to bank...")
                                Execution.delay(Navi.random.nextLong(2000, 10000))
                                Zezimax.botState = ZezimaxBotState.FIREMAKING_BANKING
                                return
                            }
                        }

                    }
                } else {
                    Zezimax.Logger.log("Craft Menu not found...")
                    Zezimax.botState = ZezimaxBotState.FIREMAKING_BANKING
                    return
                }
            }
        } else {
            Zezimax.Logger.log("Failed to move from initial position")
        }
    } else {
        Zezimax.Logger.log("Failed to interact with the log")
    }
}




