package net.botwithus

import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.script.Execution
import java.util.*


fun withdrawSmithingOreSupplies(vararg items: Pair<String, Int?>) {
    if (Bank.isOpen()) {
        val noteButton = ComponentQuery.newQuery(517)
            .componentIndex(127)
            .results()
            .firstOrNull()

        if (noteButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            noteButton.interact()  // This interacts with the note button
            println("Toggled to withdraw items as notes.")
            Execution.delay(Navi.random.nextLong(400, 1000))
        } else {
            println("Withdraw as notes button not found.")
        }

        for ((itemName, quantity) in items) {
            if (quantity == null) {
                // Withdraw all instances of the item
                val success = Bank.withdrawAll(itemName)
                println("Attempting to withdraw all instances of $itemName")

                if (success) {
                    println("Successfully withdrew all instances of $itemName.")
                    Execution.delay(Navi.random.nextLong(1300, 2000))
                } else {
                    println("Failed to withdraw $itemName")
                    Execution.delay(Navi.random.nextLong(1300, 2000))
                }
            }
        }
    }
}

fun depositOreInFurnace() {
    // Navigate to the smithing furnace location
    val reachedFurnace = Navi.walkToFaladorSmithingFurnace()
    if (reachedFurnace) {
        println("Reached Falador Smithing Furnace.")
    } else {
        println("Failed to reach Falador Smithing Furnace.")
    }
    val furnace: SceneObject? = SceneObjectQuery.newQuery().name("Furnace").results().nearest()
    if (furnace != null) {
        furnace.interact("Smelt")
        println("Smelting ore at furnace...")
        Execution.delay(Navi.random.nextLong(1300, 2000))
    }
    val smithDepositAllButton = ComponentQuery.newQuery(37)
        .componentIndex(167)
        .results()
        .firstOrNull()

    if (smithDepositAllButton != null) {
        Execution.delay(Navi.random.nextLong(400, 1000))
        smithDepositAllButton.interact()  // This interacts with the note button
        println("Deposited all ores into Furnace Inventory")
        Execution.delay(Navi.random.nextLong(400, 1000))
    } else {
        println("Couldn't Deposit all ores...")
        Execution.delay(Navi.random.nextLong(400, 1000))
    }
    Zezimax.botState = Zezimax.ZezimaxBotState.START_SMITHING_ORE
}

