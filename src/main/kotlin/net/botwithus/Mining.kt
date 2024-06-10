package net.botwithus

import net.botwithus.Zezimax.ZezimaxBotState
import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.script.Execution
import java.util.concurrent.Callable

object Mining {
    private val oreBoxCapacity = 20 // for testing changed from 100
    private var oresInBox = 0
    private var botState: ZezimaxBotState = ZezimaxBotState.INITIALIZING
    private val oreBox = InventoryItemQuery.newQuery().name("Rune ore box").results().firstOrNull()

    private fun updateBotState(newState: ZezimaxBotState) {
        botState = newState
        println("Bot state updated to: $newState")
    }

    fun mine(player: LocalPlayer) {
        while (botState == ZezimaxBotState.MINING) {
            // Mining
            while (!Backpack.isFull()) {
                val runeRock: SceneObject? = SceneObjectQuery.newQuery().name("Runite rock").results().nearest()
                if (runeRock != null && runeRock.interact("Mine")) {
                    println("Mining rune rock...")
                    Execution.delay(Navi.random.nextLong(6000, 14000)) // Simulate mining delay
                } else {
                    println("No rune rock found or failed to interact.")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                }
            }

            // Check if inventory is full during mining process
            if (Backpack.isFull() && oreBox == null) {
                Execution.delay(Navi.random.nextLong(500, 1200))
                println("No ore box found, navigating to bank.")
                updateBotState(ZezimaxBotState.NAVIGATING_TO_BANK)
                return
            } else {
                println("Found ore box: ${oreBox?.name}")
            }

            // Count the total number of runite ore in the inventory
            if (Backpack.isFull() && oreBox != null && oresInBox < oreBoxCapacity) {
                val oresInInventory = InventoryItemQuery.newQuery().name("Runite ore").results().count { it.name == "Runite ore" }
                println("Ores in inventory: $oresInInventory")
                val oreBoxComponent =
                    ComponentQuery.newQuery(1473).componentIndex(5).itemName(oreBox.name).option("Fill")
                        .results().firstOrNull()
                if (oreBoxComponent != null && oreBoxComponent.interact("Fill")) {
                    println("Filling ore box...")
                    Execution.delay(Navi.random.nextLong(1500, 2200))
                    oresInBox += oresInInventory
                    println("Filled ore box. Total ores in box: $oresInBox / $oreBoxCapacity")
                }
            } else {
                println("Ore box has reached known capacity.")
            }
            if (oreBox != null && oresInBox >= oreBoxCapacity && Backpack.isFull()) {
                println("Ore box and Backpack are full. Navigating to bank.")
                updateBotState(ZezimaxBotState.NAVIGATING_TO_BANK)
                return
            }
        }
    }

    fun bank() {
        if (!Backpack.isFull()) {
            println("Backpack is not full, should be mining.")
            updateBotState(ZezimaxBotState.NAVIGATING_TO_MINE)
            return
        }

        if (!Bank.isOpen()) {
            attemptToOpenBank()
        }

        if (Bank.isOpen()) {
            depositItems()
        } else {
            println("Bank is not open, retrying.")
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }
    }

    private fun attemptToOpenBank() {
        val bankBooth: SceneObject? = SceneObjectQuery.newQuery().name("Bank booth", "Bank chest", "Counter").results().nearest()
        if (bankBooth != null && bankBooth.interact("Bank")) {
            println("Interacting with bank booth or chest.")
            Execution.delayUntil(5000, Callable { Bank.isOpen() })
        } else {
            println("No bank booth or chest found or failed to interact.")
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }
    }

    private fun depositItems() {
        println("Depositing all runite ore.")
        Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
        Bank.depositAll("Runite ore")
        Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay

        // Empty the ore box into the bank
        val oreBox = InventoryItemQuery.newQuery().name("Rune ore box").results().firstOrNull()
        if (oreBox != null) {
            val oreBoxComponent = ComponentQuery.newQuery(517).componentIndex(15).itemName(oreBox.name).option("Empty - ore").results().firstOrNull()
            if (oreBoxComponent != null && oreBoxComponent.interact("Empty - ore")) {
                println("Emptying rune ore box.")
                Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate emptying delay
            } else {
                println("Failed to empty rune ore box.")
            }
        } else {
            println("No rune ore box found in inventory.")
        }

        // Check the amount of Runite Ore in the bank
        val runiteOreCount = Bank.getItems().filter { it.name == "Runite ore" }.sumOf { it.stackSize }
        println("Runite ore count in bank: $runiteOreCount")

        // Close the bank
        Bank.close()
        Execution.delay(Navi.random.nextLong(1000, 2500)) // Simulate bank closing delay

        if (runiteOreCount >= 300) {
            println("Collected 300 or more Runite Ore. Stopping script.")
            updateBotState(ZezimaxBotState.IDLE)
        } else {
            updateBotState(ZezimaxBotState.NAVIGATING_TO_MINE)
        }
    }
}
