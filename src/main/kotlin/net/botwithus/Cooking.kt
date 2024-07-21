package net.botwithus

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

val cookingGetName = Utilities.getNameById(DecisionTree.fishToCook)

class Cooking(private val fishName: Int,
              private val locationBank: String,
              private val locationRange: String
) {

    // LOCATIONS
    fun navigateToCookLocation() {
        when (locationRange) {
            "AlkharidWestRange" -> Navi.walkToAlkharidWestRange()
            // Add more cases as needed
            else -> throw IllegalArgumentException("Unknown location: $locationRange")
        }
    }
    fun navigateToBankLocation() {
        when (locationBank) {
            "AlkharidWestBank" -> Navi.walkToAlkharidWestBank()
            // Add more cases as needed
            else -> throw IllegalArgumentException("Unknown bank location: $locationBank")
        }
    }

    fun bank() {
        Execution.delay(Navi.random.nextLong(1000, 2000))
        navigateToBankLocation()

        if (!Bank.isOpen()) {
            // Open the bank
            Bank.open()
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }

        if (Bank.isOpen()) {
            if (!Backpack.isEmpty()) {
                Execution.delay(Navi.random.nextLong(1000, 2000))
                Zezimax.Logger.log("Deposit all...")
                Bank.depositAll()
                Execution.delay(Navi.random.nextLong(1000, 2000))
            }
            Zezimax.Logger.log("Withdrawing all $cookingGetName.")

            val fishCount = Bank.getItems().filter { it.id == fishName }.sumOf { it.stackSize }
            Zezimax.Logger.log("$cookingGetName count in bank: $fishCount")
            if (Utilities.isItemIdInBank(fishName)) {
                if (fishCount > 0) {
                    Zezimax.Logger.log("Continuing to cook more $cookingGetName.")
                    Execution.delay(Navi.random.nextLong(1000, 2000))
                    Bank.withdrawAll(fishName)
                    Execution.delay(Navi.random.nextLong(1000, 3000))
                    Bank.close()
                    Execution.delay(Navi.random.nextLong(1000, 2500))
                    Zezimax.botState = Zezimax.ZezimaxBotState.COOKING_RANGE
                    return
                }
            } else {
                Zezimax.Logger.log("No more $cookingGetName to cook, Re-Initializing...")
                Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING
                return
            }
        }
    }

    fun cook(player: LocalPlayer) {

        Execution.delay(Navi.random.nextLong(1000, 2000))
        navigateToCookLocation()
        Execution.delay(Navi.random.nextLong(500, 1500))

        // Interact with the range
        val range: SceneObject? = SceneObjectQuery.newQuery().name("Range").results().nearest()
        if (range != null && range.interact("Cook at")) {
            Zezimax.Logger.log("Interacting with the range.")
            Execution.delay(Navi.random.nextLong(2000, 4000))

            // Click on the 'Cook All' button
            val cookComponent = ComponentQuery.newQuery(1370)
                .componentIndex(30)
                .results()
                .firstOrNull()

            if (cookComponent != null && cookComponent.interact()) {
                Execution.delay(Navi.random.nextLong(2000, 4000))

                while (Interfaces.isOpen(1251)) {
                    Execution.delay(Navi.random.nextLong(3000, 5400))
                }
            }
        }
        Zezimax.botState = Zezimax.ZezimaxBotState.COOKING_BANKING
        return
    }

}




