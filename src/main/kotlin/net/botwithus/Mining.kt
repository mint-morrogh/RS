package net.botwithus

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.input.GameInput
import net.botwithus.rs3.script.Execution
import java.util.*
import java.util.concurrent.Callable


fun withdrawMiningSupplies(itemName: String, quantity: Int) {
    if (Bank.isOpen()) {
        // Find the item in the bank
        val item = ComponentQuery.newQuery(517)
            .itemName(itemName)
            .results()
            .firstOrNull()

        if (item != null) {
            // Right-click the item and select "Withdraw-X"
            val success = item.interact("Withdraw-X")
            println("Attempting Bank Withdraw for $itemName")

            if (success) {
                // Type the quantity
                Execution.delay(Navi.random.nextLong(1300, 2000)) // Short delay before typing
                GameInput.setTextInput(quantity.toString())
                Execution.delay(Navi.random.nextLong(1600, 2600)) // Simulate delay after typing

                println("Withdrew $quantity $itemName.")
            } else {
                println("Failed to withdraw $itemName")
            }
        } else {
            println("Could not find $itemName in the bank.")
        }
    }
    // Move to start mining after withdrawing supplies
    Zezimax.botState = Zezimax.ZezimaxBotState.START_MINING
}


class Mining(private val rockName: String, private val oreName: String, private val oreBoxName: String) {
    private val oreBoxCapacity = 100
    private var oresInBox = 0
    val oreBox = InventoryItemQuery.newQuery().name(oreBoxName).results().firstOrNull()

    fun mine(player: LocalPlayer) {
        while (true) {
            // Mining
            while (!Backpack.isFull()) {
                val rock: SceneObject? = SceneObjectQuery.newQuery().name(rockName).results().nearest()
                if (rock != null && rock.interact("Mine")) {
                    println("Mining $rockName...")
                    Execution.delay(Navi.random.nextLong(6000, 14000)) // Simulate mining delay
                } else {
                    println("No $rockName found or failed to interact.")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                }
            }

            // Check if inventory is full during mining process
            if (Backpack.isFull() && oreBox == null) {
                Execution.delay(Navi.random.nextLong(500, 1200))
                println("No $oreBoxName found, navigating to bank.")
                Zezimax.botState = Zezimax.ZezimaxBotState.NAVIGATING_TO_BANK
                return
            } else {
                println("Found ore box: ${oreBox?.name}")
            }

            // Count the total number of specified ore in the inventory
            if (Backpack.isFull() && oreBox != null && oresInBox < oreBoxCapacity) {
                val oresInInventory =
                    InventoryItemQuery.newQuery().name(oreName).results().count { it.name == oreName }
                println("Ores in inventory: $oresInInventory")
                val oreBoxComponent =
                    ComponentQuery.newQuery(1473).componentIndex(5).itemName(oreBox.name).option("Fill")
                        .results().firstOrNull()
                if (oreBoxComponent != null && oreBoxComponent.interact("Fill")) {
                    println("Filling $oreBoxName...")
                    Execution.delay(Navi.random.nextLong(1500, 2200))
                    oresInBox += oresInInventory
                    println("Filled $oreBoxName. Total ores in box: $oresInBox / $oreBoxCapacity")
                }
            } else {
                println("$oreBoxName has reached known capacity.")
            }
            if (oreBox != null && oresInBox >= oreBoxCapacity && Backpack.isFull()) {
                println("$oreBoxName and Backpack are full. Navigating to bank.")
                Zezimax.botState = Zezimax.ZezimaxBotState.NAVIGATING_TO_BANK
                return
            }
        }
    }
}

class MiningBanking(private val oreName: String, private val oreBoxName: String, private val oreNeeded: Int) {
    fun bank() {
        if (!Backpack.isFull()) {
            println("Backpack is not full, should be mining.")
            Zezimax.botState = Zezimax.ZezimaxBotState.NAVIGATING_TO_MINE
            return
        }

        if (!Bank.isOpen()) {
            // Open the bank
            val bankBooth: SceneObject? =
                SceneObjectQuery.newQuery().name("Bank booth", "Bank chest", "Counter").results().nearest()
            if (bankBooth != null && (bankBooth.interact("Bank") || bankBooth.interact("Use"))) {
                println("Interacting with bank booth or chest.")
                Execution.delayUntil(5000, Callable { Bank.isOpen() })
            } else {
                println("No bank booth or chest found or failed to interact.")
                Execution.delay(Navi.random.nextLong(1500, 3000))
                return
            }
        }

        // Deposit all specified ore and empty ore box into the bank
        if (Bank.isOpen()) {
            println("Depositing all $oreName.")
            Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
            Bank.depositAll(oreName)
            Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay

            // Empty the ore box into the bank
            val oreBox = InventoryItemQuery.newQuery().name(oreBoxName).results().firstOrNull()
            if (oreBox != null) {
                val oreBoxComponent =
                    ComponentQuery.newQuery(517).componentIndex(15).itemName(oreBox.name).option("Empty - ore")
                        .results().firstOrNull()
                if (oreBoxComponent != null && oreBoxComponent.interact("Empty - ore")) {
                    println("Emptying $oreBoxName.")
                    Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate emptying delay
                } else {
                    println("Failed to empty $oreBoxName.")
                }
            } else {
                println("No $oreBoxName found in inventory.")
            }

            // Check the amount of specified Ore in the bank
            val oreCount = Bank.getItems().filter { it.name == oreName }.sumOf { it.stackSize }
            println("$oreName count in bank: $oreCount")

            // Close the bank
            Bank.close()
            Execution.delay(Navi.random.nextLong(1000, 2500)) // Simulate bank closing delay

            if (oreCount >= oreNeeded) {
                println("Collected 100 or more $oreName. Re-Initializing.")
                Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING // decision tree here when ready
                return
            } else {
                Zezimax.botState = Zezimax.ZezimaxBotState.NAVIGATING_TO_MINE
            }
        } else {
            println("Bank is not open, retrying.")
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }
    }
}

