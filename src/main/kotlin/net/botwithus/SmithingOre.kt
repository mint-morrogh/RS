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

fun smithOre() {
    while (true) {
        // Select bar in interface - component 37, 103, subcomponent 11 for "Rune bar"
        val runeBarComponent = ComponentQuery.newQuery(37)
            .componentIndex(103)
            .subComponentIndex(11)
            .results()
            .firstOrNull()

        if (runeBarComponent != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            runeBarComponent.interact("Select Rune bar")
            println("Selected Rune bar.")
            Execution.delay(Navi.random.nextLong(400, 1000))
        } else {
            println("Rune bar component not found.")
        }

        // Press begin project button - component 37, 163
        val beginProjectButton = ComponentQuery.newQuery(37)
            .componentIndex(163)
            .results()
            .firstOrNull()

        if (beginProjectButton != null) {
            Execution.delay(Navi.random.nextLong(400, 1000))
            beginProjectButton.interact()
            println("Pressed Begin Project button.")
            Execution.delay(Navi.random.nextLong(3000, 5000))
        } else {
            println("Begin Project button not found.")
        }

        // Check smithing interface
        val smithingInterface = ComponentQuery.newQuery(838)
            .results()
            .firstOrNull()

        if (smithingInterface != null) {
            // Click deposit materials
            val smithDepositAllButton = ComponentQuery.newQuery(37)
                .componentIndex(167) // Assuming component index for "Deposit All"
                .results()
                .firstOrNull()

            if (smithDepositAllButton != null) {
                Execution.delay(Navi.random.nextLong(1500, 2500))
                smithDepositAllButton.interact()
                println("Deposited all materials.")
            } else {
                println("Deposit All button not found.")
            }

            Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING // Decision tree here when ready
            return
        } else {
            // WAIT FOR EXP INTERFACE TO GO AWAY
            Execution.delay(Navi.random.nextLong(2000, 6000))
            // Check exp interface
            while (!ComponentQuery.newQuery(1251).results().isEmpty) {
                Execution.delay(Navi.random.nextLong(2000, 6000))
            }
        }

        // Interact with furnace
        val furnace = SceneObjectQuery.newQuery()
            .name("Furnace")
            .results()
            .nearest()

        if (furnace != null) {
            furnace.interact("Smelt")
            println("Interacted with Furnace to Smelt.")
            Execution.delay(Navi.random.nextLong(1200, 1900))
        } else {
            println("Furnace not found.")
            Execution.delay(Navi.random.nextLong(1200, 1900))
        }

        // Click deposit materials again
        val smithDepositAllButton2 = ComponentQuery.newQuery(37)
            .componentIndex(167) // Assuming component index for "Deposit All"
            .results()
            .firstOrNull()

        if (smithDepositAllButton2 != null) {
            Execution.delay(Navi.random.nextLong(1500, 2500))
            smithDepositAllButton2.interact()
            println("Deposited all materials.")
        } else {
            println("Deposit All button not found.")
        }
    }
}
// End of loop
