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
import net.botwithus.rs3.game.scene.entities.characters.player.Player
import net.botwithus.rs3.game.queries.builders.characters.PlayerQuery
import java.util.stream.Collectors


fun withdrawWoodcuttingSupplies(itemName: String, quantity: Int) {


    if (Bank.isOpen()) {
        // Find the item in the bank
        val item = ComponentQuery.newQuery(517)
            .itemName(itemName)
            .results()
            .firstOrNull()

        if (item != null) {
            // Right-click the item and select "Withdraw-X"
            val success = item.interact("Withdraw-X")
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

class Woodcutting(private val locationWoodcutting: String,
             private val locationBank: String,
             private val treeName: String,
             private val logName: String,
             private val woodBoxName: String,
             private val randStart: Long,
             private val randEnd: Long,
             private val woodcutUntil: Int
) {
    private val woodBoxCapacity = 90
    private var woodInBox = 0
    private val woodBox = InventoryItemQuery.newQuery().name(woodBoxName).results().firstOrNull()


    // LOCATIONS
    fun navigateToWoodcuttingLocation() {
        when (locationWoodcutting) {
            "VarrockWestTrees" -> Navi.walkToVarrockWestTrees()
            "DraynorOaks" -> Navi.walkToDraynorOaks()
            "DraynorWillows" -> Navi.walkToDraynorWillows()
            // Add more cases as needed
            else -> throw IllegalArgumentException("Unknown location: $locationWoodcutting")
        }
    }
    fun navigateToBankLocation() {
        when (locationBank) {
            "DraynorBank" -> Navi.walkToDraynorBank()
            "VarrockWestBank" -> Navi.walkToVarrockWestBank()
            // Add more cases as needed
            else -> throw IllegalArgumentException("Unknown bank location: $locationBank")
        }
    }


    fun findNextAvailableTree(player: LocalPlayer): SceneObject? {
        val currentPosition = player.coordinate

        // Query to find trees that match the criteria
        val trees = SceneObjectQuery.newQuery()
            .name(treeName)
            .option("Chop down")
            .hidden(false)
            .results()
            .stream()
            .filter { (it.coordinate?.distanceTo(currentPosition) ?: Double.MAX_VALUE) <= 15 } // Filter trees within 15 spaces
            .sorted { o1, o2 ->
                val distance1 = o1.coordinate?.distanceTo(currentPosition) ?: Double.MAX_VALUE
                val distance2 = o2.coordinate?.distanceTo(currentPosition) ?: Double.MAX_VALUE
                distance1.compareTo(distance2)
            }
            .collect(Collectors.toList())

        // Check for the first tree without players nearby animating
        for (tree in trees) {
            val treePosition = tree.coordinate
            val nearbyPlayers = PlayerQuery.newQuery().results().stream()
                .filter { it.coordinate != null && treePosition?.let { it1 -> it.coordinate!!.distanceTo(it1) }!! <= 1 && it.name != player.name && it.animationId != -1 }
                .collect(Collectors.toList())

            if (nearbyPlayers.isEmpty()) {
                Zezimax.Logger.log("Found free tree within 15 spaces")
                return tree
            }
        }

        // If all trees within 10 spaces have players nearby animating, return the nearest tree regardless
        Zezimax.Logger.log("Could not find a free tree within 10 spaces, choosing the nearest tree regardless")
        return SceneObjectQuery.newQuery().name(treeName).option("Chop down").hidden(false).results().nearest()
    }


    fun woodcut(player: LocalPlayer) {
        navigateToWoodcuttingLocation()
        var currentTree: SceneObject? = null

        while (true) {
            while (!Backpack.isFull()) {
                // Log names of nearby players excluding the local player
                val currentPosition = player.coordinate

                // Check if we need to find a new tree
                if (currentTree == null || player.animationId == -1 || (currentTree.coordinate?.distanceTo(
                        currentPosition
                    ) ?: Double.MAX_VALUE) > 1
                ) {
                    currentTree = findNextAvailableTree(player)
                }

                // Attempt to interact with the current tree
                if (currentTree != null && currentTree.interact("Chop down")) {
                    Zezimax.Logger.log("Woodcutting ${currentTree.name}...")
                    Execution.delay(Navi.random.nextLong(randStart, randEnd)) // Simulate chopping delay
                } else {
                    Zezimax.Logger.log("No ${currentTree?.name} found or failed to interact.")
                    Execution.delay(Navi.random.nextLong(1500, 3000))
                }
            }

            // Check if inventory is full during woodcutting process
            if (Backpack.isFull() && woodBox == null) {
                Execution.delay(Navi.random.nextLong(500, 1200))
                Zezimax.Logger.log("No $woodBoxName found, navigating to bank.")
                bank()
                return
            } else {
                Zezimax.Logger.log("Found wood box: ${woodBox?.name}")
            }

            // Count the total number of specified wood in the inventory
            if (Backpack.isFull() && woodBox != null && woodInBox < woodBoxCapacity) {
                val logsInInventory =
                    InventoryItemQuery.newQuery().name(logName).results().count { it.name == logName }
                Zezimax.Logger.log("Logs in inventory: $logsInInventory")
                val woodBoxComponent =
                    ComponentQuery.newQuery(1473).componentIndex(5).itemName(woodBox.name).option("Fill")
                        .results().firstOrNull()
                if (woodBoxComponent != null && woodBoxComponent.interact("Fill")) {
                    Zezimax.Logger.log("Filling $woodBoxName...")
                    Execution.delay(Navi.random.nextLong(1500, 2200))
                    woodInBox += logsInInventory
                    Zezimax.Logger.log("Filled $woodBoxName. Total logs in box: $woodInBox / $woodBoxCapacity")
                }
            } else {
                Zezimax.Logger.log("$woodBoxName has reached known capacity.")
            }
            if (woodBox != null && woodInBox >= woodBoxCapacity && Backpack.isFull()) {
                Zezimax.Logger.log("$woodBoxName and Backpack are full. Navigating to bank.")
                bank()
                return
            }
        }
    }

    fun bank() {

        navigateToBankLocation()

        if (!Bank.isOpen()) {
            // Open the bank
            Bank.open()
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }

        if (Bank.isOpen()) {
            if (!Backpack.isEmpty()) {
                Zezimax.Logger.log("Depositing all $logName.")
                Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
                Bank.depositAll(logName)
                Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
            }

            val woodBox = InventoryItemQuery.newQuery().name(woodBoxName).results().firstOrNull()
            if (woodBox != null) {
                val woodBoxComponent = ComponentQuery.newQuery(517).componentIndex(15).itemName(woodBox.name).option("Empty - logs and bird's nests").results().firstOrNull()
                if (woodBoxComponent != null && woodBoxComponent.interact("Empty - logs and bird's nests")) {
                    Zezimax.Logger.log("Emptying $woodBoxName.")
                    Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate emptying delay
                } else {
                    Zezimax.Logger.log("Failed to empty $woodBoxName.")
                }
            } else {
                Zezimax.Logger.log("No $woodBoxName found in inventory.")
            }

            val logCount = Bank.getItems().filter { it.name == logName }.sumOf { it.stackSize }
            Zezimax.Logger.log("$logName count in bank: $logCount")

            Bank.close()
            Execution.delay(Navi.random.nextLong(1000, 2500)) // Simulate bank closing delay

            if (logCount >= woodcutUntil) {
                Zezimax.Logger.log("Collected $woodcutUntil or more $logName. Re-Initializing.")
                Zezimax.botState = Zezimax.ZezimaxBotState.INITIALIZING
                return
            } else {
                Zezimax.Logger.log("Continuing to woodcut more $logName.")
                Zezimax.botState = Zezimax.ZezimaxBotState.START_WOODCUTTING
                return
            }
        }
        else {
            Zezimax.Logger.log("Bank is not open, retrying.")
            Execution.delay(Navi.random.nextLong(1500, 3000))
        }
    }
}