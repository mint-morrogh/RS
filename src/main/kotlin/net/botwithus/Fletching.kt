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


val fletchID = DecisionTree.logToFletch
val logsForFletching = Bank.getItems().any { it.id == fletchID }
val fletchingGetName = Utilities.getNameById(DecisionTree.logToFletch)


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
        if (logsForFletching) {
            Zezimax.Logger.log("Withdrawing all $fletchingGetName...")
            Bank.withdrawAll(fletchID)
            Execution.delay(Navi.random.nextLong(1000, 2000))
            Bank.close()
            Execution.delay(Navi.random.nextLong(1000, 2000))
        } else {
            Zezimax.Logger.log("Out of $fletchingGetName, Re-Initializing...")
            Bank.close()
            Execution.delay(Navi.random.nextLong(1000, 2000))
            Zezimax.botState = ZezimaxBotState.INITIALIZING
            return
        }
    }



    Execution.delay(Navi.random.nextLong(700, 1400))

    // Find the first log in the inventory and interact with it
    val logComponentCraft = ComponentQuery.newQuery(1473)
        .componentIndex(5)
        .item(fletchID)
        .option("Craft")
        .results()
        .firstOrNull()
    if (logComponentCraft != null && logComponentCraft.interact("Craft")) {
        Zezimax.Logger.log("Attempting to Fletch $fletchingGetName...")
        Execution.delay(Navi.random.nextLong(2000, 4000))
        if (Interfaces.isOpen(1179) || Interfaces.isOpen(1370) || Interfaces.isOpen(1371)) {
            Zezimax.Logger.log("Craft Menu found...")
            val fletchMenu = ComponentQuery.newQuery(1179)
                .componentIndex(17)
                .option("Select")
                .results()
                .firstOrNull()
            val craftMenu = ComponentQuery.newQuery(1371)
                .componentIndex(22)
                .results()
                .firstOrNull()
            val fletchButton = ComponentQuery.newQuery(1370)
                .componentIndex(30)
                .results()
                .firstOrNull()

            if (fletchMenu != null) {
                if (Interfaces.isOpen(1179) && fletchMenu.interact()) {
                    Zezimax.Logger.log("Selecting knife...")
                    Execution.delay(Navi.random.nextLong(2200, 3500))
                }
            }
            if (craftMenu != null) {
                if (Interfaces.isOpen(1371)) {


                    val shortbow = ComponentQuery.newQuery(1371)
                        .componentIndex(22)
                        .subComponentIndex(5)
                        .results()
                        .firstOrNull()

                    Zezimax.Logger.log("Selecting Shortbow...")
                    shortbow?.interact("Select")
                    Execution.delay(Navi.random.nextLong(2200, 3500))
                }
            }
            if (fletchButton != null) {
                if (Interfaces.isOpen(1370) && fletchButton.interact()) {
                    Execution.delay(Navi.random.nextLong(2200, 3500))
                }
            }

            // While loop to check if there are logs in the inventory and if fire exists nearby
            while (true) {
                val hasLogs = !InventoryItemQuery.newQuery(93).ids(fletchID).results().isEmpty

                while (Interfaces.isOpen(1251)) {
                    Zezimax.Logger.log("Fletching $fletchingGetName...")
                    Execution.delay(Navi.random.nextLong(2000, 6400))
                }

                if (!hasLogs) {
                    Zezimax.Logger.log("No more $fletchingGetName in inventory. Returning to bank...")
                    Execution.delay(Navi.random.nextLong(2000, 4000))
                    Zezimax.botState = ZezimaxBotState.FLETCHING_BANKING
                    return
                }
            }
        }
    }
}






