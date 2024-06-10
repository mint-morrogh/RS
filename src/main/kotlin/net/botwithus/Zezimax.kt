package net.botwithus

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.events.impl.InventoryUpdateEvent
import net.botwithus.rs3.events.impl.SkillUpdateEvent
import net.botwithus.rs3.game.Area
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.Coordinate
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
import net.botwithus.rs3.script.config.ScriptConfig
import net.botwithus.rs3.util.Regex
import java.util.*
import java.util.concurrent.Callable
import java.util.regex.Pattern


class Zezimax(
    name: String,
    scriptConfig: ScriptConfig,
    scriptDefinition: ScriptDefinition
) : LoopingScript(name, scriptConfig, scriptDefinition) {

    enum class ZezimaxBotState {
        INITIALIZING,
        IDLE,
        START_MINING,
        MINING,
        NAVIGATING_TO_MINE,
        NAVIGATING_TO_BANK,
        MINING_BANKING,
        START_SECOND_THING
    }

    var botState: ZezimaxBotState = ZezimaxBotState.INITIALIZING
    private var decision: Int? = null
    var someBoolean: Boolean = true

    override fun initialize(): Boolean {
        super.initialize()
        this.sgc = ZezimaxGraphicsContext(this, console)
        botState = ZezimaxBotState.INITIALIZING
        println("Script initialized. State set to INITIALIZING.")
        return true
    }






    // MAIN BOT LOOP //
    override fun onLoop() {
        val player = Client.getLocalPlayer()
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null) {
            println("Player not logged in. Delaying execution.")
            Execution.delay(Navi.random.nextLong(2500, 7500))
            return
        }

        when (botState) {
            ZezimaxBotState.INITIALIZING -> {
                if (initializeBanking()) {
                    botState = ZezimaxBotState.IDLE
                }
            }

            ZezimaxBotState.IDLE -> {
                println("Bot state is IDLE. Decision: $decision")
                when (decision) {
                    0 ->
                        // Mining
                        botState = ZezimaxBotState.START_MINING
                    1 ->
                        // Something Else
                        botState = ZezimaxBotState.IDLE
                    else -> makeRandomDecision()
                }
            }




            // MINING STATES
            ZezimaxBotState.START_MINING -> {
                println("Decided to Mine....")
                botState = ZezimaxBotState.NAVIGATING_TO_MINE
            }
            ZezimaxBotState.NAVIGATING_TO_MINE -> {
                if (Navi.walkToMiningGuild()) {
                    println("Reached mining guild.")
                    botState = ZezimaxBotState.MINING
                } else {
                    println("Walking to mining guild.")
                }
            }
            ZezimaxBotState.NAVIGATING_TO_BANK -> {
                if (Navi.walkToFaladorSmithBank()) {
                    println("Reached Falador smith bank.")
                    botState = ZezimaxBotState.MINING_BANKING
                } else {
                    println("Walking to Falador smith bank.")
                }
            }
            ZezimaxBotState.MINING -> {
                Mining().mine(player)
            }
            ZezimaxBotState.MINING_BANKING -> {
                Banking().bank()
            }

            ZezimaxBotState.START_SECOND_THING -> {
                botState = ZezimaxBotState.INITIALIZING // decision tree here when ready
                return
            }

        }
    }













    private fun initializeBanking(): Boolean {
        val player = Client.getLocalPlayer() ?: return false

        val nearestBank = Navi.getNearestBank(player.coordinate) ?: return false

        if (!nearestBank.contains(player.coordinate)) {
            println("Walking to the nearest bank.")
            val path = NavPath.resolve(nearestBank.randomWalkableCoordinate)
            if (path != null && Movement.traverse(path) != TraverseEvent.State.NO_PATH) {
                Execution.delay(Navi.random.nextLong(1000, 3000))
                return false
            }
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
                return false
            }
        }

        // Deposit all items into the bank
        if (Bank.isOpen()) {
            println("Depositing all items.")
            Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
            Bank.depositAll()
            Execution.delay(Navi.random.nextLong(2000, 4000)) // Simulate deposit delay

            // Make random decision on what to do next
            makeRandomDecision()

            // Close the bank
            Bank.close()
            Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate bank closing delay
            println("Initialization complete. Starting main script.")
            botState = ZezimaxBotState.IDLE
            return true
        } else {
            println("Bank is not open, retrying.")
            Execution.delay(Navi.random.nextLong(1500, 3000))
            return false
        }
    }
    ///////////////////////////////////////////////////////////////
    //////////////// DECISION TREE IS HERE ////////////////////////
    private fun makeRandomDecision() {
        val decision = Navi.random.nextInt(2) // Adjust range if more tasks are added
        println("Decision made: $decision")
        when (decision) {
            0 -> {
                println("Selected task: Mining")
                withdrawMiningSupplies()
                botState = ZezimaxBotState.START_MINING
            }
            1 -> {
                println("Selected task: Nothing... but made a random choice so thats cool.")
                Execution.delay(Navi.random.nextLong(2500, 7500))
                botState = ZezimaxBotState.START_SECOND_THING
            }
            // Add more cases for other tasks here
        }
    }

    private fun withdrawMiningSupplies() {
        if (Bank.isOpen()) {
            val itemName = "Rune ore box"
            val quantity = 1

            // Find the rune ore box in the bank
            val runeOreBox = ComponentQuery.newQuery(517)
                .itemName(itemName)
                .results()
                .firstOrNull()

            if (runeOreBox != null) {
                // Right-click the item and select "Withdraw-X"
                val success = runeOreBox.interact("Withdraw-X")
                println("Attempting Bank Withdraw for $itemName")

                if (success) {
                    // Type the quantity (1 in this case)
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
    }









    inner class Mining {
        private val oreBoxCapacity = 20 // for testing changed from 100
        private var oresInBox = 0
        val oreBox = InventoryItemQuery.newQuery().name("Rune ore box").results().firstOrNull()

        fun mine(player: LocalPlayer) {
            while (true) {
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
                    botState = ZezimaxBotState.NAVIGATING_TO_BANK
                    return
                } else {
                    println("Found ore box: ${oreBox?.name}")
                }


                // Count the total number of runite ore in the inventory
                if (Backpack.isFull() && oreBox != null && oresInBox < oreBoxCapacity) {
                    val oresInInventory =
                        InventoryItemQuery.newQuery().name("Runite ore").results().count { it.name == "Runite ore" }
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
                    botState = ZezimaxBotState.NAVIGATING_TO_BANK
                    return
                }
            }
        }
    }


    inner class Banking {
        fun bank() {
            if (!Backpack.isFull()) {
                println("Backpack is not full, should be mining.")
                botState = ZezimaxBotState.NAVIGATING_TO_MINE
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

            // Deposit all runite ore and empty ore box into the bank
            if (Bank.isOpen()) {
                println("Depositing all runite ore.")
                Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay
                Bank.depositAll("Runite ore")
                Execution.delay(Navi.random.nextLong(1000, 3000)) // Simulate deposit delay

                // Empty the ore box into the bank
                val oreBox = InventoryItemQuery.newQuery().name("Rune ore box").results().firstOrNull()
                if (oreBox != null) {
                    val oreBoxComponent =
                        ComponentQuery.newQuery(517).componentIndex(15).itemName(oreBox.name).option("Empty - ore")
                            .results().firstOrNull()
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

                if (runiteOreCount >= 100) {
                    println("Collected 100 or more Runite Ore. Re-Initializing.")
                    botState = ZezimaxBotState.INITIALIZING // decision tree here when ready
                    return
                } else {
                    botState = ZezimaxBotState.NAVIGATING_TO_MINE
                }
            } else {
                println("Bank is not open, retrying.")
                Execution.delay(Navi.random.nextLong(1500, 3000))
            }
        }
    }
}