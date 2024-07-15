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
import net.botwithus.rs3.script.ScriptConsole
import java.util.concurrent.Callable



fun withdrawMiningSupplies(itemName: String, quantity: Int) {


    if (Bank.isOpen()) {
        // Find the item in the bank
        val orebox = ComponentQuery.newQuery(517)
            .itemName(itemName)
            .results()
            .firstOrNull()

        if (orebox != null) {
            // Right-click the item and select "Withdraw-X"
            val success = orebox.interact("Withdraw-X")
            Zezimax.Logger.log("Attempting Bank Withdraw for $itemName")


            if (success) {
                // Type the quantity
                Execution.delay(Navi.random.nextLong(1300, 2000)) // Short delay before typing
                GameInput.setTextInput(quantity.toString())
                Execution.delay(Navi.random.nextLong(1600, 2600)) // Simulate delay after typing

                Zezimax.Logger.log("Withdrew $quantity $itemName.")
            } else {
                Zezimax.Logger.log("Failed to withdraw $itemName")
            }
        } else {
            Zezimax.Logger.log("Could not find $itemName in the bank.")
        }
    }
    // Move to start mining after withdrawing supplies
    Zezimax.botState = Zezimax.ZezimaxBotState.START_MINING
}


class Mining(private val locationMine: String,
             private val locationBank: String,
             private val rockName: String,
             private val oreName: String,
             private val oreBoxName: String,
             private val mineUntil: Int
) {
    private val oreBoxCapacity = 100
    private var oresInBox = 0
    private val oreBox = InventoryItemQuery.newQuery(93).name(oreBoxName).results().firstOrNull()


    // LOCATIONS
    fun navigateToMineLocation() {
        when (locationMine) {
            "FaladorLuminite" -> Navi.walkToFaladorLuminite()
            "MiningGuild" -> Navi.walkToMiningGuild()
            "VarrockWestMine" -> Navi.walkToVarrockWestMine()
            "VarrockEastMine" -> Navi.walkToVarrockEastMine()
            // Add more cases as needed
            else -> throw IllegalArgumentException("Unknown location: $locationMine")
        }
    }
    fun navigateToBankLocation() {
        when (locationBank) {
            "FaladorSmithBank" -> Navi.walkToFaladorSmithBank()
            "VarrockEastBank" -> Navi.walkToVarrockEastBank()
            "VarrockWestBank" -> Navi.walkToVarrockWestBank()
            // Add more cases as needed
            else -> throw IllegalArgumentException("Unknown bank location: $locationBank")
        }
    }



    fun mine(player: LocalPlayer) {

        Execution.delay(Navi.random.nextLong(1000, 2000))
        navigateToMineLocation()

        while (true) {
            // Mining
            while (!Backpack.isFull()) {
                val rock: SceneObject? = SceneObjectQuery.newQuery().name(rockName).results().nearest()
                if (rock != null && rock.interact("Mine")) {
                    Zezimax.Logger.log("Mining $rockName...")
                    Execution.delay(Navi.random.nextLong(6000, 14000)) // Simulate mining delay
                } else {
                    Zezimax.Logger.log("No $rockName found or failed to interact.")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                }
            }

            // Check if inventory is full during mining process
            if (Backpack.isFull() && oreBox == null) {
                Execution.delay(Navi.random.nextLong(500, 1200))
                Zezimax.Logger.log("No $oreBoxName found, navigating to bank.")
                bank()
                return
            } else {
                Zezimax.Logger.log("Found ore box: ${oreBox?.name}")
            }

            // Count the total number of specified ore in the inventory
            if (Backpack.isFull() && oreBox != null && oresInBox < oreBoxCapacity) {
                val oresInInventory =
                    InventoryItemQuery.newQuery(93).name(oreName).results().count { it.name == oreName }
                Zezimax.Logger.log("Ores in inventory: $oresInInventory")
                val oreBoxComponent =
                    ComponentQuery.newQuery(1473).componentIndex(5).itemName(oreBox.name).option("Fill")
                        .results().firstOrNull()
                if (oreBoxComponent != null && oreBoxComponent.interact("Fill")) {
                    Zezimax.Logger.log("Filling $oreBoxName...")
                    Execution.delay(Navi.random.nextLong(1500, 2200))
                    oresInBox += oresInInventory
                    Zezimax.Logger.log("Filled $oreBoxName. Total ores in box: $oresInBox / $oreBoxCapacity")
                }
            } else {
                Zezimax.Logger.log("$oreBoxName has reached known capacity.")
            }
            if (oreBox != null && oresInBox >= oreBoxCapacity && Backpack.isFull()) {
                Zezimax.Logger.log("$oreBoxName and Backpack are full. Navigating to bank.")
                bank()
                return
            }
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
                Zezimax.Logger.log("Depositing all...")
                Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
                Bank.depositAll(oreName)
                Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
            }
            Execution.delay(Navi.random.nextLong(1000, 2000)) // Simulate deposit delay


            val oreBox = InventoryItemQuery.newQuery(93).name(oreBoxName).results().firstOrNull()
            if (oreBox != null) {
                val oreBoxComponent = ComponentQuery.newQuery(517).componentIndex(15).itemName(oreBox.name).option("Empty - ore").results().firstOrNull()
                if (oreBoxComponent != null && oreBoxComponent.interact("Empty - ore")) {
                    Zezimax.Logger.log("Emptying $oreBoxName.")
                    Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate emptying delay
                } else {
                    Zezimax.Logger.log("Failed to empty $oreBoxName.")
                }
            } else {
                Zezimax.Logger.log("No $oreBoxName found in inventory.")
            }

            val oreCount = Bank.getItems().filter { it.name == oreName }.sumOf { it.stackSize }
            Zezimax.Logger.log("$oreName count in bank: $oreCount")

            Bank.close()
            Execution.delay(Navi.random.nextLong(1000, 2500)) // Simulate bank closing delay

            if (oreCount >= mineUntil) {
                Zezimax.Logger.log("Collected $mineUntil or more $oreName. Re-Initializing.")
                Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING
                return
            } else {
                Zezimax.Logger.log("Continuing to mine more $oreName.")
                Zezimax.botState = Zezimax.ZezimaxBotState.START_MINING
                return
            }
        }
        else {
            Zezimax.Logger.log("Bank is not open, retrying.")
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }
    }
}
