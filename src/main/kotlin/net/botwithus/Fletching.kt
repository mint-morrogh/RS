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


val logsForFletcing = Bank.getItems().filter { it.id == DecisionTree.logToFletch }.sumOf { it.stackSize }
val fletchID = DecisionTree.logToFletch

fun startFletching() {
Zezimax.botState = Zezimax.ZezimaxBotState.START_FLETCHING
}

fun fletching() {

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
    if (logsForFletcing > 0) {
        Zezimax.Logger.log("Withdrawing all $fletchID...")
        Bank.withdrawAll(fletchID)
        Execution.delay(Navi.random.nextLong(1000, 2000))
        Bank.close()
        Execution.delay(Navi.random.nextLong(1000, 2000))
    } else {
        Zezimax.Logger.log("Out of logs, Re-Initializing...")
        Bank.close()
        Execution.delay(Navi.random.nextLong(1000, 2000))
        Zezimax.botState = ZezimaxBotState.INITIALIZING
        return
    }
}



Execution.delay(Navi.random.nextLong(700, 1400))

// Find the first log in the inventory and interact with it
val logComponent = ComponentQuery.newQuery(1473)
    .componentIndex(5)
    .item(fletchID)
    .option("Craft")
    .results()
    .firstOrNull()
if (logComponent != null && logComponent.interact("Craft")) {
    Execution.delay(Navi.random.nextLong(400, 800))

    // Interact with "Craft" option if needed
    val logComponentCraft = ComponentQuery.newQuery(1473)
        .componentIndex(5)
        .item(fletchID)
        .option("Craft")
        .results()
        .firstOrNull()
    if (logComponentCraft != null && logComponentCraft.interact("Craft")) {
        Zezimax.Logger.log("Attempting to Fletch...")
        Execution.delay(Navi.random.nextLong(2000, 4000))
        if (Interfaces.isOpen(1179) || Interfaces.isOpen(1370)) {
            Zezimax.Logger.log("Craft Menu found...")
            val fletchMenu = ComponentQuery.newQuery(1179)
                .componentIndex(17)
                .results()
                .firstOrNull()
            val fletchButton = ComponentQuery.newQuery(1370)
                .componentIndex(30)
                .results()
                .firstOrNull()

            if (fletchMenu != null && fletchMenu.interact()) {
                Execution.delay(Navi.random.nextLong(1200, 2500))
                Zezimax.Logger.log("Selecting bow type...")
            }
            if (fletchButton != null) {
                fletchButton.interact()
                Execution.delay(Navi.random.nextLong(1200, 2500))
            }

                // While loop to check if there are logs in the inventory and if fire exists nearby
                while (true) {
                    val hasLogs = !InventoryItemQuery.newQuery().ids(logID).results().isEmpty

                    while (!ComponentQuery.newQuery(1251).results().isEmpty) {
                        Zezimax.Logger.log("Still fletching...")
                        Execution.delay(Navi.random.nextLong(2000, 6400))
                    }

                    if (!hasLogs) {
                        Zezimax.Logger.log("No more logs in inventory. Ending fletching session.")
                        Execution.delay(Navi.random.nextLong(2000, 4000))
                        Zezimax.botState = ZezimaxBotState.FLETCHING_BANKING
                        return
                    }
                }
            }
        }
    }
}





